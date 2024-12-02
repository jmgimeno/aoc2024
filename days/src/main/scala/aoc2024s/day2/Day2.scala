
package aoc2024s.day2;

import utils.IO

import scala.collection.IndexedSeqView
import scala.jdk.CollectionConverters.*

object Day2 {

  case class Interval(min: Int, max: Int) {
    def contains(value: Int): Boolean = min <= value && value <= max
  }

  val INCREASING: Interval = Interval(1, 3)
  val DECREASING: Interval = Interval(-3, -1)

  case class Report(levels: IndexedSeq[Int]) {
    def isValid: Boolean =
      containsAllDifferences(levels, INCREASING)
        || containsAllDifferences(levels, DECREASING)

    private def containsAllDifferences(levels: IndexedSeq[Int], interval: Interval): Boolean =
      levels.sliding(2).forall(pair => interval.contains(pair(1) - pair(0)))

    def isValidWithHoles: Boolean =
      isValid || levels.indices.exists(pos => Report(HoledSeq(pos, levels)).isValid)
  }

  object Report {
    def parse(line: String): Report = {
      val levels = line.split(" ").map(_.toInt).toVector
      Report(levels)
    }
  }

  def part1(data: List[String]): Long =
    data.map(Report.parse).count(_.isValid)

  private class HoledSeq[A](hole: Int, elems: IndexedSeq[A]) extends IndexedSeq[A] {
    def apply(idx: Int): A =
      if (idx < hole) elems(idx)
      else elems(idx + 1)

    def length: Int =
      elems.length - 1
  }

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