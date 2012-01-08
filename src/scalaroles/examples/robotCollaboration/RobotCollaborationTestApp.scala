package scalaroles.examples.robotCollaboration

import scalaroles.roles.Basics._
import scalaroles.players.NaoRobot._

object RobotCollaborationTestApp {
  def main(args: Array[String]) {

    val game = new Football {}
    val chuck = "Chuck" IP "192.168.0.1"
    val hans = "Hans" IP "192.169.0.2"

    // chuck as goalkeeper
    (chuck as game.goalkeeper).catchBallWithHands
    // now the same as fieldplayer
    (chuck as game.fieldplayer).catchBallWithHands


    val pass = game.pass
    (chuck as pass.sender).playAPass
    (chuck as pass.receiver).playAPass
  }
}