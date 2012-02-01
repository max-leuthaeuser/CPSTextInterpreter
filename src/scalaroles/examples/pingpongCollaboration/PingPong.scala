package scalaroles.examples.pingpongCollaboration

import scalaroles.players.NaoRobot
import scalaroles.roles.TransientCollaboration

trait PingPong extends TransientCollaboration {
  val sound = new Sound {}
  val ping = new Ping {}
  val pong = new Pong {}
  val stop = new Stop {}


  trait Ping extends Role[Sound] {
    val sound = "ping"

    def play {
      println(sound)
    }
  }

  trait Pong extends Role[Sound] {
    val sound = "pong"

    def play {
      println(sound)
    }
  }

  trait Sound extends Role[NaoRobot]

  trait Stop extends Role[NaoRobot] {
    def play {
      println("now it stops!")
    }
  }

}