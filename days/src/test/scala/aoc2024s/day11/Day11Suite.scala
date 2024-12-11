
package aoc2024s.day11

import aoc2024s.day11.Day11.Stones
import utils.IO

import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day11Suite extends FunSuite {

  val example: String = "125 17"

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day11.part1(data), 55312L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day11.txt").asScala.toList
    assertEquals(Day11.part1(data), 183620L)
  }

  test("part2 - example data") { // Not provided
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day11.part2(data), 65601038650482L)
  }

  test("example - one blink") {
    val stones = new Stones(example)
    val result = stones.blink(1)
    assertEquals(result.numStones, 3L)
  }

  test("example - two blinks") {
    val stones = new Stones(example)
    val result = stones.blink(2)
    assertEquals(result.numStones, 4L)
  }

  test("example - three blinks") {
    val stones = new Stones(example)
    val result = stones.blink(3)
    assertEquals(result.numStones, 5L)
  }

  test("example - four blinks") {
    val stones = new Stones(example)
    val result = stones.blink(4)
    assertEquals(result.numStones, 9L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day11.txt").asScala.toList
    assertEquals(Day11.part2(data), 220377651399268L)
  }
}