package de.qualitune.config

import java.io.{PrintWriter, BufferedReader}
import tools.nsc.reporters.ConsoleReporter

/**
 * Reporter to pass messages from compilation process to the outer world.
 *
 * @author Max Leuthaeuser
 * @since 06.05.12
 */
class ConfigReporter(override val settings: scala.tools.nsc.Settings,
                     reader: BufferedReader,
                     writer: PrintWriter) extends ConsoleReporter(settings, reader, writer) {

  private var config: Configuration = null

  def this(settings: scala.tools.nsc.Settings, config: Configuration) {
    this(settings, Console.in, new PrintWriter(Console.err, true))
    this.config = config
  }

  override def printMessage(msg: String) {
    config.debugging.write(msg)
  }
}
