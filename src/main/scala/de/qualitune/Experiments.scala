package de.qualitune

import roles.{ComponentRole, Component, ComponentCore}
import util.ListUtils

/**
 * @author Max Leuthaeuser
 * @since 22.11.2011
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

  def test1(l: List[List[ComponentCore]]) {
    /**
     * activate for {
     * RoleA a; RoleB b; RoleC c;
     * } when ( a.a() && b.b() && c.c() )
     */
    for (c <- l) {
      if (c(0).hasRole("RoleA") && c(1).hasRole("RoleB") && c(2).hasRole("RoleC")) {
        for (a <- c(0).getRole("RoleA"); b <- c(1).getRole("RoleB"); c <- c(2).getRole("RoleC")) {
          println(a.asInstanceOf[RoleA].a() && b.asInstanceOf[RoleB].b() && c.asInstanceOf[RoleC].c())
        }
      }
    }
  }

  def test2(l: List[List[ComponentCore]]) {
    /**
     * activate for {
     * RoleA a;
     * } when ( a.a() )
     */
    for (c <- l) {
      if (c(0).hasRole("RoleA")) {
        for (a <- c(0).getRole("RoleA")) {
          println(a.asInstanceOf[RoleA].a())
        }
      }
    }
  }

  def main(args: Array[String]) {
    val c1 = new RobotCore()

    c1.addRole(new RoleA("a1 (core c1)", c1))
    c1.addRole(new RoleA("a2 (core c1)", c1))

    c1.addRole(new RoleB("b1 (core c1)", c1))
    c1.addRole(new RoleB("b2 (core c1)", c1))

    val c2 = new RobotCore()
    c2.addRole(new RoleC("c1 (core c2)", c2))
    c2.addRole(new RoleC("c2 (core c2)", c2))

    val l1 = List(c1, c2).filter(e => e.hasRole("RoleA") || e.hasRole("RoleB") || e.hasRole("RoleC"))
    test1(ListUtils.combinations(3, l1))
    val l2 = List(c1, c2).filter(e => e.hasRole("RoleA"))
    test2(ListUtils.combinations(1, l2))
  }
}

