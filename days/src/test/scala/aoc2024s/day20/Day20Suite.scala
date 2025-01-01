package aoc2024s.day20

import utils.IO

import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day20Suite extends FunSuite {

  val example: String = """###############
                          |#...#...#.....#
                          |#.#.#.#.#.###.#
                          |#S#...#.#.#...#
                          |#######.#.#.###
                          |#######.#.#...#
                          |#######.#.###.#
                          |###..E#...#...#
                          |###.#######.###
                          |#...###...#...#
                          |#.#####.#.###.#
                          |#.#...#.#.#...#
                          |#.#.#.#.#.#.###
                          |#...#...#...###
                          |###############""".stripMargin


  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day20.part1(data, 1), 44)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day20.txt").asScala.toList
    assertEquals(Day20.part1(data, 100), 1375)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day20.part2(data), -1L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day20.txt").asScala.toList
    assertEquals(Day20.part2(data), -1L)
  }
}
