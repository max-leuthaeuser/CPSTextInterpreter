import de.qualitune.util.StringUtils
import org.scalatest.FunSuite

/**
 * @author Max Leuthaeuser
 * @since 05.05.12
 */
class StringUtilTests extends FunSuite {
  test("Testing replaceAllWithPrefix") {
    // single occurrences
    val t1 = "a.bla()"
    val t2 = "!a.bla()"
    val t3 = "xyz.bla()|a.bla()"
    val t4 = "xyz.bla()||a.bla()"
    val t5 = "xyz.bla()&a.bla()"
    val t6 = "xyz.bla()&&a.bla()"

    val e1 = "b.bla()"
    val e2 = "!b.bla()"
    val e3 = "xyz.bla()|b.bla()"
    val e4 = "xyz.bla()||b.bla()"
    val e5 = "xyz.bla()&b.bla()"
    val e6 = "xyz.bla()&&b.bla()"

    // multiple occurrences
    val t7 = "a.bla()|a.pla()"
    val t8 = "!a.bla()|!a.pla()"
    val t9 = "a.bla()|xyz.bla()|a.bla()"
    val t10 = "xyz.bla()||a.bla()||a.bla()"
    val t11 = "xyz.bla()&a.bla()&a.bla()"
    val t12 = "xyz.bla()&&a.bla()&&a.bla()"

    val e7 = "b.bla()|b.pla()"
    val e8 = "!b.bla()|!b.pla()"
    val e9 = "b.bla()|xyz.bla()|b.bla()"
    val e10 = "xyz.bla()||b.bla()||b.bla()"
    val e11 = "xyz.bla()&b.bla()&b.bla()"
    val e12 = "xyz.bla()&&b.bla()&&b.bla()"

    // opening bracket
    val t13 = "(a.bla()&bla())"
    val e13 = "(b.bla()&bla())"
    val t14 = "(a.bla()&bla()||a.pla())"
    val e14 = "(b.bla()&bla()||b.pla())"

    // parameters
    val prefix = "(\\{|!|\\||&|\\()"
    val pattern = "a."
    val replacment = "b."

    assert(e1 === StringUtils.replaceAllWithPrefix(t1, prefix, pattern, replacment))
    assert(e2 === StringUtils.replaceAllWithPrefix(t2, prefix, pattern, replacment))
    assert(e3 === StringUtils.replaceAllWithPrefix(t3, prefix, pattern, replacment))
    assert(e4 === StringUtils.replaceAllWithPrefix(t4, prefix, pattern, replacment))
    assert(e5 === StringUtils.replaceAllWithPrefix(t5, prefix, pattern, replacment))
    assert(e6 === StringUtils.replaceAllWithPrefix(t6, prefix, pattern, replacment))
    assert(e7 === StringUtils.replaceAllWithPrefix(t7, prefix, pattern, replacment))
    assert(e8 === StringUtils.replaceAllWithPrefix(t8, prefix, pattern, replacment))
    assert(e9 === StringUtils.replaceAllWithPrefix(t9, prefix, pattern, replacment))
    assert(e10 === StringUtils.replaceAllWithPrefix(t10, prefix, pattern, replacment))
    assert(e11 === StringUtils.replaceAllWithPrefix(t11, prefix, pattern, replacment))
    assert(e12 === StringUtils.replaceAllWithPrefix(t12, prefix, pattern, replacment))
    assert(e13 === StringUtils.replaceAllWithPrefix(t13, prefix, pattern, replacment))
    assert(e14 === StringUtils.replaceAllWithPrefix(t14, prefix, pattern, replacment))
  }
}
