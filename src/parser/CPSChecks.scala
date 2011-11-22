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

package parser

import ast.CPSProgram

/**
 *  Object containing some methods for checking a CPSProgram statically.
 *
 * @author Max Leuthaeuser
 * @date 22.11.2011
 */
object CPSChecks {
  /**
   * Check if names are used more than once and hiding or shadowing is possible to
   * generate warnings.
   *
   * @param cst: CPSProgram to check
   */
  def checkNames(cst: CPSProgram) {

  }

  /**
   * Check if all given bindings from CPS object to roles are well formed.
   *
   * @param cst: CPSProgram to check
   */
  def checkBindings(cst: CPSProgram) {

  }

  /**
   * Check if all role definitions are well formed, hence no cyclic playedBy definitions.
   *
   * @param cst: CPSProgram to check
   */
  def checkRoles(cst: CPSProgram) {

  }

  /**
   * Check if CPSObjects are well formed, hence no same IP:PORT or same name for different devices.
   *
   * @param cst: CPSProgram to check
   */
  def checkCPSObjects(cst: CPSProgram) {

  }
}