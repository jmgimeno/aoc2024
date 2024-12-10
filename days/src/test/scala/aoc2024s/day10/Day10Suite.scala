
package aoc2024s.day10

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day10Suite extends FunSuite {

  val example: String = """89010123
                          |78121874
                          |87430965
                          |96549874
                          |45678903
                          |32019012
                          |01329801
                          |10456732""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day10.part1(data), 36L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day10.txt").asScala.toList
    assertEquals(Day10.part1(data), 582L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day10.part2(data), 81L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day10.txt").asScala.toList
    assertEquals(Day10.part2(data), 1302L)
  }
}
