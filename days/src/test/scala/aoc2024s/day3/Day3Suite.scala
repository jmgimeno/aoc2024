
package aoc2024s.day3

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day3Suite extends FunSuite {

  val example1 =
    """
    xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
    """

  val example2 =
    """
    xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
    """

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example1).asScala.toList
    assertEquals(Day3.part1(data), 161L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day3.txt").asScala.toList
    assertEquals(Day3.part1(data), 170807108L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example2).asScala.toList
    assertEquals(Day3.part2(data), 48L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day3.txt").asScala.toList
    assertEquals(Day3.part2(data), 74838033L)
  }
}