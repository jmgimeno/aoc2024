package aoc2024s.day21

import utils.IO

import scala.annotation.targetName
import scala.collection.mutable

import scala.jdk.CollectionConverters.*

object Day21 {

  case class Position(x: Int, y: Int) {
    @targetName("add")
    def +(other: Position): Position = Position(x + other.x, y + other.y)
  }

  enum Direction(dx: Int, dy: Int, val name: String) {
    case Up extends Direction(0, -1, "^")
    case Down extends Direction(0, 1, "v")
    case Left extends Direction(-1, 0, "<")
    case Right extends Direction(1, 0, ">")

    def move(pos: Position): Position = Position(pos.x + dx, pos.y + dy)
  }

  private trait KeyPad {

    def keyPositions: Map[Char, Position]

    def invalid: Position

    private def allShortestPaths(from: Char, to: Char): List[String] = {
      val fromPos = keyPositions(from)
      val toPos = keyPositions(to)
      val dx = toPos.x - fromPos.x
      val dy = toPos.y - fromPos.y
      val xSteps = (if dx < 0 then "<" else ">") * dx.abs
      val ySteps = (if dy < 0 then "^" else "v") * dy.abs
      if (dx == 0 && dy == 0) List("")
      else if (dx == 0) List(ySteps)
      else if (dy == 0) List(xSteps)
      else if (fromPos + Position(dx, 0) == invalid) List(ySteps + xSteps)
      else if (fromPos + Position(0, dy) == invalid) List(xSteps + ySteps)
      else List(xSteps + ySteps, ySteps + xSteps)
    }

    val shortestPaths: Map[(Char, Char), List[String]] = {
      val keys = keyPositions.keys.toList
      (for
        from <- keys
        to <- keys
      yield (from, to) -> allShortestPaths(from, to)).toMap
    }

    def inputs(output: String): List[String] = {
      ("A" + output)
        .sliding(2)
        .map { pair => shortestPaths(pair(0) -> pair(1)).map(_ + "A") }
        .foldLeft(List.empty[String]) { (acc, paths) =>
          if acc.isEmpty then paths
          else
            acc.flatMap { accPath =>
              paths.map { path => accPath + path }
            }
        }
    }
  }

  private class NumericalKeyPad extends KeyPad {

    override def keyPositions: Map[Char, Position] = Map(
      '7' -> Position(0, 0),
      '8' -> Position(1, 0),
      '9' -> Position(2, 0),
      '4' -> Position(0, 1),
      '5' -> Position(1, 1),
      '6' -> Position(2, 1),
      '1' -> Position(0, 2),
      '2' -> Position(1, 2),
      '3' -> Position(2, 2),
      '0' -> Position(1, 3),
      'A' -> Position(2, 3)
    )

    override def invalid: Position = Position(0, 3)
  }

  private class DirectionalKeyPad extends KeyPad {

    override def keyPositions: Map[Char, Position] = Map(
      '^' -> Position(1, 0),
      'A' -> Position(2, 0),
      '<' -> Position(0, 1),
      'v' -> Position(1, 1),
      '>' -> Position(2, 1)
    )

    override def invalid: Position = Position(0, 0)
  }

  private class Runner(steps: Int) {

    private val cache = mutable.Map.empty[(Char, Char, Int), Long]

    def run(data: List[String]): Long = {
      data.map(complexity).sum
    }

    private def complexity(code: String): Long = {
      shortest(code) * code.init.toLong
    }
    
    private def shortest(code: String): Long = {

      def shortestStep(from: Char, to: Char, level: Int): Long = {
        val keyPad = if level == 0 then new NumericalKeyPad else new DirectionalKeyPad
        keyPad
          .shortestPaths(from -> to)
          .map(path => shortest(path + "A", level + 1))
          .min
      }

      def cachedShortestStep(from: Char, to: Char, level: Int): Long = {
        cache.getOrElseUpdate((from, to, level), shortestStep(from, to, level))
      }

      def shortest(code: String, level: Int): Long = {
        if level == steps then code.length
        else
          ("A" + code)
            .sliding(2)
            .map { pair => cachedShortestStep(pair(0), pair(1), level) }
            .sum
      }

      shortest(code, 0)
    }
  }
  
  def part1(data: List[String]): Long = {
    Runner(3).run(data)
  }

  def part2(data: List[String]): Long = {
    Runner(26).run(data)
  }

  @main def main21(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day21.txt").asScala.toList
    val part1 = Day21.part1(data)
    println(s"part1 = $part1")
    val part2 = Day21.part2(data)
    println(s"part2 = $part2")
  }
}
