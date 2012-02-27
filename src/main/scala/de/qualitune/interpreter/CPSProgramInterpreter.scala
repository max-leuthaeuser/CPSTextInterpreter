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
 * User: Max Leuthaeuser
 * Date: 19.01.12
 */
class CPSProgramInterpreter extends ASTElementInterpreter {
  override def apply[E <: ASTElement, T <: AnyRef](s: EvaluableString, elem: E, data: T) = {
    elem match {
      case c: CPSProgram => {
        // imports
        // some standard imports first, they are always needed
        s + """import scala.actors.Actor
			import de.qualitune.scalaroles.roles.TransientCollaboration
			import de.qualitune.scalaroles.roles.Basics._
			import de.qualitune.scalaroles.players.NaoRobot
			import de.qualitune.scalaroles.players.NaoRobot._
			"""
			
        s ++ c.imports.map("import " + _ + "\n") + "\n"

        // cps
        s + "object CPS {"
        c.robots.foreach(new CPSTypeInterpreter()(s, _, null))
        s + "}\n import CPS._\n"

        // contexts
        val allRoles = c.getAllRoles()
        c.contexts.foreach(x => new ContextInterpreter()(s, x, allRoles))

        // control flow, start contexts and roles
        c.getContextPaths().foreach(x => {
          var name = ""
          if (x.contains("."))
            name = x.substring(x.lastIndexOf(".") + 1).toLowerCase
          else
            name = x.toLowerCase
          s += "val " + name + "= new " + x + " {}"
          s += name + ".start"
        })
        c.getRolePaths().foreach(x => {
          s += x + ".start"
        })
        s
      }
      case _ => throw new IllegalArgumentException("Unknown CPSProgram type!")
    }
  }
}
