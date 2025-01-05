
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

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day21.txt").asScala.toList
    assertEquals(Day21.part2(data), 218309335714068L)
  }
}
