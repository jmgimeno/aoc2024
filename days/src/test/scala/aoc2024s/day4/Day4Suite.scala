
package aoc2024s.day4

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day4Suite extends FunSuite {

  val example =
    """MMMSXXMASM
      |MSAMXMSMSA
      |AMXSXMAAMM
      |MSAMASMSMX
      |XMASAMXAMM
      |XXAMMXXAMA
      |SMSMSASXSS
      |SAXAMASAAA
      |MAMMMXMMMM
      |MXMXAXMASX""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day4.part1(data), 18L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day4.txt").asScala.toList
    assertEquals(Day4.part1(data), 2567L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day4.part2(data), 9L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day4.txt").asScala.toList
    assertEquals(Day4.part2(data), 2029L)
  }
}