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

package ast.callable

case class Behavior(body: String) extends Callable {
  def prettyPrint(identLevel: Int): String = {
    var ident = ""
    (1 to identLevel).foreach(e => {
      ident += "\t"
    })

    var b = ""
    if (body.matches("\\s*"))
      b = " }"
    else {
      b = body.replaceAll("\n", "\n\t\t\t" + ident)
      if (b.length >= 3)
        b = b.subSequence(0, (b.length - 3 - identLevel)).toString
      b = "\n\t\t\t" + ident + b + "\t\t" + ident + "}"
    }

    "\tbehavior {" + b
  }

  override def toString = prettyPrint(0)
}