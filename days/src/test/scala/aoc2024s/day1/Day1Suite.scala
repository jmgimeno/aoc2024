
package aoc2024s.day1

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day1Suite extends FunSuite {

  val example =
    """3   4
      |4   3
      |2   5
      |1   3
      |3   9
      |3   3""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day1.part1(data), 11L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day1.txt").asScala.toList
    assertEquals(Day1.part1(data), 1197984L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day1.part2(data), 31L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day1.txt").asScala.toList
    assertEquals(Day1.part2(data), 23387399L)
  }
}