
package aoc2024s.day9

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day9Suite extends FunSuite {

  val exampleA: String = "12345"
  val exampleB: String = "2333133121414131402"

  test("part1 - exampleA data") {
    val data = IO.splitLinesAsList(exampleA).asScala.toList
    assertEquals(Day9.part1(data), 60L)
  }

  test("part1 - exampleB data") {
    val data = IO.splitLinesAsList(exampleB).asScala.toList
    assertEquals(Day9.part1(data), 1928L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day9.txt").asScala.toList
    assertEquals(Day9.part1(data), 6430446922192L)
  }

  test("part2 - exampleB data") {
    val data = IO.splitLinesAsList(exampleB).asScala.toList
    assertEquals(Day9.part2(data), 2858L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day9.txt").asScala.toList
    assertEquals(Day9.part2(data), 6460170593016L)
  }
}