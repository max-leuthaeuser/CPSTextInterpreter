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

package ast.role

import ast.callable.{Operation, Behavior}
import ast.variable.VariableDecl

case class Role(name: String, behavior: Behavior, variables: List[VariableDecl], operations: List[Operation], playedBy: String) {
  def prettyPrint(identLevel: Int): String = {
    var ident = ""
    (1 to identLevel).foreach(e => {
      ident += "\t"
    })

    var v = ""
    if (!variables.isEmpty) v = variables.map(_.toString).mkString("\t\t" + ident, "\n\t\t" + ident, "")
    if (!variables.isEmpty && !operations.isEmpty) v += "\n"

    var o = ""
    if (!operations.isEmpty) o = operations.map(_.prettyPrint(identLevel)).mkString("\t\t" + ident, "\n\n\t\t" + ident, "")


    "role " + name + " playerBy " + playedBy + " {\n" +
      "\t" + ident + behavior.prettyPrint(identLevel) + "\n\n" + v + o + "\n\t" + ident + "}"
  }

  override def toString = prettyPrint(0)
}