
package aoc2024s.day13

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day13Suite extends FunSuite {

  val example: String = """Button A: X+94, Y+34
                          |Button B: X+22, Y+67
                          |Prize: X=8400, Y=5400
                          |
                          |Button A: X+26, Y+66
                          |Button B: X+67, Y+21
                          |Prize: X=12748, Y=12176
                          |
                          |Button A: X+17, Y+86
                          |Button B: X+84, Y+37
                          |Prize: X=7870, Y=6450
                          |
                          |Button A: X+69, Y+23
                          |Button B: X+27, Y+71
                          |Prize: X=18641, Y=10279""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day13.part1(data), 480L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day13.txt").asScala.toList
    assertEquals(Day13.part1(data), 27105L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day13.part2(data), BigInt("875318608908"))
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day13.txt").asScala.toList
    assertEquals(Day13.part2(data), BigInt("101726882250942"))
  }
}