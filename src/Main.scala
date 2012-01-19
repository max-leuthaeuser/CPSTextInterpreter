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

import scala.io.Source._
import interpreter.CPSTextInterpreter
import parser.CPSTextParser

object Main {
  private val help = """Usage:
    -p filename : parses the given CPSText file
    -i filename : parses and interpretes the given CPSText file
    -h : show this help"""

  def main(args: Array[String]) {
    if (args.isEmpty) {
      println(help)
      exit(1)
    }
    val option = args(0)
    val code = fromFile(args.toList(1)).mkString
    option match {
      case "-p" => println(CPSTextParser.parse(code))
      case "-i" => println(CPSTextInterpreter.interpretCode(code))
      case _ => println(help)
    }
  }
}