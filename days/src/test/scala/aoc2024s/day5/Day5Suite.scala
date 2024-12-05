
package aoc2024s.day5

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day5Suite extends FunSuite {

  val example: String =
    """47|53
      |97|13
      |97|61
      |97|47
      |75|29
      |61|13
      |75|53
      |29|13
      |97|29
      |53|29
      |61|53
      |97|53
      |61|29
      |47|13
      |75|47
      |97|75
      |47|61
      |75|61
      |47|29
      |75|13
      |53|13
      |S
      |75,47,61,53,29
      |97,61,53,29,13
      |75,29,13
      |75,97,47,61,53
      |61,13,29
      |97,13,75,29,47""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day5.part1(data), 143L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day5.txt").asScala.toList
    assertEquals(Day5.part1(data), 5108L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day5.part2(data), 123L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day5.txt").asScala.toList
    assertEquals(Day5.part2(data), 7380L)
  }
}