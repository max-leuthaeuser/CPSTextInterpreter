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

package interpreter

import ast.rule.ActivationRule

/**
 * User: Max Leuthaeuser
 * Date: 18.01.12
 */
class ActivationRuleInterpreter extends ASTElementInterpreter {
  override def apply[E <: AnyRef](s: EvaluableString, elem: E) = {
    elem match {
      case ar: (ActivationRule, String) => {
        val actorName = "context_activator_" + ar._1.name
        s + ("val " + actorName.toLowerCase + " = new " + actorName + "()\n")
        s + ("class " + actorName + " extends Actor {\ndef act() {\n")
        if (ar._1.settings.timeout > 0)
          s + ("Thread.sleep(" + ar._1.settings.timeout + ")\n")
        s + ("while(!(" + ar._1.when + ")) {" + "Thread.sleep(" + ar._1.settings.interval + ")\n" + "} do_activate_" + ar._1.name + "()\n")
        s + ("\n}\n")
        s
      }
      case _ => throw new IllegalArgumentException("Unknown ActivationRule type!")
    }
  }
}