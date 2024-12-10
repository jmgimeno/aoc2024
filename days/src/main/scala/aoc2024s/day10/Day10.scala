
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

    override def toString: String =
      values.map(_.mkString).mkString("\n")

    def expand(p: Position): Seq[Position] =
      Direction.expand(p).filter(isInside)

    def totalScore1: Int =
      trailheads.map(score1).sum

    def score1(p: Position): Int = {
      def go(at: Position, found: Set[Position]): Set[Position] = {
        val value = this (at)
        if value == 9 then found + at
        else {
          expand(at)
            .filter(p => this (p) == value + 1)
            .foldLeft(found)((found, p) => go(p, found))
        }
      }

      go(p, Set.empty).size
    }

    def totalScore2: Int =
      trailheads.map(score2).sum

    def score2(p: Position): Int = {
      def go(path: List[Position], found: Set[List[Position]]): Set[List[Position]] = {
        val at = path.head
        val value = this (at)
        if value == 9 then found + path
        else {
          expand(at)
            .filter(p => this (p) == value + 1)
            .foldLeft(found)((found, p) => go(p :: path, found))
        }
      }

      go(List(p), Set.empty).size
    }
  }

  def part1(data: List[String]): Long = {
    new TopographicalMap(data).totalScore1
  }

  def part2(data: List[String]): Long = {
    new TopographicalMap(data).totalScore2
  }

  @main def main10(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day10.txt").asScala.toList;
    val part1 = Day10.part1(data);
    println(s"part1 = $part1");
    val part2 = Day10.part2(data);
    println(s"part2 = $part2");
  }
}
