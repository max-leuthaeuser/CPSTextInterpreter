package de.qualitune.roles

/**
 * User: Max
 */
class ComponentRole(private val core: ComponentCore) extends Component {
  def getName = this.getClass.getSimpleName

  def addRole(spec: ComponentRole) {
    core.addRole(spec)
  }

  def getRole(spec: String) {
    core.getRole(spec)
  }

  def hasRole(spec: String) {
    core.hasRole(spec)
  }
}
