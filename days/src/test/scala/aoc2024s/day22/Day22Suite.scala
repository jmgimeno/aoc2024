package aoc2024s.day22

import utils.IO

import scala.jdk.CollectionConverters.*
import munit.FunSuite

import scala.concurrent.duration.{Duration, FiniteDuration}

class Day22Suite extends FunSuite {

  override val munitTimeout: FiniteDuration = Duration(5, "m")

  val example1: String = """1
                          |10
                          |100
                          |2024""".stripMargin

  val example2: String = """1
                          |2
                          |3
                          |2024""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example1).asScala.toList
    assertEquals(Day22.part1(data), 37327623L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    assertEquals(Day22.part1(data), 18317943467L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example2).asScala.toList
    assertEquals(Day22.part2(data), 23)
  }

  test("part2 - input data") { // too slow
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    assertEquals(Day22.part2(data), 2018)
  }
}
