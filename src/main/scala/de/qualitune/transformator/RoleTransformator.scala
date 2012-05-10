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

import de.qualitune.ast.role.{Role, EquivalenceConstraint, ImplicationConstraint, ProhibitionConstraint}
import de.qualitune.ast.ASTElement

/**
 * @author Max Leuthaeuser
 * @since 18.01.12
 */
class RoleTransformator extends ASTElementTransformator {
  private def buildActMethod(name: String) = {
    "def act() { while (true) { receive { case " + name + " => behavior() } } }\n"
  }

  override def apply[E <: ASTElement, T <: AnyRef](s: ExecutableString, elem: E, data: T) = {
    elem match {
      case r: Role => data match {
        case d: List[Role] => {
          s + ("case object token_" + r.name + "\n")
          s + "class " + r.name + "(core: Component) extends ComponentRole(core) with Actor {\n"
          // act method to start the behaviour method when the context the role belongs to gets activated
          s + buildActMethod("token_" + r.name)

          // variables:
          r.variables.foreach(new VariableTransformator()(s, _, null))
          // behavior:
          new CallableTransformator()(s, r.behavior, null)
          // methods:
          r.operations.foreach(new CallableTransformator()(s, _, null))
          s + "\n}\n"
        }
      }
      case ec: EquivalenceConstraint => s // TODO handle EquivalenceConstraint interpretation
      case ic: ImplicationConstraint => s // TODO handle ImplicationConstraint interpretation
      case pc: ProhibitionConstraint => s // TODO handle ProhibitionConstraint interpretation
      case _ => throw new IllegalArgumentException("Unknown Role or RoleContraint type!")
    }
  }
}