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

package de.qualitune.parser

import java.net.InetAddress
import scala.util.parsing.combinator._
import de.qualitune.ast.cps.CPSType._
import de.qualitune.ast.cps.{CPS, CPSType}
import de.qualitune.ast.{CPSProgram, Context}
import de.qualitune.ast.variable.{VariableDeclAccessType, EmptyVariableDecl, InitVariableDecl, VariableDecl}
import de.qualitune.ast.rule.{ActivationRuleBinding, ActivationRuleVariable, ActivationRule, Settings}
import de.qualitune.ast.role._
import de.qualitune.ast.callable.{Behavior, Operation}
import scala.ScalaObject

/**
 * Parser for parsing CPSText and creating an instance of the corresponding AST.
 *
 * @author Max Leuthaeuser
 * @since 22.11.2011
 */
object CPSTextParser extends JavaTokenParsers {
  // ignore whitespaces and all c-style comments
  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  def cpsProgram: Parser[CPSProgram] = imports ~ robots ~ contexts ^^ {
    case i ~ r ~ c => CPSProgram(i, r, c)
  }

  def imports: Parser[List[String]] = opt("import {" ~> rep1(importItem) <~ "}") ^^ {
    _.getOrElse(List[String]())
  }

  def importItem: Parser[String] = """[a-zA-Z_\.\*]+""".r <~ ";"

  def robots: Parser[List[CPS]] = rep(robot)

  def robot: Parser[CPS] = cpsType ~ ident ~ "IP" ~ ip ~ "PORT" ~ port ^^ {
    case t ~ n ~ "IP" ~ i ~ "PORT" ~ p => CPS(t, n, i, p)
  }

  def cpsType: Parser[CPSType] = ("Nao" | "Mindstorm") ^^ {
    case "Nao" => CPSType.Nao
    case "Mindstorm" => CPSType.Mindstorm
  }

  def ip: Parser[String] = ipv4Address | ipv6Address

  def ipv4Address: Parser[String] = """[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}""".r ^^ {
    InetAddress.getByName(_).toString.replace("/", "")
  }

  def ipv6Address: Parser[String] = """[:%a-z0-9]+""".r ^^ {
    InetAddress.getByName(_).toString.replace("/", "")
  }

  def port: Parser[Int] = decimalNumber ^^ {
    case s: String => {
      val p = s.toInt
      if (p <= 0) throw new Exception("Invalid Port: " + s)
      p
    }
  }

  def contexts: Parser[List[Context]] = rep1(context)

  def codeLine: Parser[String] = """[^\{^\}^;]*""".r ^^ {
    _.trim
  }

  def codeBlock: Parser[String] = "{" ~> expr <~ "}" | "{}"

  def expr: Parser[String] = """[^\{^\}]*;{0,1}""".r ~ opt(parens) ^^ {
    case a ~ b => {
      val body = b.getOrElse("")
      if (body.isEmpty) {
        a
      } else {
        a + "{" + body + "}"
      }
    }
  }

  def parens: Parser[String] = "{" ~> expr <~ "}"

  def activationRuleVariable: Parser[ActivationRuleVariable] = ident ~ ident ^^ {
    case r ~ n => ActivationRuleVariable(r, n)
  }

  def activationRuleVariables: Parser[List[ActivationRuleVariable]] = rep1(activationRuleVariable <~ ";")

  def activationRuleBinding: Parser[ActivationRuleBinding] = ident ~ "->" ~ ident ^^ {
    case n ~ "->" ~ r => ActivationRuleBinding(n, r)
  }

  def activationRuleBindings: Parser[List[ActivationRuleBinding]] = rep1(activationRuleBinding <~ ";")

  def activationRule: Parser[ActivationRule] = "activate for {" ~ activationRuleVariables ~ "} when" ~ "{" ~ codeLine ~ "} with bindings {" ~ activationRuleBindings ~ "}" ~ settings ^^ {
    case "activate for {" ~ av ~ "} when" ~ "{" ~ c ~ "} with bindings {" ~ ab ~ "}" ~ s => ActivationRule(av, c, ab, s)
  }

  def settings: Parser[Settings] = "with settings {" ~> setting <~ "}"

  def setting: Parser[Settings] = interval ~ after ~ continuously ^^ {
    case i ~ t ~ c => Settings(i, t, c)
  }

  def interval: Parser[Int] = opt("interval " ~> decimalNumber <~ ";") ^^ {
    _.getOrElse("100").toInt
  }

