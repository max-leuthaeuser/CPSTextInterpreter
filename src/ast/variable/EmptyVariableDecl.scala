package ast.variable

import VariableDeclAccessType._

case class EmptyVariableDecl(accessType: VariableDeclAccessType, name: String) extends VariableDecl(accessType, name) {

}