package scalaroles.examples.pingpongCollaboration

import scalaroles.players.NaoRobot._
import scalaroles.roles.Basics._

object PingPongCollaborationTestApp {
  def main(args: Array[String]) {
    val pingpong = new PingPong {}

    var chuck = "Chuck" IP "192.15.2.2"

    var change = 0

    def c1 = {
      if (change != 1) throw new Exception("Not allowed! Activation was not done yet!")
      ((chuck as pingpong.sound) -: pingpong.ping)
    }

    change = 1
    c1.play

    //val c2 = ((chuck as pingpong.sound) -: pingpong.pong)
    //c2.play

    //val c3 = (chuck -: pingpong.stop)
    //c3.play


  }
}