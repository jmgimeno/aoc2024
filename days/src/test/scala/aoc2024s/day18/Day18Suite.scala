
package aoc2024s.day18

import aoc2024s.day18.Day18.Position
import utils.IO

import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day18Suite extends FunSuite {

  val example: String = """5,4
                          |4,2
                          |4,5
                          |3,0
                          |2,1
                          |6,3
                          |2,4
                          |1,5
                          |0,6
                          |3,3
                          |2,6
                          |5,1
                          |1,2
                          |5,5
                          |2,5
                          |6,5
                          |1,4
                          |0,4
                          |6,4
                          |1,1
                          |6,1
                          |1,0
                          |0,5
                          |1,6
                          |2,0""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day18.part1(6, 12, data), 22)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day18.txt").asScala.toList
    assertEquals(Day18.part1(70, 1024, data), 334)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day18.part2(data), Position(6,1))
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day18.txt").asScala.toList
    assertEquals(Day18.part2(data), null)
  }
}
