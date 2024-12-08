
package aoc2024s.day8

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day8Suite extends FunSuite {

  val example: String = """............
                          |........0...
                          |.....0......
                          |.......0....
                          |....0.......
                          |......A.....
                          |............
                          |............
                          |........A...
                          |.........A..
                          |............
                          |............""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day8.part1(data), 14L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day8.txt").asScala.toList
    assertEquals(Day8.part1(data), 256L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day8.part2(data), 34L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day8.txt").asScala.toList
    assertEquals(Day8.part2(data), 1005L)
  }
}
