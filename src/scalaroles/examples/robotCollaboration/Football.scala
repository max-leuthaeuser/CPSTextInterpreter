package scalaroles.examples.robotCollaboration

import scalaroles.players.NaoRobot
import scalaroles.roles.TransientCollaboration

trait Football extends TransientCollaboration {
  val goalkeeper = new Goalkeeper {}
  val fieldplayer = new FieldPlayer {}

  def pass: Pass = new Pass {}

  trait Pass extends TransientCollaboration {
    val sender = new Sender {}
    val receiver = new Receiver {}

    trait Sender extends Role[NaoRobot] {
      def playAPass = println("I am passing the ball now!")
    }

    trait Receiver extends Role[NaoRobot] {
      def playAPass = println("I cant pass, I am here to recieve the ball!")
    }

  }

  trait FootballPlayer {
    def shoot = println("I am shooting the ball now!")

    def catchBallWithHands = println("I am catching the ball with my hands now!")
  }

  trait Goalkeeper extends Role[NaoRobot] with FootballPlayer {
    def behavior = {
      println("I am watching the goal.")
      // watchTheGoal()
    }
  }

  trait FieldPlayer extends Role[NaoRobot] with FootballPlayer {
    override def catchBallWithHands = println("I am not allowed to do this!")
  }

}