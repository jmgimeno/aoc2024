package aoc2024s.day22

import utils.IO

import scala.jdk.CollectionConverters.*

object Day22 {

  class Secrets(start: Long) {

    private val MODULUS = 16777216L - 1L

    private def prune(n: Long): Long = {
      n & MODULUS
    }

    private def next(n: Long): Long = {
      val step1 = prune((n << 6) ^ n)
      val step2 = prune((step1 >> 5) ^ step1)
      val step3 = prune((step2 << 11) ^ step2)
      step3
    }

    def nth(n: Int): Long = {
      Iterator.iterate(start)(next).drop(n).next()
    }

    def prices: Iterator[Int] = {
      Iterator.iterate(start)(next).map(l => (l % 10).toInt)
    }
  }

  class Optimizer(data: List[Long]) {
    private val size = 2001

    private val totals = Array.fill(19 * 19 * 19 * 19)(0)

    private def toIndex(d1: Int, d2: Int, d3: Int, d4: Int): Int = {
      (d1 + 9) * 19 * 19 * 19 + (d2 + 9) * 19 * 19 + (d3 + 9) * 19 + (d4 + 9)
    }

    def max: Int = {
      data.foreach(n => {
        val notSeen = Array.fill(19 * 19 * 19 * 19)(true)
        val prices = Secrets(n).prices.take(size).toList
        val changes = prices.sliding(2).map { group => group(1) - group(0) }
        val indexes = changes.sliding(4).map { group => toIndex(group(0), group(1), group(2), group(3)) }
        prices.drop(4).zip(indexes).foreach { case (change, index) =>
          if (notSeen(index)) {
            notSeen(index) = false
            totals(index) += change
          }
        }
      })
      totals.max
    }
  }

  def parse(data: List[String]): List[Long] = data.map(_.toLong)

  def part1(data: List[String]): Long = {
    parse(data).map(Secrets(_).nth(2000)).sum
  }

  def part2(data: List[String]): Int = {
    val parsed = parse(data)
    Optimizer(parsed).max
  }

  @main def main22(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    val part1 = Day22.part1(data)
    println(s"part1 = $part1")
    val part2 = Day22.part2(data)
    println(s"part2 = $part2")
  }
}
