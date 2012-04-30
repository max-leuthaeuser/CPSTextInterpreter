package de.qualitune.config

import util.parsing.combinator.JavaTokenParsers

/**
 * Parser for parsing a config file  and creating an instance of Configuration.
 *
 * @author Max Leuthaeuser
 * @since 11.03.2012
 */
object ConfigReader extends JavaTokenParsers {
  // ignore whitespaces and all c-style comments
  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  def config: Parser[Configuration] = debugging ~ target ~ target ~ clean ^^ {
    case d ~ i ~ e ~ c => Configuration(d, i, e, c)
  }

  def target: Parser[Target] = "target" ~ ident ~ "{" ~ "requires" ~ ":" ~ ident ~ "enabled" ~ ":" ~ bool ~ "}" ^^ {
    case "target" ~ "Interpretation" ~ "{" ~ "requires" ~ ":" ~ req ~ "enabled" ~ ":" ~ e ~ "}" => Interpretation(e, req)
    case "target" ~ "Execution" ~ "{" ~ "requires" ~ ":" ~ req ~ "enabled" ~ ":" ~ e ~ "}" => Execution(e, req)
  }

  def clean: Parser[Boolean] = "target" ~ "Clean" ~ "{" ~ "enabled" ~ ":" ~> bool <~ "}"

  def debugging: Parser[Debugging] = "target" ~ "Debugging" ~ "{" ~ "enabled" ~ ":" ~ bool ~ "target" ~ ":" ~ path ~ "}" ^^ {
    case "target" ~ "Debugging" ~ "{" ~ "enabled" ~ ":" ~ db ~ "target" ~ ":" ~ t ~ "}" => Debugging(db, "", t)
  }

  def path: Parser[String] = ("stdout" | (("file" ~ "{" ~ "path" ~ ":") ~> stringLiteral <~ "}")) ^^ {
    case "stdout" => "stdout"
    case p: String => p
  }

  def bool: Parser[Boolean] = ("true" | "false") ^^ {
    case "true" => true
    case "false" => false
  }

  /**
   * Will parse a String and return an instance of Configuration
   *
   * @param p: a String representing a textual configuration
   * @return an instance of Configuration
   */
  def parse(p: String): Configuration = {
    parseAll(config, p) match {
      case Success(r, _) => r.asInstanceOf[Configuration]
      case e => throw new Exception("Invalid configuration file:\n" + e.toString)
    }
  }
}