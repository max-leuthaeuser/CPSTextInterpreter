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

import de.qualitune.ast.cps.{CPS, CPSType}
import de.qualitune.ast.ASTElement

/**
 * @author Max Leuthaeuser
 * @since 18.01.12
 */
class CPSTypeInterpreter extends ASTElementInterpreter {
  override def apply[E <: ASTElement, T <: AnyRef](s: EvaluableString, elem: E, data: T) = {
    elem match {
      case c: CPS => {
        c.cpsType match {
          case CPSType.Nao => {
            s + ("@volatile val " + c.name + " = NaoRobot.createWithCore(\"" + c.name + ", \"" + c.ip + "\", " + c.port + ")\n")
            s += ("Registry.cores = " + c.name + " :: Registry.cores")
          }
          case CPSType.Mindstorm => throw new IllegalArgumentException("Interpretation for CPSType Mindstorm is not implemented yet.")
        }
      }
      case _ => throw new IllegalArgumentException("Unknown CPS type!")
    }
  }
}