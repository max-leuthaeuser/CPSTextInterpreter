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

import parser.CPSTextDSL

object Example {
  def main(args: Array[String]) {
    val p = "Nao Chuck IP 192.234.1.22 PORT 8080;" +
      "Mindstorm Hans IP 192.168.1.1 PORT 8090;" +
      "context Football {" +
      " activate for {" +
      "  Nao a; Nao b;" +
      " } when { bla() && foo() } with bindings {" +
      "  a -> SomeRole; b -> OtherRole;" +
      " }" +
      " var test: Int = 10;" +
      " val blub: Int = 11;" +
      " FieldPlayer prohibits Keeper;" +
      " role FieldPlayer playedBy Hans {" +
      "  behavior {" +
      "   // not implemented yet" +
      "  }" +
      "  var x: Float;" +
      "  var y: Float;" +
      " }" +
      " role Keeper playedBy Chuck {" +
      "  behavior {" +
      "   while(true) watchTheGoal()" +
      "  }" +
      "  void watchTheBall() {" +
      "    // some code here" +
      "    magic();" +
      "  }" +
      "  void anotherMedthod() {" +
      "   bla();" +
      "  }" +
      " }" +
      " context Pass {" +
      "  activate for {" +
      "   Nao r; Nao s;" +
      "  } when { passPossible() } with bindings {" +
      "   r -> Reciever; s -> Sender;" +
      "  }" +
      "  Sender prohibits Reciever;" +
      "  Reciever prohibits Sender;" +
      " }" +
      "}"

    // TODO: test roles in nested contexts
    println(CPSTextDSL.parse(p))
  }
}