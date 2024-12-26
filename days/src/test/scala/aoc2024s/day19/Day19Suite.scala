package aoc2024s.day19

import munit.FunSuite
import utils.IO

import scala.jdk.CollectionConverters.*

class Day19Suite extends FunSuite {

  val example: String =
    """r, wr, b, g, bwu, rb, gb, br
      |
      |brwrr
      |bggr
      |gbbr
      |rrbgbr
      |ubwu
      |bwurrg
      |brgr
      |bbrgwb""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day19.part1(data), 6)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day19.txt").asScala.toList
    assertEquals(Day19.part1(data), 358)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day19.part2(data), 16L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day19.txt").asScala.toList
    assertEquals(Day19.part2(data), 600639829400603L)
  }
}
