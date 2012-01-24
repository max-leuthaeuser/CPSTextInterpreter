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

import ast.variable.{EmptyVariableDecl, InitVariableDecl}
import ast.variable.VariableDeclAccessType
import ast.variable.VariableDeclAccessType._


/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class VariableInterpreter extends ASTElementInterpreter {
  private def getAccessTypeModifier(t: VariableDeclAccessType): String = {
    t match {
      // we using the volatile annotation by default here because
      // concurrent access from several contexts might be possible
      case VariableDeclAccessType.modifiable => "@volatile var "
      case VariableDeclAccessType.unmodifiable => "@volatile val "
      case _ => throw new IllegalArgumentException("Unknown VariableDeclAccessType!")
    }
  }

  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case ev: EmptyVariableDecl => s + (getAccessTypeModifier(ev.accessType) + ev.name + ":" + ev.typ + "=_\n")
      case iv: InitVariableDecl => s + (getAccessTypeModifier(iv.accessType) + iv.name + ":" + iv.typ + "=" + iv.value + "\n")
      case _ => throw new IllegalArgumentException("Unknown Variable type!")
    }
  }
}