package de.qualitune.roles

/**
 * User: Max
 */
class ComponentCore extends Component {
  private var roles = Map[String, ComponentRole]()

  def addRole(spec: ComponentRole) {
    roles(spec.getName) = spec
  }

  def getRole(spec: String): ComponentRole = {
    roles.get(spec).getOrElse(null)
  }

  def hasRole(spec: String): Boolean = {
    roles.contains(spec)
  }
}
