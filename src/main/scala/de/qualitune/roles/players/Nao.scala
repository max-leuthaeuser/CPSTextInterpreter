package de.qualitune.roles.players

import com.aldebaran.proxy._
import de.qualitune.NaoAbstraction
import de.qualitune.roles.{ComponentCore, ComponentRole, Component}

/**
 * Companion object for {@link de.qualitune.roles.players.Nao}.
 * Provides standard IP and PORT and a static factory method
 * to create a new {@link de.qualitune.roles.ComponentCore} with Nao as played {@link de.qualitune.roles.ComponentRole}.
 *
 * @author Max Leuthaeuser
 * @since 25.04.12
 */
object Nao {
  val STANDARD_IP: String = "192.168.0.139"
  val STANDARD_PORT: Int = 8070

  def createWithCore(name: String, ip: String, port: Int) = {
    val core = new ComponentCore()
    val robot = new Nao(name, ip, port, core)
    core.addRole(robot)
    core
  }
}

/**
 * Role class representing a NAO robot.
 *
 * @author Max Leuthaeuser
 * @since 25.04.12
 */
class Nao(override val name: String = "Nao", val ip: String = Nao.STANDARD_IP, val port: Int = Nao.STANDARD_PORT, private val core: Component) extends ComponentRole(core) with Robot {
  def AudioDevice(): ALAudioDeviceProxy = NaoAbstraction.createALAudioDevice(ip, port)

  def AudioPlayer(): ALAudioPlayerProxy = NaoAbstraction.createALAudioPlayer(ip, port)

  def AudioSourceLocalization(): ALAudioSourceLocalizationProxy = NaoAbstraction.createALAudioSourceLocalization(ip, port)

  def BehaviorManager(): ALBehaviorManagerProxy = NaoAbstraction.createALBehaviorManager(ip, port)

  def Bluetooth(): ALBluetoothProxy = NaoAbstraction.createALBluetooth(ip, port)

  def Bonjour(): ALBonjourProxy = NaoAbstraction.createALBonjour(ip, port)

  def FaceDetection(): ALFaceDetectionProxy = NaoAbstraction.createALFaceDetection(ip, port)

  def FaceTracker(): ALFaceTrackerProxy = NaoAbstraction.createALFaceTracker(ip, port)

  def FileManager(): ALFileManagerProxy = NaoAbstraction.createALFileManager(ip, port)

  def FrameManager(): ALFrameManagerProxy = NaoAbstraction.createALFrameManager(ip, port)

  def Fsr(): ALFsrProxy = NaoAbstraction.createALFsr(ip, port)

  def Infrared(): ALInfraredProxy = NaoAbstraction.createALInfrared(ip, port)

  def LandMarkDetection(): ALLandMarkDetectionProxy = NaoAbstraction.createALLandMarkDetection(ip, port)

  def Laser(): ALLaserProxy = NaoAbstraction.createALLaser(ip, port)

  def Launcher(): ALLauncherProxy = NaoAbstraction.createALLauncher(ip, port)

  def Leds(): ALLedsProxy = NaoAbstraction.createALLeds(ip, port)

  def Logger(): ALLoggerProxy = NaoAbstraction.createALLogger(ip, port)

  def Memory(): ALMemoryProxy = NaoAbstraction.createALMemory(ip, port)

  def Motion(): ALMotionProxy = NaoAbstraction.createALMotion(ip, port)

  def MotionRecorder(): ALMotionRecorderProxy = NaoAbstraction.createALMotionRecorder(ip, port)

  def Preferences(): ALPreferencesProxy = NaoAbstraction.createALPreferences(ip, port)

  def PythonBridge(): ALPythonBridgeProxy = NaoAbstraction.createALPythonBridge(ip, port)

  def RedBallDetection(): ALRedBallDetectionProxy = NaoAbstraction.createALRedBallDetection(ip, port)

  def RedBallTracker(): ALRedBallTrackerProxy = NaoAbstraction.createALRedBallTracker(ip, port)

  def ResourceManager(): ALResourceManagerProxy = NaoAbstraction.createALResourceManager(ip, port)

  def RobotPose(): ALRobotPoseProxy = NaoAbstraction.createALRobotPose(ip, port)

  def Sensors(): ALSensorsProxy = NaoAbstraction.createALSensors(ip, port)

  def Sentinel(): ALSentinelProxy = NaoAbstraction.createALSentinel(ip, port)

  def Sonar(): ALSonarProxy = NaoAbstraction.createALSonar(ip, port)

  def SoundDetection(): ALSoundDetectionProxy = NaoAbstraction.createALSoundDetection(ip, port)

  def SpeechRecognition(): ALSpeechRecognitionProxy = NaoAbstraction.createALSpeechRecognition(ip, port)

  def TextToSpeech(): ALTextToSpeechProxy = NaoAbstraction.createALTextToSpeech(ip, port)

  def VideoDevice(): ALVideoDeviceProxy = NaoAbstraction.createALVideoDevice(ip, port)

  def VisionRecognition(): ALVisionRecognitionProxy = NaoAbstraction.createALVisionRecognition(ip, port)

  def VisionToolbox(): ALVisionToolboxProxy = NaoAbstraction.createALVisionToolbox(ip, port)

  def DCM(): DCMProxy = NaoAbstraction.createDCM(ip, port)
}
