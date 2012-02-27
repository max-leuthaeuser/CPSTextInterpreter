/**
 * CPSTextInterpreter - parses and interprets the CPSText DSL.
 * Copyright (C) 2011 Max Leuthaeuser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.qualitune.interpreter

import de.qualitune.ast.rule.ActivationRule
import de.qualitune.ast.ASTElement

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class ActivationRuleInterpreter extends ASTElementInterpreter {
  override def apply[E <: ASTElement, T <: AnyRef](s: EvaluableString, elem: E, data: T) = {
    elem match {
      case ar: ActivationRule => {
          val actorName = "Context_Activator_" + ar.name
          s + ("val " + actorName.toLowerCase + " = new " + actorName + "()\n")
          s + ("class " + actorName + " extends Actor {\ndef act() {\n")
          if (ar.settings.timeout > 0)
            s + ("Thread.sleep(" + ar.settings.timeout + ")\n")
          s + ("while(!(" + ar.when + ")) {" + "Thread.sleep(" + ar.settings.interval + ")\n" + "}\n do_activate_" + ar.name + "(); exit()}\n")
          s + ("\n}\n")
          s
        }
      case _ => throw new IllegalArgumentException("Unknown ActivationRule type!")
    }
  }
}