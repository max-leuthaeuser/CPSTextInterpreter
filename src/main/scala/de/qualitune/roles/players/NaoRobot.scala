package de.qualitune.roles.players

/**
 * User: Max Leuthaeuser
 * Date: 25.04.12
 */

import de.qualitune.Nao._

object NaoRobot {
  val STANDARD_IP: String = "192.168.0.139"
  val STANDARD_PORT: Int = 8070

  implicit def toNao(n: String) = new NaoRobot {
    name = n
  }
}

/**
 * Trait representing a NAO robot.
 */
trait NaoRobot extends Robot {
  var ip: String = NaoRobot.STANDARD_IP
  var port: Int = NaoRobot.STANDARD_PORT

  def IP(ip: String): NaoRobot = {
    this.ip = ip
    this
  }

  def PORT(port: Int): NaoRobot = {
    this.port = port
    this
  }

  def AudioDevice(): ALAudioDevice = new ALAudioDevice(ip, port)

  def AudioPlayer(): ALAudioPlayer = new ALAudioPlayer(ip, port)

  def AudioSourceLocalization(): ALAudioSourceLocalization = new ALAudioSourceLocalization(ip, port)

  def BehaviorManager(): ALBehaviorManager = new ALBehaviorManager(ip, port)

  def Bluetooth(): ALBluetooth = new ALBluetooth(ip, port)

  def Bonjour(): ALBonjour = new ALBonjour(ip, port)

  def FaceDetection(): ALFaceDetection = new ALFaceDetection(ip, port)

  def FaceTracker(): ALFaceTracker = new ALFaceTracker(ip, port)

  def FileManager(): ALFileManager = new ALFileManager(ip, port)

  def FrameManager(): ALFrameManager = new ALFrameManager(ip, port)

  def Fsr(): ALFsr = new ALFsr(ip, port)

  def Infrared(): ALInfrared = new ALInfrared(ip, port)

  def LandMarkDetection(): ALLandMarkDetection = new ALLandMarkDetection(ip, port)

  def Laser(): ALLaser = new ALLaser(ip, port)

  def Launcher(): ALLauncher = new ALLauncher(ip, port)

  def Leds(): ALLeds = new ALLeds(ip, port)

  def Logger(): ALLogger = new ALLogger(ip, port)

  def Memory(): ALMemory = new ALMemory(ip, port)

  def Motion(): ALMotion = new ALMotion(ip, port)

  def MotionRecorder(): ALMotionRecorder = new ALMotionRecorder(ip, port)

  def Preferences(): ALPreferences = new ALPreferences(ip, port)

  def PythonBridge(): ALPythonBridge = new ALPythonBridge(ip, port)

  def RedBallDetection(): ALRedBallDetection = new ALRedBallDetection(ip, port)

  def RedBallTracker(): ALRedBallTracker = new ALRedBallTracker(ip, port)

  def ResourceManager(): ALResourceManager = new ALResourceManager(ip, port)

  def RobotPose(): ALRobotPose = new ALRobotPose(ip, port)

  def Sensors(): ALSensors = new ALSensors(ip, port)

  def Sentinel(): ALSentinel = new ALSentinel(ip, port)

  def Sonar(): ALSonar = new ALSonar(ip, port)

  def SoundDetection(): ALSoundDetection = new ALSoundDetection(ip, port)

  def SpeechRecognition(): ALSpeechRecognition = new ALSpeechRecognition(ip, port)

  def TextToSpeech(): ALTextToSpeech = new ALTextToSpeech(ip, port)

  def VideoDevice(): ALVideoDevice = new ALVideoDevice(ip, port)

  def VisionRecognition(): ALVisionRecognition = new ALVisionRecognition(ip, port)

  def VisionToolbox(): ALVisionToolbox = new ALVisionToolbox(ip, port)

  def DCM(): DCM = new DCM(ip, port)
}
