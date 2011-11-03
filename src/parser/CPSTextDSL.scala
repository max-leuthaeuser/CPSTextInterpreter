package parser

import java.net.InetAddress
import scala.util.parsing.combinator._
import ast.cps.CPSType._
import ast.cps.{CPS, CPSType}
import ast.{CPSProgram, Context}

/**
 * Parser for parsing CPSText and creating an instance of the AST.
 */
object CPSTextDSL extends JavaTokenParsers {
  def cpsprogram: Parser[CPSProgram] = robots ~ contexts ^^ {
    case robots ~ contexts => CPSProgram(robots, contexts)
  }

  def robots: Parser[List[CPS]] = rep(robot <~ ";") ^^ {
    (l: List[CPS]) => l
  }

  def robot: Parser[CPS] = cpstype ~ ident ~ "IP" ~ ip ~ "PORT" ~ port ^^ {
    case t ~ n ~ "IP" ~ i ~ "PORT" ~ p => CPS(t, n, i, p)
  }

  def cpstype: Parser[CPSType] = ("Nao" | "Mindstorm") ^^ {
    case "Nao" => CPSType.Nao
    case "Mindstorm" => CPSType.Mindstorm
  }

  def ip: Parser[String] = ipv4Address | ipv6Address

  def ipv4Address: Parser[String] = """[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}""".r ^^ {
    s => InetAddress.getByName(s).toString
  }

  def ipv6Address: Parser[String] = """[:%a-z0-9]+""".r ^^ {
    s => InetAddress.getByName(s).toString
  }

  def port: Parser[Int] = decimalNumber ^^ {
    case s: String => s.toInt
  }

  def contexts: Parser[List[Context]] = rep1(context) ^^ { l:List[Context] => l }

  def context: Parser[Context] = "context" ~ ident ~ "{" ~ optContexts ~ "}" ^^ {
    case "context" ~ n ~ "{" ~ c ~ "}" => Context(n, c)   // TODO
  }

  def optContexts: Parser[List[Context]] = opt(contexts) ^^ {
    case None => List[Context]()
    case Some(contexts) => contexts
  }

  /**
   * Will parse the String s and return an instance of the CPS AST.
   */
  def parse(p: String) = {
    parseAll(cpsprogram, p) match {
      case Success(r, _) => r
      case e => throw new Exception(e.toString)
    }
  }
}