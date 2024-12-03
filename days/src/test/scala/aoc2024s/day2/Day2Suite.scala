
package aoc2024s.day2

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day2Suite extends FunSuite {

  val example =
    """7 6 4 2 1
      |1 2 7 8 9
      |9 7 6 2 1
      |1 3 2 4 5
      |8 6 4 4 1
      |1 3 6 7 9""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day2.part1(data), 2L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day2.txt").asScala.toList
    assertEquals(Day2.part1(data), 402L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day2.part2(data), 4L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day2.txt").asScala.toList
    assertEquals(Day2.part2(data), 455L)
  }
}