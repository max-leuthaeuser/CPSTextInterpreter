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

/**
 * User: Max Leuthaeuser
 * Date: 19.01.12
 */
class CPSProgramInterpreter extends ASTElementInterpreter {
  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case c: CPSProgram => {
        // imports
        s ++ c.imports.map(_ + "\n")

        // cps
        c.robots.map(new CPSTypeInterpreter()(s, _))

        // contexts
        c.contexts.map(new ContextInterpreter()(s, _))

        // TODO handle control flow

        s
      } // TODO handle CPSProgram interpretation
      case _ => throw new IllegalArgumentException("Unknown CPSProgram type!")
    }
  }
}