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

  test(
    "for secret number 123L the max value on a sequence of 10 prices should be 6"
  ) {
    assertEquals(Optimizer(List(123L), 10).max, 6)
  }

  test("masks and indices for 123L") {
    val prices = Sequence(123L).prices(10)
    val changes = Changes(prices)
    val expectedMasks = // indices from -9 to +9
      List(0, 0, 0, 0, 0, 0, 1, 320, 12, 144, 0, 32, 0, 0, 0, 2, 0, 0, 0)
        .map(BigInt(_))
    assertEquals(changes.masks.toList, expectedMasks)

    for (diff <- List(-9, -8, -7, -6, -5, -4, 1, 3, 4, 5, 7, 8, 9)) {
      assertEquals(changes.indices(changes.masks(diff + 9)), List.empty)
    }
    assertEquals(changes.indices(changes.masks(-3 + 9)), List(0))
    assertEquals(changes.indices(changes.masks(-2 + 9)), List(6, 8))
    assertEquals(changes.indices(changes.masks(-1 + 9)), List(2, 3))
    assertEquals(changes.indices(changes.masks(0 + 9)), List(4, 7))
    assertEquals(changes.indices(changes.masks(2 + 9)), List(5))
    assertEquals(changes.indices(changes.masks(6 + 9)), List(1))
  }

  test("for secret number 123L, evaluation of some patterns") {
    val prices = Sequence(123L).prices(10)
    val changes = Changes(prices)
    val optimizer = Optimizer(List(123L), 10)
    assertEquals(changes.evaluate(Pattern.Four(-3, 6, -1, -1)), 4)
    assertEquals(changes.evaluate(Pattern.Four(6, -1, -1, 0)), 4)
    assertEquals(changes.evaluate(Pattern.Four(-1, -1, 0, 2)), 6)
    assertEquals(changes.evaluate(Pattern.Four(-1, 0, 2, -2)), 4)
    assertEquals(changes.evaluate(Pattern.Four(0, 2, -2, 0)), 4)
    assertEquals(changes.evaluate(Pattern.Four(2, -2, 0, -2)), 2)
  }

  test("for secret number 123, bound of One patterns") {
    val prices = Sequence(123L).prices(10)
    val changes = Changes(prices)
    val optimizer = Optimizer(List(123L), 10)
    assertEquals(optimizer.maxBound(Pattern.One(-9)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(-8)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(-7)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(-6)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(-5)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(-4)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(-3)), 4)
    assertEquals(optimizer.maxBound(Pattern.One(-2)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(-1)), 6)
    assertEquals(optimizer.maxBound(Pattern.One(0)), 4)
    assertEquals(optimizer.maxBound(Pattern.One(1)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(2)), 2)
    assertEquals(optimizer.maxBound(Pattern.One(3)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(4)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(5)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(6)), 4)
    assertEquals(optimizer.maxBound(Pattern.One(7)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(8)), 0)
    assertEquals(optimizer.maxBound(Pattern.One(9)), 0)
  }
}
