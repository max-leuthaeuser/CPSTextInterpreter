package ast

import role.{RoleConstraint, Role}
import rule.ActivationRule
import variable.VariableDecl

case class Context(name: String, inner: List[Context], variables: List[VariableDecl],
                   activation: ActivationRule, roles: List[Role],
                   constraints: List[RoleConstraint]) {
  // TODO use BuildPattern!
}