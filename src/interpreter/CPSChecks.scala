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

import ast.{CPSProgram, Context}

/**
 *  Object containing some methods for checking a CPSProgram statically.
 *
 * @author Max Leuthaeuser
 * @date 22.11.2011
 */
object CPSChecks {
  /**
   * Check if names are used more than once and hiding is possible to
   * generate warnings.
   *
   * @param cst: CPSProgram to check
   * @throws DuplicateNameException if the same name is used multiple times at one specific level.
   */
  def checkNames(cst: CPSProgram) {
    var contextNames = List[String]()
    var roleNames = List[String]()

    def checkContextNames(c: List[Context]) {
      c.foreach(e => {
        if (contextNames.contains(e.name))
          throw new DuplicateNameException("Context '" + e.name + "' is already defined!")
        else
          contextNames = e.name :: contextNames
      })
      c.foreach(e => {
        checkContextNames(e.inner)
      })
    }

    def checkRoleNames(c: List[Context]) {
      c.foreach(e => {
        e.roles.foreach(r => {
          if (roleNames.contains(r.name))
            throw new DuplicateNameException("Role '" + e.name + "' is already defined!")
          else
            roleNames = r.name :: roleNames
        })
      })
      c.foreach(e => {
        checkRoleNames(e.inner)
      })
    }

    /**
     * Algorithm:
     *  - run through all contexts and
     *  - run through all roles
     * and check if their names and embedded variables are unique.
     *
     * Same name for role or contexts generates error, same name for variables on top level
     * generate error, same name for variables in embedded contexts/roles will generate hiding warning.
     */
    checkContextNames(cst.contexts)
    checkRoleNames(cst.contexts)
    // TODO check variables
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