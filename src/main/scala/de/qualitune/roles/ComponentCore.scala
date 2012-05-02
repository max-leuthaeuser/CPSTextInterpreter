package de.qualitune.roles

import collection.mutable.{Map, LinkedList}
import players.NaoRobot

/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */
class ComponentCore extends Component {
  private val roles = Map[String, LinkedList[ComponentRole]]()

  /**Copyconstructor */
  def this(r: NaoRobot) {
    this()
    this.name = r.name
    this.ip = r.ip
    this.port = r.port
  }

  override def addRole(spec: ComponentRole) {
    if (roles.contains(spec.getName)) {
      roles(spec.getName) = roles.get(spec.getName).get :+ spec
    } else {
      roles(spec.getName) = LinkedList(spec)
    }
  }

  override def getRole(spec: String): Iterable[ComponentRole] = {
    roles.get(spec).get
  }

  override def hasRole(spec: String): Boolean = {
    roles.contains(spec)
  }

  @deprecated
  def getRoles = roles.values.flatten
}

