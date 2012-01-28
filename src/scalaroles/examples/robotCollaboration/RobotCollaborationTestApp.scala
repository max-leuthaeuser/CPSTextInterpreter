package scalaroles.examples.robotCollaboration

import scalaroles.players.NaoRobot._

object RobotCollaborationTestApp {
  def main(args: Array[String]) {

    val game = new Football {}
    val chuck = "Chuck" IP "192.168.0.1"
    val hans = "Hans" IP "192.169.0.2"

    // chuck as goalkeeper
    (chuck -: game.goalkeeper).catchBallWithHands
    // now the same as fieldplayer
    (chuck -: game.fieldplayer).catchBallWithHands


    val pass = game.pass
    (chuck -: pass.sender).playAPass
    (chuck -: pass.receiver).playAPass

    ((chuck -: game.fieldplayer) -: game.goalkeeper).catchBallWithHands
  }
}