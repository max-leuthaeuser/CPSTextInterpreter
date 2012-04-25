package de.qualitune

import roles.{ComponentRole, Component, ComponentCore}

/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */

object Experiments {

  class RobotCore extends ComponentCore

  class RoleA(n: String, core: Component) extends ComponentRole(core) {
    def a() = {
      println("A.a from " + n)
      true
    }
  }

  class RoleB(n: String, core: Component) extends ComponentRole(core) {
    def b() = {
      println("B.b from " + n)
      true
    }
  }

  class RoleC(n: String, core: Component) extends ComponentRole(core) {
    def c() = {
      println("C.c from " + n)
      true
    }
  }

  def test1(l: List[ComponentCore]) {
    /**
     * activate for {
     * RoleA a; RoleB b; RoleC c;
     * } when ( a.a() && b.b() && c.c() )
     */
    for (c <- l) {
      if (c.hasRole("RoleA") && c.hasRole("RoleB") && c.hasRole("RoleC")) {
        for (a <- c.getRole("RoleA"); b <- c.getRole("RoleB"); c <- c.getRole("RoleC")) {
          println(a.asInstanceOf[RoleA].a() && b.asInstanceOf[RoleB].b() && c.asInstanceOf[RoleC].c())
        }
      }
    }
  }

  def main(args: Array[String]) {
    val c = new RobotCore()

    c.addRole(new RoleA("a1", c))
    c.addRole(new RoleA("a2", c))

    c.addRole(new RoleB("b1", c))
    c.addRole(new RoleB("b2", c))

    c.addRole(new RoleC("c1", c))
    c.addRole(new RoleC("c2", c))

    test1(List(c))
  }
}

