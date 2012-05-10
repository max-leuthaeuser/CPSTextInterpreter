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

package de.qualitune

import config.{Configuration, ConfigReader}
import scala.io.Source._
import de.qualitune.parser.CPSTextParser
import java.io.FileNotFoundException

/**
 * @author Max Leuthaeuser
 * @since 22.11.2011
 */
object Main {
  // TODO: use templates for code generation globally
  private val help = """Usage:
    -p filename : parses the given CPSText file
    -i filename : parses and interpretes the given CPSText file
    -h : show this help"""

  def main(args: Array[String]) {
    if (args.isEmpty) {
      println(help)
      System.exit(1)
    }
    val option = args(0)
    option match {
      case "-p" => println(CPSTextParser.parse(fromFile(args.toList(1)).mkString))
      case "-i" => {
        // read config first
        var config: Configuration = null
        try {
          config = ConfigReader.parse(fromFile("config/run.conf").mkString)
        } catch {
          case e: FileNotFoundException => println("Configuration not found!")
        }
        CPSTextRunner.runCode(fromFile(args.toList(1)).mkString, config)
      }
      case _ => println(help)
    }
  }
}