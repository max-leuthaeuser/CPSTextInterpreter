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
    "def act() { while (true) { receive { case " + name + " => behavior() } } }\n"
  }

  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case r: (Role, List[Role]) => {
        s + ("case object token_" + r._1.name + "\n")

        s + ("var " + r._1.name + " = new Role_" + r._1.name + " {}\n")

        if (r._2.map(_.name).contains(r._1.playedBy))
          s + "trait Role_" + r._1.name + " extends Role[Role_" + r._1.playedBy + "] with Actor {\n"
        else
          s + "trait Role_" + r._1.name + " extends Role[" + r._1.playedBy + "] with Actor {\n"
        // act method to start the behaviour method when the context the role belongs to gets activated
        s + buildActMethod("token_" + r._1.name)

        // variables:
        r._1.variables.foreach(new VariableInterpreter()(s, _))
        // behavior:
        new CallableInterpreter()(s, r._1.behavior)
        // methods:
        r._1.operations.foreach(new CallableInterpreter()(s, _))
        s + "\n}\n"
      }
      case ec: EquivalenceConstraint => s // TODO handle EquivalenceConstraint interpretation
      case ic: ImplicationConstraint => s // TODO handle ImplicationConstraint interpretation
      case pc: ProhibitionConstraint => s // TODO handle ProhibitionConstraint interpretation
      case _ => throw new IllegalArgumentException("Unknown Role or RoleContraint type!")
    }
  }
}