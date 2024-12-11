
package aoc2024s.day10;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day10 {

  enum Direction(val dx: Int, val dy: Int) {
    case Up extends Direction(0, -1)
    case Right extends Direction(1, 0)
    case Down extends Direction(0, 1)
    case Left extends Direction(-1, 0)

    def apply(p: Position): Position =
      Position(p.x + dx, p.y + dy)
  }

  object Direction {
    def expand(p: Position): Seq[Position] =
      Direction.values.map(_.apply(p))
  }

  case class Position(x: Int, y: Int)

  type Path = List[Position]

  case class TopographicalMap(values: Array[Array[Int]]) {
    val height: Int = values.length
    val width: Int = values(0).length

    def apply(p: Position): Int =
      values(p.y)(p.x)

    def isInside(p: Position): Boolean =
      p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

    def this(data: List[String]) =
      this(data.map(_.map {
        _ - '0'
      }.toArray).toArray)

    def trailheads: Seq[Position] =
      for
        y <- 0 until height
        x <- 0 until width
        if values(y)(x) == 0
      yield Position(x, y)

    def expand(p: Position): Seq[Position] =
      Direction.expand(p).filter(isInside)

    def trails(start: Position): Seq[Path] =
      trails(List(start))

    def trails(path: Path): Seq[Path] = {
      val at = path.head
      val value = this (at)
      if value == 9 then Seq(path)
      else {
        expand(at)
          .filter(p => this (p) == value + 1)
          .flatMap(p => trails(p :: path))
      }
    }

    def part1: Long =
      trailheads
        .flatMap(trails(_).map(_.head).distinct)
        .size

    def part2: Long =
      trailheads
        .flatMap(trails)
        .size
  }

  def part1(data: List[String]): Long = {
    new TopographicalMap(data).part1
  }

  def part2(data: List[String]): Long = {
    new TopographicalMap(data).part2
  }

  @main def main10(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day10.txt").asScala.toList;
    val part1 = Day10.part1(data);
    println(s"part1 = $part1");
    val part2 = Day10.part2(data);
    println(s"part2 = $part2");
  }
}
