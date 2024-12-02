
package aoc2024s.day2

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day2Suite extends FunSuite {

  val example = """7 6 4 2 1
                  |1 2 7 8 9
                  |9 7 6 2 1
                  |1 3 2 4 5
                  |8 6 4 4 1
                  |1 3 6 7 9""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(2L, Day2.part1(data))
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day2.txt").asScala.toList
    assertEquals(402L, Day2.part1(data))
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(4L, Day2.part2(data))
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day2.txt").asScala.toList
    assertEquals(455L, Day2.part2(data))
  }
}