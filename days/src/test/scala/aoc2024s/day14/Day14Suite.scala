
package aoc2024s.day14

import aoc2024s.day14.Day14.Space
import utils.IO

import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day14Suite extends FunSuite {

  val example: String =
    """p=0,4 v=3,-3
      |p=6,3 v=-1,-3
      |p=10,3 v=-1,2
      |p=2,0 v=2,-1
      |p=0,0 v=1,3
      |p=3,0 v=-2,-2
      |p=7,6 v=-1,-3
      |p=3,0 v=-1,-2
      |p=9,3 v=2,3
      |p=7,3 v=-1,2
      |p=2,4 v=2,-3
      |p=9,5 v=-3,-3""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Space(11, 7).parse(data).run(100).safety, 12)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day14.txt").asScala.toList
    assertEquals(Day14.part1(data), 216772608)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day14.txt").asScala.toList
    assertEquals(Day14.part2(data), 6888)
  }
}
