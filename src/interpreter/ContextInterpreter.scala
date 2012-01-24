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

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class ContextInterpreter extends ASTElementInterpreter {
  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case c: Context => {
        /**
         * TODO Context activation
         * All contexts have to be stored somewhere and should be
         * activated if the corresponding activation record is triggered.
         */

        s + ("trait " + c.name + " extends TransientCollaboration {\n")

        // activation records
        c.activations.foreach(new ActivationRuleInterpreter()(s, _))

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