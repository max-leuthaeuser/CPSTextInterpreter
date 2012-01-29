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
 * Small class which wraps around Twitters Eval
 * and does nothing else than adding some shortcut functions.
 * 
 * User: Max Leuthaeuser
 * Date: 25.01.12
 */
class ScalaInterpreter {
  private lazy val eval = new Eval()

  /**
   * Compiles a String to the virtual class loader.
   * @waring: resets the virtual class loader.
   *
   * @param s: the String to compile and evaluate
   */
  def !(s: String) {
    eval.compile(s)
  }

  /**
   * Like !, but doesn't reset the virtual class loader before evaluating.
   *
   * @param s: the String to evaluate in place
   */
  def !=(s: String) {
    eval.inPlace(s)
  }

  /**
   * Compiles an EvaluableString to the virtual class loader.
   * @waring: resets the virtual class loader.
   *
   * @param s: the EvaluableString to compile and evaluate
   */
  def !(s: EvaluableString) {
    eval.compile(s.toString)
  }

  /**
   * Like !, but doesn't reset the virtual class loader before evaluating.
   * @param s: the EvaluableString to evaluate in place
   */
  def !=(s: EvaluableString) {
    eval.inPlace(s.toString)
  }
}