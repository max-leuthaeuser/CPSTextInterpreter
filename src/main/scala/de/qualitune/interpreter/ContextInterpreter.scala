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

import de.qualitune.ast.role.Role
import de.qualitune.ast.rule.ActivationRule
import de.qualitune.ast.{ASTElement, Context}

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class ContextInterpreter extends ASTElementInterpreter {
  // TODO test with deeper nested role play role constructs
  /**
   * Recursively calculates a List of all roles role 'r' plays.
   */
  private def getRolePlaysRoleList(r: Role, roles: List[Role]): List[Role] = {
    if (!roles.contains(r)) {
      List[Role](r)
    } else {
      if (!r.playedBy.equals("NaoRobot")) {
        val p = roles.filter(ro => r.playedBy.equals(ro.name))(0)
        r :: getRolePlaysRoleList(p, roles)
      } else {
        List[Role](r)
      }
    }
  }

  /**
   * Processes the list of all roles which role 'role' plays.
   * Type conversion done with keyword 'as' first, playedBy is then translated to '-:'.
   * Makes sure that all parentheses are set correctly.
   */
  // TODO refactor this for ROP
  private def getRolePlaysRoleDependencies(role: String, roles: List[Role]): String = {
    val allRoles = roles.map(_.name)
    if (allRoles.contains(roles.filter(ro => role.equals(ro.name))(0).playedBy)) {
      var list = getRolePlaysRoleList(roles.filter(ro => role.equals(ro.name))(0), roles)
      val head = list.head.name
      list = list.reverse
      val tail = list.slice(0, list.size - 1)
      if (list.size == 1) {
        " -: " + head
      } else {
        var result = "<<name>> as " + tail.head.name
        list.tail.foreach(x => {
          result = "(" + result + ") -: " + x.name
        })
        return result
      }
    }
    return "<<name>> -: " + role
  }

  // TODO check constraints
  private def buildDoActivationMethod(index: Int, a: ActivationRule, allRoles: List[Role]) = {
    val vars = new StringBuilder()
    a.activateFor.foreach(x => {
      vars.append("def " + x.variableName + " = {\n")
      vars.append("if (change < " + index + ") throw new Exception(\"Using " + x.variableName + " is not allowed yet! Activation was not done yet!\")\n")
      // TODO look up all roles for the core object and generate playedBy deps live
      vars.append(getRolePlaysRoleDependencies(a.getBindingForVariable(x).roleName, allRoles).replace("<<name>>", x.roleName) + "}\n")
    })

    vars.toString() + "\n" + "def do_activate_" + a.name + "() {\n" +
      "change = " + index + "\n" +
      // TODO create new instance of role, update role mapping (name of role + instance), activate role
      // TODO add role mapping registry
      // TODO remove single role instance everywhere (in main etc.)
      a.bindings.map(x => x.roleName + " ! " + "token_" + x.roleName).mkString("\n") + "}\n"
  }

  private def buildStartMethod(a: List[ActivationRule]) = {
    "def start {" + a.map("context_activator_" + _.name + ".start").mkString("\n") + "}\n"
  }


  override def apply[E <: ASTElement, T <: AnyRef](s: EvaluableString, elem: E, data: T) = {
    elem match {
      case c: Context => data match {
        case d: List[Role] => {
          s + ("trait Context_" + c.name + " extends TransientCollaboration {\n")
          // activation records
          s + ("var change = 0\n")
          var index = 0
          c.activations.foreach(a => {
            index += 1
            new ActivationRuleInterpreter()(s, a, null)
            s + buildDoActivationMethod(index, a, d)
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