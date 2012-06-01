package de.qualitune.util

/**
 * Provides list util for calculating variations with repetition in O(n^k).
 *
 * @author Max Leuthaeuser
 * @since 15.04.12
 */
object ListUtils {
  /* Calculates combinations out of list l with length n. */
  def combinations[T](n: Int, l: List[T]): List[List[T]] =
    n match {
      case 0 => List(List())
      case _ => for (el <- l; sl <- combinations(n - 1, l))
      yield el :: sl
    }
}
