import de.qualitune.util.ListUtils
import org.scalatest.FunSuite

class ListUtilTests extends FunSuite {
  test("Testing permutations") {
    val l1 = List()
    val l2 = List("a")
    val l3 = List("a", "b")
    val l4 = List("a", "b", "c")

    assert(ListUtils.permute(l1) === List(Nil))
    assert(ListUtils.permute(l2) === List(List("a")))
    assert(ListUtils.permute(l3) === List(List("a", "b"), List("b", "a")))
    assert(ListUtils.permute(l4) === List(
      List("a", "b", "c"),
      List("a", "c", "b"),
      List("b", "a", "c"),
      List("b", "c", "a"),
      List("c", "a", "b"),
      List("c", "b", "a"))
    )
  }
}