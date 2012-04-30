/**
 * CPSTextInterpreter - parses and interprets the CPSText DSL.
 * Copyright (C) 2011 Max Leuthaeuser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.qualitune.interpreter

import de.qualitune.ast.CPSProgram
import de.qualitune.parser.CPSTextParser
import java.io.{InputStreamReader, BufferedReader, File}
import de.qualitune.config.Configuration
import de.qualitune.util.IOUtils

/**
 * Interpreter for CPSText containing static methods for interpreting CPSText code and programs.
 *
 * @author Max Leuthaeuser
 * @since 22.11.2011
 */
object CPSTextInterpreter {
  /**
   * Interprets a CPSProgram representing a piece of CPSText code.
   *
   * @param cst: the CPSProgram representing the concrete syntax tree
   * @param config: the current configuration
   */

  def interpretCST(cst: CPSProgram, config: Configuration) {
    // Some static checks before starting the actual interpretation.
    if (config.debugging.enabled) {
      config.debugging.write("Running static checks...")
      config.debugging.write("\t1) Checking names")
    }
    CPSChecks.checkNames(cst)
    if (config.debugging.enabled) config.debugging.write("\t2) Checking imports")
    CPSChecks.checkImports(cst)
    if (config.debugging.enabled) config.debugging.write("\t2) Checking role bindings")
    CPSChecks.checkBindings(cst)
    if (config.debugging.enabled) config.debugging.write("\t4) Checking CPS objects")
    CPSChecks.checkCPSObjects(cst)
    if (config.debugging.enabled) config.debugging.write("\t5) Checking role constrains")
    CPSChecks.checkConstrains(cst)

    var compiler = "scalac"
    var jre = "java"
    var removeFile = "rm cpsprogram_Main.scala"
    var removeClasses = "rm temp/*.class"
    val sep = System.getProperties.getProperty("path.separator");

    if (IOUtils.isWindows) {
      compiler = "cmd.exe /C " + compiler
      jre = "cmd.exe /C " + jre
      removeFile = "cmd.exe /C del /S cpsprogram_Main.scala"
      removeClasses = "cmd.exe /C cd temp && del *.class"
    }

    config.debugging.write("# Starting")

    if (config.interpretation.enabled) {
      IOUtils.Time("Interpretation") {
        val s = new EvaluableString()
        s + ("object cpsprogram_Main {\n")
        new CPSProgramInterpreter().apply(s, cst, null)
        s + ("def main(args: Array[String]) { " + s.getInPlace + "} \n}")

        IOUtils.writeToFile("cpsprogram_Main.scala", s.toString)

        IOUtils.createDirectory(new File("temp"))

        // TODO fix compile bug, which leads to infinite runtime sometimes
        /**
        val proc = Runtime.getRuntime().exec(compiler + " -d temp -cp CPSTextInterpreter.jar cpsprogram_Main.scala", null, new File("."))
        config.debugging.write("# Output of compilation process: \n")
        val reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))
        Stream.continually(reader.readLine()).takeWhile(_ != null).foreach(config.debugging.write(_))
        val exitCode = proc.waitFor()
        config.debugging.write("# Finished. Exit code: " + exitCode)
         */
      }
    }

    if (config.execution.enabled) {
      IOUtils.Time("Execution") {
        val proc = Runtime.getRuntime.exec(jre + " -cp temp" + sep + "CPSTextInterpreter.jar" + sep + ". cpsprogram_Main", null, new File("."))
        config.debugging.write("# Output of CPSText program: \n")
        val reader = new BufferedReader(new InputStreamReader(proc.getInputStream))
        Stream.continually(reader.readLine()).takeWhile(_ != null).foreach(x => config.debugging.write(" > " + IOUtils.now + ": " + x))
        val exitCode = proc.waitFor()
        config.debugging.write("# Finished. Exit code: " + exitCode)
      }
    }

    if (config.clean) {
      IOUtils.Time("Cleaning up") {
        Runtime.getRuntime.exec(removeFile).waitFor()
        Runtime.getRuntime.exec(removeClasses).waitFor()
      }
    }
    config.debugging.write("# Shutting down")
  }

  /**
   * Parses and interprets a String containing CPSText code.
   *
   * @param code: the piece of CPSText code you want to interpret.
   * @param config: the current configuration
   */
  def interpretCode(code: String, config: Configuration) {
    interpretCST(CPSTextParser.parse(code), config)
  }
}
