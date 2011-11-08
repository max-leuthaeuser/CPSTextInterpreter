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

package ast

import role._
import rule.ActivationRule
import variable.{InitVariableDecl, EmptyVariableDecl, VariableDecl}

object Context {
  def build(name: String, contents: List[List[AnyRef]], activation: ActivationRule): Context = {
    var inner: List[Context] = List[Context]()
    var variables: List[VariableDecl] = List[VariableDecl]()
    var roles: List[Role] = List[Role]()
    var constraints: List[RoleConstraint] = List[RoleConstraint]()

    // TODO: this might could be removed - needs to be tested
    if (contents == null)
      return Context(name, inner, variables, activation, roles, constraints)

    contents.foreach(l => {
      l.foreach(e => e match {
        case e: Context => inner ::= e.asInstanceOf[Context]
        case e: EmptyVariableDecl => variables ::= e.asInstanceOf[EmptyVariableDecl]
        case e: InitVariableDecl => variables ::= e.asInstanceOf[InitVariableDecl]
        case e: Role => roles ::= e.asInstanceOf[Role]
        case e: EquivalenceConstraint => constraints ::= e.asInstanceOf[EquivalenceConstraint]
        case e: ImplicationConstraint => constraints ::= e.asInstanceOf[ImplicationConstraint]
        case e: ProhibitionConstraint => constraints ::= e.asInstanceOf[ProhibitionConstraint]
        case other => throw new Exception("Expression '" + other + "' is not allowed inside Context!")
      })
    })

    Context(name, inner, variables, activation, roles, constraints)
  }
}

case class Context(name: String,
                   inner: List[Context],
                   variables: List[VariableDecl],
                   activation: ActivationRule,
                   roles: List[Role],
                   constraints: List[RoleConstraint]) {
}