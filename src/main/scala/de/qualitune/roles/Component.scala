package de.qualitune.roles

import players.NaoRobot

/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */
abstract class Component extends NaoRobot {
  def addRole(spec: ComponentRole)

  def getRole(spec: String): ComponentRole

  def hasRole(spec: String): Boolean
}
