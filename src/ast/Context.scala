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

import role.{RoleConstraint, Role}
import rule.ActivationRule
import variable.VariableDecl

case class Context(name: String,
                   inner: List[Context],
                   variables: List[VariableDecl],
                   activation: ActivationRule,
                   roles: List[Role],
                   constraints: List[RoleConstraint]) {
  def this(name: String, inner: List[Context]) = this (name, inner, List[VariableDecl](), null, List[Role](), List[RoleConstraint]())
}