package ast.role

case class EquivalenceConstraint(source: Role, target: Role) extends RoleConstraint(source, target) {

}