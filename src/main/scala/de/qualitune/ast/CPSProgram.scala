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

package de.qualitune.ast

import cps.CPS
import role.Role

case class CPSProgram(imports: List[String], robots: List[CPS], contexts: List[Context]) extends ASTElement {
  //some helper methods:
  def getAllRoles(c: List[Context] = contexts): List[Role] = {
    var l = List[Role]()
    c.foreach(e => {
      e.roles.foreach(r => {
        l = r :: l
      })
      l = l ++ getAllRoles(e.inner)
    })
    l
  }

  def getRolePaths(c: List[Context] = contexts): List[String] = {
    var l = List[String]()
    c.foreach(e => {
      e.roles.foreach(r => {
        l = ("context_" + e.name.toLowerCase + "." + r.name) :: l
      })
      l = l ++ getRolePaths(e.inner)
    })
    l
  }

  def getContextPaths(path: String = "", c: List[Context] = contexts): List[String] = {
    var l = List[String]()
    c.foreach(e => {
      l = (path + "Context_" + e.name) :: l
      l = l ++ getContextPaths("context_" + e.name.toLowerCase + ".", e.inner)
    })
    l
  }

  override def toString = {
    robots.map(_.toString).mkString("\n") + "\n\n" + contexts.map(_.toString).mkString("\n")
  }
}