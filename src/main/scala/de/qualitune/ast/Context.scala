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

package de.qualitune.ast

import role._
import rule.ActivationRule
import variable.{InitVariableDecl, EmptyVariableDecl, VariableDecl}

object Context {
  def build(name: String, contents: List[ScalaObject], activations: List[ActivationRule]): Context = {
    var inner = List[Context]()
    var variables = List[VariableDecl]()
    var roles = List[Role]()
    var constraints = List[RoleConstraint]()

    contents.foreach(_ match {
      case e: Context => inner = e.asInstanceOf[Context] :: inner
      case e: Role => roles = e.asInstanceOf[Role] :: roles
      case e: EmptyVariableDecl => variables = e.asInstanceOf[EmptyVariableDecl] :: variables
      case e: InitVariableDecl => variables = e.asInstanceOf[InitVariableDecl] :: variables
      case e: EquivalenceConstraint => constraints = e.asInstanceOf[EquivalenceConstraint] :: constraints
      case e: ImplicationConstraint => constraints = e.asInstanceOf[ImplicationConstraint] :: constraints
      case e: ProhibitionConstraint => constraints = e.asInstanceOf[ProhibitionConstraint] :: constraints
      case e => throw new Exception("Unexpected type: " + e.getClass)
    })

    Context(name, inner, variables, activations, roles, constraints)
  }
}

case class Context(name: String,
                   inner: List[Context],
                   variables: List[VariableDecl],
                   activations: List[ActivationRule],
                   roles: List[Role],
                   constraints: List[RoleConstraint]) {

  def prettyPrint(identLevel: Int): String = {
    var ident = ""
    (1 to identLevel).foreach(e => {
      ident += "\t"
    })

    var v = ""
    var a = activations.map(_.prettyPrint(identLevel)).mkString(ident + "\t", "\n\n" + ident + "\t", "\n")
    if (!(constraints.isEmpty && roles.isEmpty && inner.isEmpty)) a += "\n"

    var c = ""
    var r = ""
    var i = ""

    if (!variables.isEmpty) v = variables.map(_.toString).mkString("\t" + ident, "\n\t" + ident, "\n\n")

    if (!constraints.isEmpty) c = constraints.map(_.toString).mkString("\t" + ident, "\n\t" + ident, "\n")
    if (!constraints.isEmpty && !(roles.isEmpty && inner.isEmpty)) c += "\n"

    if (!roles.isEmpty) r = roles.map(_.prettyPrint(identLevel)).mkString("\t" + ident, "\n\n\t" + ident, "\n")
    if (!roles.isEmpty && !inner.isEmpty) r += "\n"

    if (!inner.isEmpty) i = inner.map(_.prettyPrint(identLevel + 1)).mkString("\t" + ident, "\n\t" + ident, "\n")

    "context " + name + " {\n" + v + a + c + r + i + ident + "}"
  }

  override def toString = {
    prettyPrint(0)
  }
}