import parser.{CPSTextDSL}
import scala.io.Source._

object Example {
  def main(args: Array[String]) {
    val p = fromFile(args(0)).mkString
    println(CPSTextDSL.parse(p))
  }
}