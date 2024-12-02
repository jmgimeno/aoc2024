
package aoc2024s.day2;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day2 {

  case class Interval(min: Int, max: Int) {
    def contains(value: Int): Boolean = min <= value && value <= max
  }

  val INCREASING: Interval = Interval(1, 3)
  val DECREASING: Interval = Interval(-3, -1)

  case class Report(levels: List[Int]) {
    def isValid: Boolean =
      containsAllDifferences(levels, INCREASING)
        || containsAllDifferences(levels, DECREASING)

    private def containsAllDifferences(levels: List[Int], interval: Interval): Boolean =
      levels.sliding(2).forall {
        case List(a, b) => interval.contains(b - a)
      }

    private def makeHole(pos: Int): Report =
      Report(levels.take(pos) ++ levels.drop(pos + 1))

    def isValidWithHoles: Boolean =
      isValid || levels.indices.exists(makeHole(_).isValid)
  }

  object Report {
    def parse(line: String): Report = {
      val levels = line.split(" ").map(_.toInt).toList
      Report(levels)
    }
  }

  def part1(data: List[String]): Long =
    data.map(Report.parse).count(_.isValid)

  def part2(data: List[String]): Long = {
    data.map(Report.parse).count(_.isValidWithHoles)
  }

  @main def main2(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day2.txt").asScala.toList;
    val part1 = Day2.part1(data);
    println(s"part1 = $part1");
    val part2 = Day2.part2(data);
    println(s"part2 = $part2");
  }
}