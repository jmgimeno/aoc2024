
package aoc2024s.day12

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day12Suite extends FunSuite {

  val example1: String =
    """AAAA
      |BBCD
      |BBCC
      |EEEC""".stripMargin

  val example2: String =
    """OOOOO
      |OXOXO
      |OOOOO
      |OXOXO
      |OOOOO""".stripMargin

  val example3: String =
    """RRRRIICCFF
      |RRRRIICCCF
      |VVRRRCCFFF
      |VVRCCCJFFF
      |VVVVCJJCFE
      |VVIVCCJJEE
      |VVIIICJJEE
      |MIIIIIJJEE
      |MIIISIJEEE
      |MMMISSJEEE""".stripMargin

  val example4: String =
    """EEEEE
      |EXXXX
      |EEEEE
      |EXXXX
      |EEEEE""".stripMargin

  val example5: String =
    """AAAAAA
      |AAABBA
      |AAABBA
      |ABBAAA
      |ABBAAA
      |AAAAAA""".stripMargin

  test("part1 - example1 data") {
    val data = IO.splitLinesAsList(example1).asScala.toList
    assertEquals(Day12.part1(data), 140L)
  }

  test("part1 - example2 data") {
    val data = IO.splitLinesAsList(example2).asScala.toList
    assertEquals(Day12.part1(data), 772L)
  }

  test("part1 - example3 data") {
    val data = IO.splitLinesAsList(example3).asScala.toList
    assertEquals(Day12.part1(data), 1930L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day12.txt").asScala.toList
    assertEquals(Day12.part1(data), 1549354L)
  }

  test("part2 - example1 data") {
    val data = IO.splitLinesAsList(example1).asScala.toList
    assertEquals(Day12.part2(data), 80L)
  }

  test("part2 - example2 data") {
    val data = IO.splitLinesAsList(example2).asScala.toList
    assertEquals(Day12.part2(data), 436L)
  }

  test("part2 - example3 data") {
    val data = IO.splitLinesAsList(example3).asScala.toList
    assertEquals(Day12.part2(data), 1206L)
  }

  test("part2 - example4 data") {
    val data = IO.splitLinesAsList(example4).asScala.toList
    assertEquals(Day12.part2(data), 236L)
  }

  test("part2 - example5 data") {
    val data = IO.splitLinesAsList(example5).asScala.toList
    assertEquals(Day12.part2(data), 368L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day12.txt").asScala.toList
    assertEquals(Day12.part2(data), 937032L)
  }
}