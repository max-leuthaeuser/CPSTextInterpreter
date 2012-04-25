package de.qualitune.roles

/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */
class ComponentCore extends Component {
  private var roles = scala.collection.mutable.Map[String, ComponentRole]()

  override def addRole(spec: ComponentRole) {
    roles(spec.getName) = spec
  }

  override def getRole(spec: String): ComponentRole = {
    roles.get(spec).get
  }

  override def hasRole(spec: String): Boolean = {
    roles.contains(spec)
  }

  def getRoles() = {
    roles.values
  }
}

