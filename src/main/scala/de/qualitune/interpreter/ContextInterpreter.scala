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

import de.qualitune.ast.rule.ActivationRule
import de.qualitune.ast.{ASTElement, Context}
import de.qualitune.ast.role.Role

/**
 * @author Max Leuthaeuser
 * @since 22.11.2011
 */
// TODO check constraints
class ContextInterpreter extends ASTElementInterpreter {
  private def buildStartMethod(a: List[ActivationRule]) = {
    "def start {" + a.map("context_activator_" + _.name + ".start").mkString("\n") + "}\n"
  }

  override def apply[E <: ASTElement, T <: AnyRef](s: EvaluableString, elem: E, data: T) = {
    elem match {
      case c: Context => data match {
        case d: List[Role] => {
          s + ("class Context_" + c.name + " {\n")
          c.activations.foreach(a => {
            new ActivationRuleInterpreter()(s, a, null)
          })
          s + buildStartMethod(c.activations)

          // constraints
          c.constraints.foreach(new RoleInterpreter()(s, _, null))

          // variables
          c.variables.foreach(new VariableInterpreter()(s, _, null))

          // roles
          c.roles.foreach(x => new RoleInterpreter()(s, x, d))

          // inner contexts
          c.inner.foreach(x => new ContextInterpreter()(s, x, d))

          s + "\n}\n"
        }
      }
      case _ => throw new IllegalArgumentException("Unknown Context type!")
    }
  }
}