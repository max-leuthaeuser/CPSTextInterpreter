package de.qualitune

import roles.{ComponentRole, Component, ComponentCore}

/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */

object Experiments {

  class RobotCore extends ComponentCore

  case class RoleA(core: Component) extends ComponentRole(core) {
    def a() = {
      println("A.a")
      true
    }
  }

  case class RoleB(core: Component) extends ComponentRole(core) {
    def a() = {
      println("B.a")
      true
    }
  }

  def test1(l: List[ComponentCore]) {
    /**
     * activate for {
     * RoleA a; RoleB b;
     * } when ( a.a() || b.a() )
     */
    for (c <- l) {
      if (c.hasRole("RoleA") && c.hasRole("RoleB")) {
        val a = c.getRole("RoleA").asInstanceOf[RoleA]
        val b = c.getRole("RoleB").asInstanceOf[RoleB]
        println(a.a() || b.a())
      }
    }
  }

  def test2(l: List[ComponentCore]) {
    /**
     * activate for {
     * RoleA a; RoleB b;
     * } when ( a.a() && b.a() )
     */
    for (c <- l) {
      if (c.hasRole("RoleA") && c.hasRole("RoleB")) {
        val a = c.getRole("RoleA").asInstanceOf[RoleA]
        val b = c.getRole("RoleB").asInstanceOf[RoleB]
        println(a.a() && b.a())
      }
    }
  }

  def main(args: Array[String]) {
    val c = new RobotCore()
    val a = new RoleA(c)
    c.addRole(a)
    val b = new RoleB(c)
    c.addRole(b)

    test1(List(c))
    test2(List(c, c))
  }
}

