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

package ast.rule

case class ActivationRule(activateFor: List[ActivationRuleVariable], when: String, bindings: List[ActivationRuleBinding], interval: Int = 100) {
  def prettyPrint(identLevel: Int): String = {
    var ident = ""
    (1 to identLevel).foreach(e => {
      ident += "\t"
    })

    "activate for {\n" +
      activateFor.map(_.toString).mkString("\t\t" + ident, "\n\t\t" + ident, "\n\t" + ident) +
      "when(" + interval + ") { " + when + " } with bindings {\n" +
      bindings.map(_.toString).mkString("\t\t" + ident, "\n\t\t" + ident, "\n\t" + ident) + "}"
  }

  override def toString = {
    prettyPrint(0)
  }
}