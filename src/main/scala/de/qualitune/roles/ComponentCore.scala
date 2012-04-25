package de.qualitune.roles

import collection.mutable.{Map, LinkedList}


/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */
class ComponentCore extends Component {
  private var roles = Map[String, LinkedList[ComponentRole]]()

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

  def getRoles() = {
    roles.values.flatten
  }
}

