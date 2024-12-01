
package aoc2024s.day1;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day1 {

  private def splitInput(data: List[String]) = {
    data.map(_.split("\\s+")).unzip(a => (a(0).toLong, a(1).toLong))
  }

  def part1(data: List[String]): Long = {
    val (first, second) = splitInput(data)
    first.sorted.zip(second.sorted).map(a => math.abs(a._1 - a._2)).sum
  }

  def part2(data: List[String]): Long = {
    val (first, second) = splitInput(data)
    val frequencies = second.groupMapReduce(identity)(_ => 1L)(_ + _)
    first.map(f => f * frequencies.getOrElse(f, 0L)).sum
  }

  @main def main1(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day1.txt").asScala.toList;
    val part1 = Day1.part1(data);
    println(s"part1 = $part1");
    val part2 = Day1.part2(data);
    println(s"part2 = $part2");
  }
}