
package aoc2024s.day21

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day21Suite extends FunSuite {

  val example: String = """029A
                          |980A
                          |179A
                          |456A
                          |379A""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day21.part1(data), 126384L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day21.txt").asScala.toList
    assertEquals(Day21.part1(data), 176452L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day21.part2(data), -1L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day21.txt").asScala.toList
    assertEquals(Day21.part2(data), -1L)
  }

  test("part1 - inputs for 029A") {
    val num = new Day21.NumericalKeyPad
    assertEquals(num.inputs("029A"), Set("<A^A>^^AvvvA", "<A^A^>^AvvvA", "<A^A^^>AvvvA"))
  }

  test("part1 - input for <A^A>^^AvvvA") {
    val dir = new Day21.DirectionalKeyPad
    assertEquals(dir.inputs("<A^A>^^AvvvA").contains("v<<A>>^A<A>AvA<^AA>A<vAAA>^A"), true)
  }
}
