package de.qualitune.roles

object ComponentRole {
  def apply() = new ComponentRole(null)
}

/**
 * @author Max Leuthaeuser
 * @since 25.04.12
 */
case class ComponentRole(private var core: Component, val singleton: Boolean = false) extends Component {
  def getName = this.getClass.getSimpleName

  def setCore(core: Component) {
    this.core = core
  }

  override def addRole(spec: ComponentRole) {
    core.addRole(spec)
  }

  override def getRole(spec: String): Iterable[ComponentRole] = {
    core.getRole(spec)
  }

  override def hasRole(spec: String): Boolean = {
    core.hasRole(spec)
  }
}
