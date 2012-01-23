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

import ast.cps.{CPS, CPSType}
import scalaroles.players.NaoRobot._

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class CPSTypeInterpreter extends ASTElementInterpreter {
  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case c: CPS => {
        c.cpsType match {
          case CPSType.Nao => s + ("val nao_" + c.name + "=\"" + c.name + "\" IP \"" + c.ip + "\" PORT " + c.port + "\n")
          case CPSType.Mindstorm => throw new IllegalArgumentException("Interpretation for CPSType Mindstorm is not implemented yet.")
        }
      }
      case _ => throw new IllegalArgumentException("Unknown CPS type!")
    }
  }
}