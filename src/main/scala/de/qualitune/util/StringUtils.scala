package de.qualitune.util

import RegexUtils._
import scala.util.matching.Regex

private object RegexUtils {

  class RichRegex(underlying: Regex) {
    def matches(s: String): Boolean = underlying.pattern.matcher(s).matches
  }

  implicit def regexToRichRegex(r: Regex) = new RichRegex(r)
}

/**
 * Utility class which provides helper methods
 * to perform operations like replacing substring in Strings.
 *
 * @author Max Leuthaeuser
 * @since 05.05.12
 */
object StringUtils {
  /**
   * Replaces the given pattern if the defined prefix matches (even multiple occurrences).
   *
   * @param text the String where all the replacements should happen
   * @param prefix the prefix that has to match (must not be empty)
   * @param pattern the String to search for and replace if the prefix matches (must not be empty)
   * @param replacement replaces param pattern
   * @return the given text after applying all replacements. If pattern could not be found return text unmodified.
   */
  def replaceAllWithPrefix(text: String, prefix: String, pattern: String, replacement: String): String = {
    var r_text = text
    // ignore empty text
    if (text == "") return ""
    // empty prefix or pattern is not allowed.
    assert(prefix != "")
    assert(pattern != "")

    var working = r_text
    while (working != "") {
      // search the pattern
      val index = working.indexOf(pattern)
      index match {
        case -1 => return r_text
        case 0 => {
          r_text = replacement + working.substring(index + pattern.length)
        }
        case _ => {
          val m = working.substring(index - 1, index)
          // test if the prefix fits
          if (prefix.r matches m) {
            r_text = r_text.substring(0, index + (r_text.length - working.length)) + replacement + working.substring(index + pattern.length)
          }
        }
      }
      working = working.substring(index + pattern.length)
    }
    r_text
  }
}
