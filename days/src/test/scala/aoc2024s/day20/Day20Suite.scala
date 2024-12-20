package aoc2024s.day20

import aoc2024s.day20.Day20.CheatingHistory.NoCheating
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

  test("part1 - finds optimal path with no cheats") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day20.allPaths(data, canCheat = false, findMany = false).head.f, 84)
  }

  test("part1 - finds optimal path with cheats (should be maximum saving)") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day20.allPaths(data, canCheat = true, findMany = false).head.f, 20)
  }

  test("part1 - classify savings") {
    val data = IO.splitLinesAsList(example).asScala.toList
    val withCheating = Day20.allPaths(data, canCheat = true, findMany = true)
    val noCheating = withCheating
      .filter(n => n.cheatingHistory == NoCheating)
      .minBy(_.f)
      .f
    val savings = withCheating
      .filter(n => n.cheatingHistory != NoCheating)
      .filter(n => n.f < noCheating)
      .groupMapReduce(node => noCheating - node.f)(_ => 1)(_ + _)
    val expected =
      Map(
        2 -> 14,
        4 -> 14,
        6 -> 2,
        8 -> 4,
        10 -> 2,
        12 -> 3,
        20 -> 1,
        36 -> 1,
        38 -> 1,
        40 -> 1,
        64 -> 1
      )
    assertEquals(savings, expected)
  }

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day20.part1(data, 100), 0)
    assertEquals(Day20.part1(data, 65), 0)
    assertEquals(Day20.part1(data, 64), 1)
    assertEquals(Day20.part1(data, 11), 8)
    assertEquals(Day20.part1(data, 1), 44)
  }

  test("part1 - input data".ignore) {
    val data = IO.getResourceAsList("aoc2024/day20.txt").asScala.toList
    assertEquals(Day20.part1(data, 900), -1)
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
