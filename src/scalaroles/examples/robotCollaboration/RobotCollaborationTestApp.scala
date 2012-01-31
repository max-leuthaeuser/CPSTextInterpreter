package scalaroles.examples.robotCollaboration

import scalaroles.players.NaoRobot._
import scalaroles.roles.Basics._

object RobotCollaborationTestApp {
  def main(args: Array[String]) {
    val game = new Football {}
    val chuck = "Chuck" IP "192.168.0.1"
    var hans = "Hans" IP "192.169.0.2"

    val pass = game.pass

    // chuck as goalkeeper
    (((chuck as game.player) -: game.footballplayer) -: game.goalkeeper).catchBallWithHands
    // now the same as fieldplayer
    (((chuck as game.player) -: game.footballplayer) -: game.fieldplayer).catchBallWithHands



    (chuck -: pass.sender).playAPass
    (chuck -: pass.receiver).playAPass

    ((((chuck as game.player) -: game.footballplayer) -: game.goalkeeper) -: game.fieldplayer).catchBallWithHands

  }
}