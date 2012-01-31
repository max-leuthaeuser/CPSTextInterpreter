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
import ast.rule.{ActivationRuleVariable, ActivationRule}

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class ContextInterpreter extends ASTElementInterpreter {
  private var initializedVariables = List[ActivationRuleVariable]()

  // TODO Nao should be NaoRobot everywhere
  // TODO handle roles that play roles
  // TODO check constraints
  private def buildDoActivationMethod(a: ActivationRule, innerRoles: List[Role], allRoles: List[Role]) = {
    val globals = new StringBuilder()
    val vars = new StringBuilder()
    a.activateFor.foreach(x => {
      if (!x.roleName.equals("Nao") && !initializedVariables.contains(x)) {
        globals.append("var " + x.variableName + ": " + x.roleName + " = null\n")
        vars.append(x.variableName + "= new " + x.roleName + "() :- "
          + a.getBindingForVariable(x).roleName + "\n")
        initializedVariables = x :: initializedVariables
      } else {
        vars.append(x.variableName + "=" + x.variableName + " :- "
          + a.getBindingForVariable(x).roleName + "\n")
      }
    })

    globals.toString() + "def do_activate_" + a.name + "() {\n" +
      vars.toString() + "\n" +
      innerRoles.map(x => x.name + " ! " + "token_" + x.name).mkString("\n") + "}\n"
  }

  private def buildStartMethod(a: List[ActivationRule]) = {
    "def start {" + a.map("context_activator_" + _.name + ".start").mkString("\n") + "}\n"
  }

  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case c: (Context, List[Role]) => {
        s + ("trait Context_" + c._1.name + " extends TransientCollaboration {\n")
        // activation records
        c._1.activations.foreach(a => {
          new ActivationRuleInterpreter()(s, (a, c._1.name))
          s + buildDoActivationMethod(a, c._1.roles, c._2)
        })
        s + buildStartMethod(c._1.activations)

        // constraints
        c._1.constraints.foreach(new RoleInterpreter()(s, _))

        // variables
        c._1.variables.foreach(new VariableInterpreter()(s, _))

        // roles
        c._1.roles.foreach(new RoleInterpreter()(s, _))

        // inner contexts
        c._1.inner.foreach(new ContextInterpreter()(s, _))

        s + "\n}\n"
      }
      case _ => throw new IllegalArgumentException("Unknown Context type!")
    }
  }
}