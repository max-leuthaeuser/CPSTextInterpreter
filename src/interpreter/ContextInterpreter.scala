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

import ast.Context
import ast.role.Role
import ast.rule.ActivationRule

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class ContextInterpreter extends ASTElementInterpreter {
  private def buildDoActivationMethod(a: ActivationRule, r: List[Role]) = {
    "def do_activate_" + a.name + "() {" + r.map(x => x.name + " ! " + "token_" + x.name).mkString("\n") + "}\n" // TODO handle role bindings
  }

  private def buildStartMethod(a: List[ActivationRule]) = {
    "def start {" + a.map("context_activator_" + _.name + ".start").mkString("\n") + "}\n"
  }

  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case c: Context => {
        s + ("trait Context_" + c.name + " extends TransientCollaboration {\n")
        // activation records
        c.activations.foreach(a => {
          new ActivationRuleInterpreter()(s, (a, c.name))
          s + buildDoActivationMethod(a, c.roles)
        })
        s + buildStartMethod(c.activations)

        // constraints
        c.constraints.foreach(new RoleInterpreter()(s, _))

        // variables
        c.variables.foreach(new VariableInterpreter()(s, _))

        // roles
        c.roles.foreach(new RoleInterpreter()(s, _))

        // inner contexts
        c.inner.foreach(new ContextInterpreter()(s, _))

        s + "\n}\n"
      }
      case _ => throw new IllegalArgumentException("Unknown Context type!")
    }
  }
}