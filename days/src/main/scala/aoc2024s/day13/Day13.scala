
package aoc2024s.day13;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day13 {

  object Parser {

    private val buttonRegexp = """Button [A-Z]: X\+(\d+), Y\+(\d+)""".r
    private val prizeRegexp = """Prize: X=(\d+), Y=(\d+)""".r

    def parse(data: List[String]): List[Machine] = {
      data.filterNot(_.isEmpty).grouped(3).map(parseMachine).toList
    }

    def parseMachine(data: List[String]): Machine = {
      val matchA = buttonRegexp.findFirstMatchIn(data(0)).get
      val matchB = buttonRegexp.findFirstMatchIn(data(1)).get
      val matchPrize = prizeRegexp.findFirstMatchIn(data(2)).get
      val a = Button(matchA.group(1).toLong, matchA.group(2).toLong)
      val b = Button(matchB.group(1).toLong, matchB.group(2).toLong)
      val prize = Prize(matchPrize.group(1).toLong, matchPrize.group(2).toLong)
      Machine(a, b, prize)
    }
  }

  case class Button(dx: Long, dy: Long) {}

  case class Prize(x: Long, y: Long) {}

  case class Machine(a: Button, b: Button, prize: Prize) {

    private inline def determinant(m00: Long, m01: Long, m10: Long, m11: Long): Long = {
      m00 * m11 - m01 * m10
    }

    private val delta = determinant(a.dx, b.dx, a.dy, b.dy)

    def execute: Execution = {
      if (delta == 0) {
        Execution(0, 0)
      } else {
        val detA = determinant(prize.x, b.dx, prize.y, b.dy)
        val detB = determinant(a.dx, prize.x, a.dy, prize.y)
        if (detA % delta != 0 || detB % delta != 0) {
          Execution(0, 0)
        } else {
          val pushA = detA / delta
          val pushB = detB / delta
          // must be in range [0, 100]
          if (pushA < 0 || pushA > 100 || pushB < 0 || pushB > 100) {
            Execution(0, 0)
          } else {
            val tokens = 3 * pushA + pushB
            Execution(1, tokens)
          }
        }
      }
    }
  }

  case class Execution(prizes: Long, tokens: Long) {}

  def totalTokens(machines: List[Machine]): Long = {
    machines.map(_.execute).map(_.tokens).sum
  }

  def part1(data: List[String]): Long = {
    val machines = Parser.parse(data);
    println(machines)
    totalTokens(machines)
  }

  def part2(data: List[String]): Long = {
    throw new UnsupportedOperationException("part2");
  }

  @main def main13(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day13.txt").asScala.toList;
    val part1 = Day13.part1(data);
    println(s"part1 = $part1");
    val part2 = Day13.part2(data);
    println(s"part2 = $part2");
  }
}