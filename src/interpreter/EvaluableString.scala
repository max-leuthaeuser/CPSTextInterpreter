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

/**
 * Represents an evaluable String that are generated from CPSText program.
 * It simply adopts a StringBuilder an adds helper methods.
 *
 * @author Max Leuthaeuser
 * @date 18.01.2012
 */
class EvaluableString {
  lazy private val wrapped = new StringBuilder()

  /**
   * Adds a String.
   *
   * @param s: the String to add
   * @return this
   */
  def +(s: String): EvaluableString = {
    wrapped.append(s)
    this
  }

  /**
   * Adds a List of Strings.
   *
   * @param l: the List of Strings to add
   * @return this
   */
  def ++(l: List[String]): EvaluableString = {
    l.map(wrapped.append(_))
    this
  }

  override def toString = wrapped.toString()
}