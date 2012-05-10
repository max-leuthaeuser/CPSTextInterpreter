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

package de.qualitune.transformator

/**
 * Represents an evaluable String that are generated from CPSText program.
 * It simply adopts a StringBuilder an adds helper methods.
 * Additionally a field for storing all information for activation blocks is provided.
 *
 * @author Max Leuthaeuser
 * @since 18.01.2012
 */
class ExecutableString {
  lazy private val wrapped = new StringBuilder()
  private val inPlace = new StringBuilder()

  /**
   * Adds a String.
   *
   * @param s: the String to add
   * @return this
   */
  def +(s: String): ExecutableString = {
    wrapped.append(s)
    this
  }

  /**
   * Adds a String for in place interpretation.
   *
   * @param s: the String to add
   * @return this
   */
  def +=(s: String): ExecutableString = {
    inPlace.append(s + ";")
    this
  }


  /**
   * Adds a List of Strings.
   *
   * @param l: the List of Strings to add
   * @return this
   */
  def ++(l: List[String]): ExecutableString = {
    l.foreach(wrapped.append(_))
    this
  }

  /**
   * Adds a List of Strings for in place interpretation.
   *
   * @param l: the List of Strings to add
   * @return this
   */
  def ++=(l: List[String]): ExecutableString = {
    l.foreach(x => inPlace.append(x + ";"))
    this
  }

  override def toString = wrapped.toString()

  def getInPlace = inPlace
}