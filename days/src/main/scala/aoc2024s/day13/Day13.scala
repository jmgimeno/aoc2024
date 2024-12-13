
package aoc2024s.day13;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day13 {

  extension (s: String) {
    def toBigInt: BigInt = BigInt(s)
  }

  object Parser {

    private val buttonRegexp = """Button [A-Z]: X\+(\d+), Y\+(\d+)""".r
    private val prizeRegexp = """Prize: X=(\d+), Y=(\d+)""".r

    def parse(data: List[String], increment: BigInt = BigInt(0)): List[Machine] = {
      data.filterNot(_.isEmpty).grouped(3).map(parseMachine(increment)).toList
    }

    def parseMachine(increment: BigInt)(data: List[String]): Machine = {
      val matchA = buttonRegexp.findFirstMatchIn(data(0)).get
      val matchB = buttonRegexp.findFirstMatchIn(data(1)).get
      val matchPrize = prizeRegexp.findFirstMatchIn(data(2)).get
      val a = Button(matchA.group(1).toBigInt, matchA.group(2).toBigInt)
      val b = Button(matchB.group(1).toBigInt, matchB.group(2).toBigInt)
      val incrementedX = increment + matchPrize.group(1).toBigInt
      val incrementedY = increment + matchPrize.group(2).toBigInt
      val prize = Prize(incrementedX, incrementedY)
      Machine(a, b, prize)
    }
  }

  case class Button(dx: BigInt, dy: BigInt)

  case class Prize(x: BigInt, y: BigInt)

  case class Machine(a: Button, b: Button, prize: Prize) {

    private inline def determinant(m00: BigInt, m01: BigInt, m10: BigInt, m11: BigInt): BigInt = {
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
          val tokens = 3 * pushA + pushB
          Execution(1, tokens)

        }
      }
    }
  }

  case class Execution(prizes: BigInt, tokens: BigInt) {}

  def totalTokens(machines: List[Machine]): BigInt = {
    machines.map(_.execute).map(_.tokens).sum
  }

  def part1(data: List[String]): Long = {
    val machines = Parser.parse(data);
    totalTokens(machines).toLong
  }

  def part2(data: List[String]): BigInt = {
    val machines = Parser.parse(data, BigInt("10000000000000"));
    totalTokens(machines)
  }

  @main def main13(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day13.txt").asScala.toList;
    val part1 = Day13.part1(data);
    println(s"part1 = $part1");
    val part2 = Day13.part2(data);
    println(s"part2 = $part2");
  }
}