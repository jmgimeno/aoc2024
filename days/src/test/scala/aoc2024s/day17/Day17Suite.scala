
package aoc2024s.day17

import aoc2024s.day17.Day17.{Computer, Executor}
import utils.IO

import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day17Suite extends FunSuite {

  val part1: String =
    """Register A: 729
      |Register B: 0
      |Register C: 0
      |
      |Program: 0,1,5,4,3,0""".stripMargin

  val part2: String =
    """Register A: 2024
      |Register B: 0
      |Register C: 0
      |
      |Program: 0,3,5,4,3,0
      |""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(part1).asScala.toList
    assertEquals(Day17.part1(data), "4,6,3,5,6,3,5,2,1,0")
    assertEquals(Day17.part1Eager(data), "4,6,3,5,6,3,5,2,1,0")
    assertEquals(Day17.part1Lazy(data), "4,6,3,5,6,3,5,2,1,0")
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day17.txt").asScala.toList
    assertEquals(Day17.part1(data), "3,7,1,7,2,1,0,6,3")
    assertEquals(Day17.part1Eager(data), "3,7,1,7,2,1,0,6,3")
    assertEquals(Day17.part1Lazy(data), "3,7,1,7,2,1,0,6,3")
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(part2).asScala.toList
    assertEquals(Day17.part2(data), 117440)
  }


  test("part2 - input data".ignore) {
    val data = IO.getResourceAsList("aoc2024/day17.txt").asScala.toList
    assertEquals(Day17.part2(data), -1)
  }

  test("If register C contains 9, the program 2,6 would set register B to 1.") {
    val machine = Computer(0, 0, 9, 0, List.empty)
    val program = List(2, 6)
    val result = Executor.run(machine, program)
    assertEquals(result.b.toInt, 1)
  }

  test("If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.") {
    val machine = Computer(10, 0, 0, 0, List.empty)
    val program = List(5, 0, 5, 1, 5, 4)
    val result = Executor.run(machine, program)
    assertEquals(result.output, List(0, 1, 2))
  }

  test("If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.") {
    val machine = Computer(2024, 0, 0, 0, List.empty)
    val program = List(0, 1, 5, 4, 3, 0)
    val result = Executor.run(machine, program)
    assertEquals(result.output, List(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0))
    assertEquals(result.a.toInt, 0)
  }

  test("If register B contains 29, the program 1,7 would set register B to 26.") {
    val machine = Computer(0, 29, 0, 0, List.empty)
    val program = List(1, 7)
    val result = Executor.run(machine, program)
    assertEquals(result.b.toInt, 26)
  }

  test("If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.") {
    val machine = Computer(0, 2024, 43690, 0, List.empty)
    val program = List(4, 0)
    val result = Executor.run(machine, program)
    assertEquals(result.b.toInt, 44354)
  }

  test("part 1 - on example2") {
    val data = IO.splitLinesAsList(part2).asScala.toList
    val (machine, program) = Day17.parse(data)
    val modified = machine.copy(a = 117440)
    val result = Executor.run(modified, program)
    assertEquals(result.output, program)
  }
}
