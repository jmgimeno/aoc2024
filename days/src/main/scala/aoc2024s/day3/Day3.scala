
package aoc2024s.day3;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day3 {

  val pattern1 = "mul\\((\\d{1,3}),(\\d{1,3})\\)".r

  def sumLine(line: String): Long = {
    pattern1.findAllMatchIn(line).map(m => m.group(1).toLong * m.group(2).toLong).sum
  }

  def part1(data: List[String]): Long = {
    data.map(sumLine).sum
  }

  val pattern2 = "mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)".r

  case class State(result: Long, doMul: Boolean) {
    def updated(arg1: Long, arg2: Long): State =
      if doMul then copy(result = result + arg1 * arg2) else this
  }

  def part2(data: List[String]): Long = {
    data.foldLeft(State(0, true)) { (state, line) =>
      pattern2.findAllMatchIn(line).foldLeft(state) { (state, m) =>
        m.group(0) match {
          case "do()" => state.copy(doMul = true)
          case "don't()" => state.copy(doMul = false)
          case _ => state.updated(m.group(1).toLong, m.group(2).toLong)
        }
      }
    }.result

  }

  @main def main3(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day3.txt").asScala.toList;
    val part1 = Day3.part1(data);
    println(s"part1 = $part1");
    val part2 = Day3.part2(data);
    println(s"part2 = $part2");
  }
}