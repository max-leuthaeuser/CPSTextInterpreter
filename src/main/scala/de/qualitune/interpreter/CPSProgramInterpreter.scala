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

import de.qualitune.ast.{ASTElement, CPSProgram}


/**
 * @author Max Leuthaeuser
 * @since 19.01.12
 */
class CPSProgramInterpreter extends ASTElementInterpreter {
  override def apply[E <: ASTElement, T <: AnyRef](s: EvaluableString, elem: E, data: T) = {
    elem match {
      case c: CPSProgram => {
        // imports
        // some standard imports first, they are always needed
        s + """import scala.actors.Actor
			import de.qualitune.roles.ComponentRole
			import de.qualitune.roles.ComponentCore
			import de.qualitune.roles.players.Nao
			import de.qualitune.roles.players.Nao._
			import collection.mutable.Map
			import de.qualitune.util.ListUtils
			"""

        s ++ c.imports.map("import " + _ + "\n") + "\n"

        // cps
        s + "object CPS {"
        c.robots.foreach(new CPSTypeInterpreter()(s, _, null))
        s + "}\n import CPS._\n"

        // global registry for core object with their roles
        s + "object Registry {"
        s + " @volatile var cores = List[ComponentCore]()"
        s + "}\n"


        // contexts
        val allRoles = c.getAllRoles()
        c.contexts.foreach(x => new ContextInterpreter()(s, x, allRoles))

        // control flow, start contexts
        c.getContextPaths().foreach(x => {
          var name = ""
          if (x.contains("."))
            name = x.substring(x.lastIndexOf(".") + 1).toLowerCase
          else
            name = x.toLowerCase
          s += "val " + name + "= new " + x + "()"
          s += name + ".start"
        })
        s
      }
      case _ => throw new IllegalArgumentException("Unknown CPSProgram type!")
    }
  }
}
