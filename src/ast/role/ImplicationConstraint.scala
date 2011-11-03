package ast.role

case class ImplicationConstraint(source: Role, target: Role) extends RoleConstraint(source, target) {

}