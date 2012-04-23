package de.qualitune.roles

import de.qualitune.roles.players.NaoRobot

/**
 * User: Max
 */
abstract class Component extends NaoRobot {
  abstract def addRole(spec: ComponentRole)

  abstract def getRole(spec: String): ComponentRole

  abstract def hasRole(spec: String): Boolean
}
