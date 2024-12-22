
package aoc2024s.day22

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day22Suite extends FunSuite {

  val example: String = """1
                          |10
                          |100
                          |2024""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day22.part1(data), 37327623L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    assertEquals(Day22.part1(data), 18317943467L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day22.part2(data), -1L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    assertEquals(Day22.part2(data), -1L)
  }
}
