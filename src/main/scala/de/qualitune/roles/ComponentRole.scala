package de.qualitune.roles

/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */
class ComponentRole(private val core: Component) extends Component {
  def getName = this.getClass.getSimpleName

  override def addRole(spec: ComponentRole) {
    core.addRole(spec)
  }

  override def getRole(spec: String): ComponentRole = {
    core.getRole(spec)
  }

  override def hasRole(spec: String): Boolean = {
    core.hasRole(spec)
  }
}