  def after: Parser[Int] = opt("after " ~> decimalNumber <~ ";") ^^ {
    _.getOrElse("0").toInt
  }

  def continuously: Parser[Boolean] = opt("continuously " ~> ident <~ ";") ^^ {
    _.getOrElse("false") match {
      case "true" => true
      case "false" => false
      case _ => throw new IllegalArgumentException("Setting \"continuously\" can only be \"true\" or \"false\"!")
    }
  }

  def context: Parser[Context] = "context" ~ ident ~ "{" ~ rep1(activationRule) ~ contextContent ~ "}" ^^ {
    case "context" ~ n ~ "{" ~ a ~ c ~ "}" => Context.build(n, c, a)
  }

  def variableValue: Parser[String] = opt("=" ~> codeLine) ^^ {
    _.getOrElse("")
  }

  def variableDecl: Parser[VariableDecl] = ("var" | "val") ~ ident ~ ":" ~ ident ~ variableValue ^^ {
    case "var" ~ n ~ ":" ~ t ~ "" => EmptyVariableDecl(VariableDeclAccessType.modifiable, n, t)
    case "var" ~ n ~ ":" ~ t ~ v => InitVariableDecl(VariableDeclAccessType.modifiable, n, t, v)
    case "val" ~ n ~ ":" ~ t ~ "" => EmptyVariableDecl(VariableDeclAccessType.unmodifiable, n, t)
    case "val" ~ n ~ ":" ~ t ~ v => InitVariableDecl(VariableDeclAccessType.unmodifiable, n, t, v)
  }

  def variableDecls: Parser[List[VariableDecl]] = rep(variableDecl <~ ";")

  def optVariableDecls: Parser[List[VariableDecl]] = opt(variableDecls) ^^ {
    _.getOrElse(List[VariableDecl]())
  }

  def optRoles: Parser[List[Role]] = opt(roles) ^^ {
    _.getOrElse(List[Role]())
  }

  def optContexts: Parser[List[Context]] = opt(contexts) ^^ {
    _.getOrElse(List[Context]())
  }

  def optConstraints: Parser[List[RoleConstraint]] = opt(constraints) ^^ {
    _.getOrElse(List[RoleConstraint]())
  }

  def contextContent: Parser[List[ScalaObject]] = rep((variableDecl <~ ";")
    | (constraint <~ ";")
    | role
    | context)

  def behavior: Parser[Behavior] = "behavior " ~> codeBlock ^^ {
    Behavior(_)
  }

  def method: Parser[Operation] = ident ~ ident ~ "() " ~ codeBlock ^^ {
    case t ~ n ~ "() " ~ c => Operation(n, t, c)
  }

  def methods: Parser[List[Operation]] = rep(method)

  def optMethods: Parser[List[Operation]] = opt(methods) ^^ {
    _.getOrElse(List[Operation]())
  }

  def role: Parser[Role] = opt(singleton) ~ "role" ~ ident ~ "{" ~ behavior ~ roleContent ~ "}" ^^ {
    case s ~ "role" ~ n ~ "{" ~ b ~ c ~ "}" => Role.build(s.getOrElse(false), n, b, c)
  }

  def singleton: Parser[Boolean] = "singleton" ^^^ true

  def roleContent: Parser[List[ScalaObject]] = rep((variableDecl <~ ";") | method)

  def roles: Parser[List[Role]] = rep(role)

  def constraint: Parser[RoleConstraint] = ident ~ ("implies" | "prohibits" | "equals") ~ ident ^^ {
    case ra ~ "implies" ~ rb => ImplicationConstraint(ra, rb)
    case ra ~ "prohibits" ~ rb => ProhibitionConstraint(ra, rb)
    case ra ~ "equals" ~ rb => EquivalenceConstraint(ra, rb)
  }

  def constraints: Parser[List[RoleConstraint]] = rep(constraint <~ ";")

  /**
   * Will parse a String and return an instance of the CPS AST.
   *
   * @param p: a String representing a piece of CPSText code.
   * @return an instance of CPSProgram representing the concrete syntax tree for a given CPSText program.
   */
  def parse(p: String): CPSProgram = {
    parseAll(cpsProgram, p) match {
      case Success(r, _) => r.asInstanceOf[CPSProgram]
      case e => throw new Exception("Invalid CPSText source:\n" + e.toString)
    }
  }
}