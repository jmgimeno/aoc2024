
package aoc2024s.day25

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day25Suite extends FunSuite {

  val example: String = """#####
                          |.####
                          |.####
                          |.####
                          |.#.#.
                          |.#...
                          |.....
                          |
                          |#####
                          |##.##
                          |.#.##
                          |...##
                          |...#.
                          |...#.
                          |.....
                          |
                          |.....
                          |#....
                          |#....
                          |#...#
                          |#.#.#
                          |#.###
                          |#####
                          |
                          |.....
                          |.....
                          |#.#..
                          |###..
                          |###.#
                          |###.#
                          |#####
                          |
                          |.....
                          |.....
                          |.....
                          |#....
                          |#.#..
                          |#.#.#
                          |#####""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day25.part1(data), 3)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day25.txt").asScala.toList
    assertEquals(Day25.part1(data), 2854)
  }
}
