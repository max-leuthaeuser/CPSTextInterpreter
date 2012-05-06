package de.qualitune.roles

/**
 * @author Max Leuthaeuser
 * @since 25.04.12
 */
abstract class Component {
  def addRole(spec: ComponentRole)

  def getRole(spec: String): Iterable[ComponentRole]

  def hasRole(spec: String): Boolean

  def as(role: ComponentRole) = {
    role.setCore(this)
    role
  }
}
