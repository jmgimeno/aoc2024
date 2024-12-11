
package aoc2024s.day11;

import utils.IO

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

object Day11 {

  case class Stones(stones: Map[Long, Long]) {

    def this(data: String) =
      this(data
        .split(" ")
        .map(_.toLong)
        .groupBy(identity)
        .map { case (value, values) =>
          value -> values.length.toLong
        })

    def numStones: Long = stones.values.sum

    def blink(steps: Int): Stones = {
      (1 to steps).foldLeft(this) { case (acc, _) =>
        acc.oneBlink
      }
    }

    def oneBlink: Stones = {
      val newCounters =
        stones.toSeq.flatMap { case (value, counter) =>
          blinkValue(value).map {
            _ -> counter
          }
        }.groupBy(_._1).map { case (value, pairs) =>
          value -> pairs.map(_._2).sum
        }

      Stones(newCounters)
    }

    def numberOfDigits(value: Long): Int = {
      @tailrec
      def go(value: Long, acc: Int): Int =
        if value < 10 then acc
        else go(value / 10, acc + 1)
      go(value, 1)
    }

    def pow10(exp: Int): Long = {
      (1 to exp)
        .foldLeft(1L) { case (acc, _) =>
          acc * 10
        }
    }

    def blinkValue(value: Long): Seq[Long] =
      if value == 0 then Seq(1)
      else
        val n = numberOfDigits(value)
        if n % 2 == 0 then
          val div = pow10(n / 2)
          val left = value / div
          val right = value % div
          Seq(left, right)
        else
          Seq(value * 2024)
  }

  def part1(data: List[String]): Long = {
    val stones = new Stones(data.head)
    val result = stones.blink(25)
    result.numStones
  }

  def part2(data: List[String]): Long = {
    val stones = new Stones(data.head)
    val result = stones.blink(75)
    result.numStones
  }

  @main def main11(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day11.txt").asScala.toList;
    val part1 = Day11.part1(data);
    println(s"part1 = $part1");
    val part2 = Day11.part2(data);
    println(s"part2 = $part2");
  }
}