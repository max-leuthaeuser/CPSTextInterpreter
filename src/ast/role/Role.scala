package ast.role

import ast.callable.{Operation, Behavior}
import ast.variable.VariableDecl

case class Role(operations: List[Operation], behavior: Behavior, variables: List[VariableDecl]) {

}