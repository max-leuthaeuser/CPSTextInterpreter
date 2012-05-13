package de.qualitune.util

/**
 * Provides some list utils like calculating permutations or combinations.
 * If this projects gets updated to Scala 2.9.x one could use the
 * build-in list operations from the library instead.
 *
 * From: http://stackoverflow.com/questions/4596724/faster-permutation-generator
 *
 * @author Max Leuthaeuser
 * @since 15.04.12
 */
object ListUtils {
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
    var li = List[T]()
    (1 to n).foreach(_ => li ++= l)
    comb1(n, li).distinct
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
