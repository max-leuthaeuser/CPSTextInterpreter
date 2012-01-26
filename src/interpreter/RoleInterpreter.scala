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

import ast.role.{Role, EquivalenceConstraint, ImplicationConstraint, ProhibitionConstraint}

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class RoleInterpreter extends ASTElementInterpreter {
  private def buildActMethod(name: String) = {
    "def act() { loop { react { case " + name + " => behavior() } } }\n"
  }

  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case r: Role => {
        s + ("case object token_" + r.name + "\n")

        s + ("var " + r.name + " = new role_" + r.name + " {}\n")

        s + "trait role_" + r.name + " extends Role[" + r.playedBy + "] with Actor {\n"
        // act method to start the behaviour method when the context the role belongs to gets activated
        // TODO pass the token if context and the corresponding activation record gets activated
        s + buildActMethod("token_" + r.name)

        // variables:
        r.variables.foreach(new VariableInterpreter()(s, _))
        // behavior:
        new CallableInterpreter()(s, r.behavior)
        // methods:
        r.operations.foreach(new CallableInterpreter()(s, _))
        s + "\n}\n"
      }
      case ec: EquivalenceConstraint => s // TODO handle EquivalenceConstraint interpretation
      case ic: ImplicationConstraint => s // TODO handle ImplicationConstraint interpretation
      case pc: ProhibitionConstraint => s // TODO handle ProhibitionConstraint interpretation
      case _ => throw new IllegalArgumentException("Unknown Role or RoleContraint type!")
    }
  }
}