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

import parser.CPSTextParser._
import org.scalatest.FunSuite

object ParserUtils {
  /**
   * Will parse the String 's' with parser method 'parser' and returning
   * the corresponding AST part.
   */
  def parse[T](parser: Parser[T], p: String) = {
    parseAll(parser, p) match {
      case Success(r, _) => r
      case e => throw new Exception(e.toString)
    }
  }
}

class ParserUnitTests extends FunSuite {
  test("Testing cpsprogram parser") {
    // cpsprogram
  }

  test("Testing imports parser") {
    // imports
  }

  test("Testing robots parser") {
    // robots
  }

  test("Testing robot parser") {
    // robot
  }

  test("Testing cpstype") {
    import ast.cps.CPSType
    import ast.cps.CPSType._
    val expResult: (CPSType, CPSType) = (CPSType.Nao, CPSType.Mindstorm)
    assert(expResult._1 === ParserUtils.parse[CPSType](cpstype, "Nao"))
    assert(expResult._2 === ParserUtils.parse[CPSType](cpstype, "Mindstorm"))

    intercept[Exception] {
      ParserUtils.parse[CPSType](cpstype, "ABC") // invalid robot type
    }
  }

  test("Testing ip parser") {
    // ip
  }

  test("Testing ipv4Address parser") {
    val expResult: String = "192.169.0.1"
    assert(expResult === ParserUtils.parse[String](ipv4Address, "192.169.0.1"))

    val invalid: String = "192.169.a.b" // not a valid ipv4 address
    intercept[Exception] {
      ParserUtils.parse[String](ipv4Address, invalid)
    }
  }

  test("Testing ipv6Address parser") {
    // these are actually the same addresses, leading zeros can be ignored.
    val expResult1: String = "2001:0db8:0000:08d3:0000:8a2e:0070:7344"
    val expResult2: String = "2001:db8:0:8d3:0:8a2e:70:7344"

    assert(expResult2 === ParserUtils.parse[String](ipv6Address, expResult1))
    assert(expResult2 === ParserUtils.parse[String](ipv6Address, expResult2))

    val invalid1: String = "192.169.a.b" // not a valid ipv6 address
    val invalid2: String = "192.169.2.1" // not a valid ipv6 address
    val invalid3: String = "2001:db8:0:8d3:0:8a2e:70:7344:z" // not a valid ipv6 address
    intercept[Exception] {
      ParserUtils.parse[String](ipv6Address, invalid1)
    }
    intercept[Exception] {
      ParserUtils.parse[String](ipv6Address, invalid2)
    }
    intercept[Exception] {
      ParserUtils.parse[String](ipv6Address, invalid3)
    }
  }

  test("Testing port parser") {
    val expResult: Int = 8080
    val invalid1: String = "80aa"
    val invalid2: String = "-1"
    val invalid3: String = "0"
    val invalid4: String = "2.2"

    assert(expResult === ParserUtils.parse[Int](port, "8080"))
    intercept[Exception] {
      ParserUtils.parse[Int](port, invalid1)
    }
    intercept[Exception] {
      ParserUtils.parse[Int](port, invalid2)
    }
    intercept[Exception] {
      ParserUtils.parse[Int](port, invalid3)
    }
    intercept[Exception] {
      ParserUtils.parse[Int](port, invalid4)
    }
  }

  test("Testing contexts parser") {
    // contexts
  }

  test("Testing codeLine parser") {
    // codeLine
  }

  test("Testing codeBlock parser") {
    // codeBlock
  }

  test("Testing activationRuleVariable parser") {
    // activationRuleVariable
  }

  test("Testing activationRuleVariables parser") {
    // activationRuleVariables
  }

  test("Testing activationRuleBinding parser") {
    // activationRuleBinding
  }

  test("Testing activationRuleBindings parser") {
    // activationRuleBindings
  }

  test("Testing activationRule parser") {
    // activationRule
  }

  test("Testing context parser") {
    // context
  }

  test("Testing variableValue parser") {
    // variableValue
  }

  test("Testing variableDecl parser") {
    // variableDecl
  }

  test("Testing variableDecls parser") {
    // variableDecls
  }

  test("Testing optVariableDecls parser") {
    // optVariableDecls
  }

  test("Testing contextContent parser") {
    // contextContent
  }

  test("Testing behavior parser") {
    // behavior
  }

  test("testing method parser") {
    // method
  }

  test("testing methods parser") {
    // methods
  }

  test("testing optMethods parser") {
    // optMethods
  }

  test("testing role parser") {
    // role
  }

  test("testing roles parser") {
    // roles
  }

  test("testing constraint parser") {
    // constraint
  }

  test("testing constraints parser") {
    // constrains
  }

  test("testing contextContents parser") {
    // contextContents
  }
}