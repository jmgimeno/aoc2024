
package aoc2024s.day4;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day4 {

  def part1(data: List[String]): Long = {
    Part1Grid(data).count("XMAS")
  }

  def part2(data: List[String]): Long = {
    Part2Grid(data).count("MAS");
  }

  @main def main4(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day4.txt").asScala.toList;
    val part1 = Day4.part1(data);
    println(s"part1 = $part1");
    val part2 = Day4.part2(data);
    println(s"part2 = $part2");
  }
}