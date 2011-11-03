package ast.role

case class ProhibitionConstraint(source: Role, target: Role) extends RoleConstraint(source, target) {

}