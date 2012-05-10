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

package de.qualitune.transformator

import de.qualitune.ast.cps.{CPS, CPSType}
import de.qualitune.ast.ASTElement

/**
 * @author Max Leuthaeuser
 * @since 18.01.12
 */
class CPSTypeTransformator extends ASTElementTransformator {
  override def apply[E <: ASTElement, T <: AnyRef](s: ExecutableString, elem: E, data: T) = {
    elem match {
      case c: CPS => {
        c.cpsType match {
          case CPSType.Nao => {
            s + ("val compound_" + c.name + " = Nao.createWithCore(\"" + c.name + "\", \"" + c.ip + "\", " + c.port + ")\n")
            s + ("@volatile var " + c.name + " = compound_" + c.name + "._2\n")
            s += ("Registry.cores = compound_" + c.name + "._1 :: Registry.cores")
          }
          case CPSType.Mindstorm => throw new IllegalArgumentException("Interpretation for CPSType Mindstorm is not implemented yet.")
        }
      }
      case _ => throw new IllegalArgumentException("Unknown CPS type!")
    }
  }
}