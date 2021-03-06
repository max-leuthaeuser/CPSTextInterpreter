package de.qualitune

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

import de.qualitune.ast.CPSProgram
import de.qualitune.parser.CPSTextParser
import java.io.{InputStreamReader, BufferedReader, File}
import de.qualitune.util.IOUtils
import tools.nsc.Global
import de.qualitune.config.{ConfigReporter, Configuration}
import de.qualitune.checks.CPSChecks
import transformator.{CPSProgramTransformator, ExecutableString}

/**
 * Interpreter for CPSText containing static methods for interpreting CPSText code and programs.
 *
 * @author Max Leuthaeuser
 * @since 22.11.2011
 */
object CPSTextRunner {
  /**
   * Runs a CPSProgram AST representing a piece of CPSText code.
   *
   * @param cst: the CPSProgram representing the concrete syntax tree
   * @param config: the current configuration
   */

  def runCST(cst: CPSProgram, config: Configuration) {
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

    var jre = "java"
    var removeFile = "rm cpsprogram_Main.scala"
    var removeClasses = "rm -rf temp"
    val sep = System.getProperties.getProperty("path.separator");

    if (IOUtils.isWindows) {
      jre = "cmd.exe /C " + jre
      removeFile = "cmd.exe /C del /S cpsprogram_Main.scala"
      removeClasses = "cmd.exe /C RD /S /q temp"
    }

    config.debugging.write("# Starting")

    if (config.interpretation.enabled) {
      IOUtils.Time("Interpretation") {
        val s = new ExecutableString()
        s + ("object cpsprogram_Main {\n")
        new CPSProgramTransformator().apply(s, cst, null)
        s + ("def main(args: Array[String]) { " + s.getInPlace + "} \n}")

        IOUtils.writeToFile("cpsprogram_Main.scala", s.toString)

        IOUtils.createDirectory(new File("temp"))

        val settings = new scala.tools.nsc.Settings(error)
        settings.outdir.value = "temp"
        settings.classpath.value = System.getProperty("java.class.path", ".");
        settings.deprecation.value = true
        settings.unchecked.value = true
        val reporter = new ConfigReporter(settings, config)
        val compiler = new Global(settings, reporter)
        (new compiler.Run).compile(List("cpsprogram_Main.scala"))

        config.debugging.write("# Output of compilation process: \n")
        reporter.printSummary()
        config.debugging.write("# Finished. Exit code: " + (if (reporter.hasErrors) 1 else 0))
      }
    }

    if (config.execution.enabled) {
      IOUtils.Time("Execution") {
        val proc = Runtime.getRuntime.exec(jre + " -cp temp" + sep + "CPSTextInterpreter.jar" + sep + ". cpsprogram_Main", null, new File("."))
        config.debugging.write("# Output of CPSText program: \n")
        val reader = new BufferedReader(new InputStreamReader(proc.getInputStream))
        Stream.continually(reader.readLine()).takeWhile(_ != null).foreach(x => config.debugging.write(" > " + IOUtils.now + ": " + x))
        val exitCode = proc.waitFor()
        config.debugging.write("\n# Finished. Exit code: " + exitCode)
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
   * Parses and runs a String containing CPSText code.
   *
   * @param code: the piece of CPSText code you want to interpret.
   * @param config: the current configuration
   */
  def runCode(code: String, config: Configuration) {
    runCST(CPSTextParser.parse(code), config)
  }
}
