package ast.rule

case class ActivationRule(activateFor: List[ActivationRuleVariable], when: String, bindings: List[ActivationRuleBinding]) {

}