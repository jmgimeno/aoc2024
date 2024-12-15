
package aoc2024s.day15

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day15Suite extends FunSuite {

  val smaller: String =
    """########
      |#..O.O.#
      |##@.O..#
      |#...O..#
      |#.#.O..#
      |#...O..#
      |#......#
      |########
      |
      |<^^>>>vv<v>>v<<""".stripMargin

  val larger: String =
    """##########
      |#..O..O.O#
      |#......O.#
      |#.OO..O.O#
      |#..O@..O.#
      |#O#..O...#
      |#O..O..O.#
      |#.OO.O.OO#
      |#....O...#
      |##########
      |
      |<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
      |vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
      |><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
      |<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
      |^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
      |^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
      |>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
      |<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
      |^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
      |v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^""".stripMargin

  val smallest: String =
    """#######
      |#...#.#
      |#.....#
      |#..OO@#
      |#..O..#
      |#.....#
      |#######
      |
      |<vv<<^^<<^^""".stripMargin

  test("part1 - smaller data") {
    val data = IO.splitLinesAsList(smaller).asScala.toList
    assertEquals(Day15.part1(data), 2028L)
  }

  test("part1 - larger data") {
    val data = IO.splitLinesAsList(larger).asScala.toList
    assertEquals(Day15.part1(data), 10092L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day15.txt").asScala.toList
    assertEquals(Day15.part1(data), 1406628L)
  }

  test("part2 - smallest data") {
    val data = IO.splitLinesAsList(smallest).asScala.toList
    assertEquals(Day15.part2(data), 618L)
  }

  test("part2 - larger data") {
    val data = IO.splitLinesAsList(larger).asScala.toList
    assertEquals(Day15.part2(data), 9021L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day15.txt").asScala.toList
    assertEquals(Day15.part2(data), -1L)
  }
}
