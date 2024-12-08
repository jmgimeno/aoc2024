
package aoc2024s.day8;

import utils.IO

import scala.annotation.targetName
import scala.jdk.CollectionConverters.*

object Day8 {

  case class Position(x: Int, y: Int) {
    @targetName("sub") def -(other: Position): Position = Position(x - other.x, y - other.y)

    @targetName("add") def +(other: Position): Position = Position(x + other.x, y + other.y)

    @targetName("mul") def *(factor: Int): Position = Position(x * factor, y * factor)

    def antinodes(factor: Int)(p1: Position, p2: Position): List[Position] = {
      val delta = p1 - p2
      List(p1 + delta * factor, p2 - delta * factor)
    }
  }

  case class Antennas(antennas: Map[Char, List[Position]], height: Int, width: Int) {
    def isInside(position: Position): Boolean = {
      position.x >= 0 && position.x < width && position.y >= 0 && position.y < height
    }

    def antinodes(hops: Int): Set[Position] = {
      antennas.flatMap { case (_, positions) =>
        positions.flatMap { p1 =>
          positions.flatMap { p2 =>
            if (p1 != p2) p1.antinodes(hops)(p1, p2).filter(isInside) else Nil
          }
        }
      }.toSet
    }

    def antinodesForPart2: Set[Position] = {
      LazyList.from(0)
        .map(antinodes)
        .takeWhile(_.nonEmpty)
        .flatten
        .toSet
    }

    def part1: Long = antinodes(1).size

    def part2: Long = antinodesForPart2.size
  }

  object Antennas {
    def apply(data: List[String]): Antennas = {
      val antennas = data.zipWithIndex.flatMap { case (line, y) =>
        line.zipWithIndex.filter(_._1 != '.').map { case (c, x) =>
          (c, Position(x, y))
        }
      }.groupBy(_._1).map { case (c, positions) =>
        (c, positions.map(_._2))
      }
      Antennas(antennas, height = data.size, width = data.head.length)
    }
  }

  def part1(data: List[String]): Long = {
    Antennas(data).part1
  }

  def part2(data: List[String]): Long = {
    Antennas(data).part2
  }

  @main def main8(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day8.txt").asScala.toList;
    val part1 = Day8.part1(data);
    println(s"part1 = $part1");
    val part2 = Day8.part2(data);
    println(s"part2 = $part2");
  }
}
