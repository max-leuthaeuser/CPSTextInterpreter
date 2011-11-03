import parser.CPSTextDSL

object Example {
  def main(args: Array[String]) {
    val p = "Nao Chuck IP 192.234.1.22 PORT 8080; Mindstorm Hans IP 192.168.1.1 PORT 8090;" +
            "context Football { context Pass { } }" +
            "context Basketball { }"
    println(CPSTextDSL.parse(p))
  }
}