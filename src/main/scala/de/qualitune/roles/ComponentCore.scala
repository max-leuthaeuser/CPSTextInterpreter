package de.qualitune.roles

import collection.mutable.{Map, LinkedList}

/**
 * @author Max Leuthaeuser
 * @since 25.04.12
 */
case class ComponentCore() extends Component {
  private val roles = Map[String, LinkedList[ComponentRole]]()

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

