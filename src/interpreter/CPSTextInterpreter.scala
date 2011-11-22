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

package interpreter

import ast.CPSProgram
import parser.CPSTextParser

/**
 * Interpreter for CPSText containing static methods for interpreting CPSText code and programs.
 *
 * @author Max Leuthaeuser
 * @date 22.11.2011
 */
object CPSTextInterpreter {
  val debug = false

  /**
   * Interprets a CPSProgram representing a piece of CPSText code.
   *
   * @param cst: the CPSProgram representing the concrete syntax tree
   * @param db: optional boolean flag, set to true if you want additional debug information printed to stdout. (predefined: false)
   * @return true if interpretation was successful, false otherwise.
   */
  def interpret(cst: CPSProgram, db: Boolean = false): Boolean = {
    false
  }

  /**
   * Parses and interprets a String containing CPSText code.
   *
   * @param code: the piece of CPSText code you want to interpret.
   * @param db: optional boolean flag, set to true if you want additional debug information printed to stdout. (predefined: false)
   * @return true if parsing and interpretation was successful, false otherwise.
   */
  def interpret(code: String, db: Boolean = false): Boolean = {
    interpret(CPSTextParser.parse(code), debug)
  }
}