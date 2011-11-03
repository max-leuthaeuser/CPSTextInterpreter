package ast.variable

import ast.variable.VariableDeclAccessType._

case class InitVariableDecl(value: String, accessType: VariableDeclAccessType, name: String)
  extends VariableDecl(accessType, name) {

}