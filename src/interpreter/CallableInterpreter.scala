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

import ast.callable.{Callable, Behavior, Operation}


/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class CallableInterpreter extends ASTElementInterpreter {
  private def callableToString(c: Callable) = "def " + c.toString.replaceAll("\t", "")

  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case b: Behavior => s + callableToString(b)
      case o: Operation => s + callableToString(o)
      case _ => throw new IllegalArgumentException("Unknown Callable type!")
    }
  }
}