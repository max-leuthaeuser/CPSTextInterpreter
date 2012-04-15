package de.qualitune.util

/**
 * Provides some list utils like calculating permutation.
 * If this projects gets updated to Scala 2.9.x one could use the
 * build-in list operations from the library instead.
 *
 * From: http://stackoverflow.com/questions/4596724/faster-permutation-generator
 *
 * User: Max Leuthaeuser
 * Date: 15.04.12
 */
object ListUtils {
  /** For each element x in List xss, returns (x, xss - x) */
  def selections[A](xss: List[A]): List[(A, List[A])] = xss match {
    case Nil => Nil
    case x :: xs =>
      (x, xs) :: (for ((y, ys) <- selections(xs))
      yield (y, x :: ys))
  }

  /** Returns a list containing all permutations of the input list */
  def permute[A](xs: List[A]): List[List[A]] = xs match {
    case Nil => List(Nil)

    //special case lists of length 1 and 2 for better performance
    case t :: Nil => List(xs)
    case t :: u :: Nil => List(xs, List(u, t))

    case _ =>
      for ((y, ys) <- selections(xs); ps <- permute(ys))
      yield y :: ps
  }
}
