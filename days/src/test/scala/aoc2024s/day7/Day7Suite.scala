
package aoc2024s.day7

import munit.FunSuite
import utils.IO

import scala.jdk.CollectionConverters.*

class Day7Suite extends FunSuite {

  val example: String = """190: 10 19
                          |3267: 81 40 27
                          |83: 17 5
                          |156: 15 6
                          |7290: 6 8 6 15
                          |161011: 16 10 13
                          |192: 17 8 14
                          |21037: 9 7 18 13
                          |292: 11 6 16 20""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day7.part1(data), 3749L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day7.txt").asScala.toList
    assertEquals(Day7.part1(data), 267566105056L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day7.part2(data), 11387L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day7.txt").asScala.toList
    assertEquals(Day7.part2(data), 116094961956019L)
  }
}
