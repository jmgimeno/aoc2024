
package aoc2024s.day6

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day6Suite extends FunSuite {

  val example: String =
    """....#.....
      |.........#
      |..........
      |..#.......
      |.......#..
      |..........
      |.#..^.....
      |........#.
      |#.........
      |......#...""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day6.part1(data), 41L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day6.txt").asScala.toList
    assertEquals(Day6.part1(data), 5086L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day6.part2(data), 6L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day6.txt").asScala.toList
    assertEquals(Day6.part2(data), 1770L)
  }
}