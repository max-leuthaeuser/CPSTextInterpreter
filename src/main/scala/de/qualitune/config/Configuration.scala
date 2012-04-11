package de.qualitune.config

import sun.reflect.generics.reflectiveObjects.NotImplementedException

/**
 * Configuration classes representing a config for CPSText interpreter.
 *
 * @author Max Leuthaeuser
 * @date 11.03.2012
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

case class Target(var enabled: Boolean, var depends: String)

case class Debugging(override var enabled: Boolean, override var depends: String, var path: String) extends Target(enabled, depends) {
  def write(text: String) {
    if (path == "stdout") {
      println(text)
    } else {
      // write to file
      // TODO implement this
      throw new NotImplementedException()
    }
  }
}

case class Interpretation(override var enabled: Boolean, override var depends: String) extends Target(enabled, depends)

case class Execution(override var enabled: Boolean, override var depends: String) extends Target(enabled, depends)

case class Configuration(var debugging: Debugging, var interpretation: Target, var execution: Target, var clean: Boolean) {

}