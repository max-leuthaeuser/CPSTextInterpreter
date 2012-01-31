package scalaroles.examples.pingpongCollaboration

import scalaroles.players.NaoRobot._
import scalaroles.roles.Basics._

object PingPongCollaborationTestApp {
  def main(args: Array[String]) {
    val pingpong = new PingPong {}

    var chuck = "Chuck" IP "192.15.2.2"

    val x = ((chuck as pingpong.sound) -: pingpong.ping)
    x.play

    val y = (x -: pingpong.stop)
    y.play

  }
}