
package aoc2024s.day22

import aoc2024s.day22.Day22.{Changes, Optimizer, Pattern, Sequence}
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

  test("part2 - input data".ignore) {
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    assertEquals(Day22.part2(data), -1)
  }

  test("for secret number 123 and changes -1, -1. 0, 2, price should be 6") {
    val prices = Sequence(123L).prices(2001)
    val changes = Changes(prices)
    val pattern: Pattern.Four = Pattern.Four(-1, -1, 0, 2)
    assertEquals(changes.evaluate(pattern), 6)
  }

  test("for secret number 1 and changes -2, 1. -1, 3, price should be 7") {
    val prices = Sequence(1L).prices(2001)
    val changes = Changes(prices)
    val pattern: Pattern.Four = Pattern.Four(-2, 1, -1, 3)
    assertEquals(changes.evaluate(pattern), 7)
  }

  test("for secret number 2 and changes -2, 1. -1, 3, price should be 7") {
    val prices = Sequence(2L).prices(2001)
    val changes = Changes(prices)
    val pattern: Pattern.Four = Pattern.Four(-2, 1, -1, 3)
    assertEquals(changes.evaluate(pattern), 7)
  }

  test("for secret number 3 and changes -2, 1. -1, 3, price should be 0") {
    val prices = Sequence(3L).prices(2001)
    val changes = Changes(prices)
    val pattern: Pattern.Four = Pattern.Four(-2, 1, -1, 3)
    assertEquals(changes.evaluate(pattern), 0)
  }

  test("for secret number 2024 and changes -2, 1. -1, 3, price should be 9") {
    val prices = Sequence(2024L).prices(2001)
    val changes = Changes(prices)
    val pattern: Pattern.Four = Pattern.Four(-2, 1, -1, 3)
    assertEquals(changes.evaluate(pattern), 9)
  }

  test("for example 2 the evaluation of -2, 1, -1, 3 is 23") {
    val data = IO.splitLinesAsList(example2).asScala.toList
    val parsed = Day22.parse(data)
    val optimizer = Optimizer(parsed)
    val pattern: Pattern.Four = Pattern.Four(-2, 1, -1, 3)
    assertEquals(optimizer.evaluate(pattern), 23)
  }

  test("for secret number 123L the max value on a sequence of 10 prices should be 6") {
    assertEquals(Optimizer(List(123L), 10).max, 6)
  }

  test("masks for 123L") {
    val prices = Sequence(123L).prices(10)
    val changes = Changes(prices)
    changes.masks.indices.foreach( i =>
      println(s"${i -9} -> ${changes.masks(i)} -> ${changes.ones(changes.masks(i))}")
    )
  }
}
