package de.qualitune.config

import java.io.File
import de.qualitune.util.IOUtils

/**
 * Configuration classes representing a config for CPSText transformation and execution phase.
 *
 * @author Max Leuthaeuser
 * @since 11.03.2012
 */

object Configuration {
  def apply(debugging: Debugging, interpretation: Target, execution: Target, clean: Boolean) = {
    if (execution.depends != "Interpretation")
      throw new Exception("Target Execution can not run without Interpretation")
    if (execution.depends != "Interpretation" && !interpretation.enabled)
      throw new Exception("Target Execution can not run without Interpretation!")
    if (execution.depends == "Interpretation" && !interpretation.enabled)
      throw new Exception("Target Execution can not run without Interpretation!")

    new Configuration(debugging, interpretation, execution, clean)
  }
}

case class Target(enabled: Boolean, depends: String)

case class Debugging(override val enabled: Boolean, override val depends: String, path: String) extends Target(enabled, depends) {
  def write(text: String) {
    if (path == "stdout") {
      print(text + "\n")
    } else {
      // write to file
      val file = new File(path)
      if (file.exists())
        IOUtils.appendToFile(path, text)
      else
        IOUtils.writeToFile(path, text)
    }
  }
}

case class Interpretation(override val enabled: Boolean, override val depends: String) extends Target(enabled, depends)

case class Execution(override val enabled: Boolean, override val depends: String) extends Target(enabled, depends)

class Configuration private(val debugging: Debugging, val interpretation: Target, val execution: Target, val clean: Boolean)