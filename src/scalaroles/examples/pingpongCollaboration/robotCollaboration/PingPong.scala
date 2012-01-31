package scalaroles.examples.pingpongCollaboration.robotCollaboration

import scalaroles.players.NaoRobot
import scalaroles.roles.TransientCollaboration

trait PingPong extends TransientCollaboration {
  val sound = new Sound {}
  val ping = new Ping {}
  val pong = new Pong {}
  val stop = new Stop {}

  trait Sound extends Role[NaoRobot]

  trait Ping extends Role[Sound] {
    val sound = "ping"

    def play {
      println(sound)
    }
  }

  trait Pong extends Role[Sound] {
    val sound = "ping"

    def play {
      println(sound)
    }
  }

  trait Stop extends Role[NaoRobot] {
    def play {
      println("now it stops!")
    }
  }

}