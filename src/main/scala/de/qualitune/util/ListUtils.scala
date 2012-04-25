package de.qualitune.util

/**
 * Provides some list utils like calculating permutations or combinations.
 * If this projects gets updated to Scala 2.9.x one could use the
 * build-in list operations from the library instead.
 *
 * From: http://stackoverflow.com/questions/4596724/faster-permutation-generator
 *
 * User: Max Leuthaeuser
 * Date: 15.04.12
 */
object ListUtils {
  /**For each element x in List xss, returns (x, xss - x) */
  def selections[A](xss: List[A]): List[(A, List[A])] = xss match {
    case Nil => Nil
    case x :: xs =>
      (x, xs) :: (for ((y, ys) <- selections(xs))
      yield (y, x :: ys))
  }

  /**Returns a list containing all permutations of the input list */
  def permute[A](xs: List[A]): List[List[A]] = xs match {
    case Nil => List(Nil)

    //special case lists of length 1 and 2 for better performance
    case t :: Nil => List(xs)
    case t :: u :: Nil => List(xs, List(u, t))

    case _ =>
      for ((y, ys) <- selections(xs); ps <- permute(ys))
      yield y :: ps
  }

  /**Calculates the Cartesian product for two lists. */
  def pair[A](a: List[A], b: List[A]) = a.flatMap(_a => b.map(_b => _a -> _b))

  /**Calculates combinations out of list l with length n. */
  def combinations[T](n: Int, l: List[T]): List[List[T]] = {
    def comb1[E](n: Int, l: List[E]): List[List[E]] =
      n match {
        case 0 => List(List())
        case _ => for (i <- (0 to (l.size - n)).toList;
                       l1 = l.drop(i);
                       sl <- combinations(n - 1, l1.tail))
        yield l1.head :: sl
      }
    comb1(n, l).distinct
  }

  /**
   * For a list of a unknown number of lists, of different length, and for maybe
   * different types, you can use this to get the Cartesian product.
   */
  def xproduct(xx: List[List[_]]): List[List[_]] =
    xx match {
      case aa :: bb :: Nil =>
        aa.map(a => bb.map(b => List(a, b))).flatten
      case aa :: bb :: cc =>
        xproduct(bb :: cc).map(li => aa.map(a => a :: li)).flatten
      case _ => xx
    }
}
