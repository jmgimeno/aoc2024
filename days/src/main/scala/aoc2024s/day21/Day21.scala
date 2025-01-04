package aoc2024s.day21

import utils.IO
import scala.jdk.CollectionConverters.*

object Day21 {

  case class Position(x: Int, y: Int)

  enum Direction(dx: Int, dy: Int, val name: String) {
    case Up extends Direction(0, -1, "^")
    case Down extends Direction(0, 1, "v")
    case Left extends Direction(-1, 0, "<")
    case Right extends Direction(1, 0, ">")

    def move(pos: Position): Position = Position(pos.x + dx, pos.y + dy)
  }

  trait KeyPad {

    def keyPositions: Map[Char, Position]

    protected def allShortestPaths(from: Char, to: Char): Set[String] = {
      // find all shortest paths from 'from' to 'two' that do not
      // pass through an invalid key nor go out of bounds
      case class Node(pos: Position, path: String, distance: Int)
      val fromPos = keyPositions(from)
      val toPos = keyPositions(to)
      var minDistance = Int.MaxValue
      val queue = scala.collection.mutable.Queue(Node(fromPos, "", 0))
      val distances = scala.collection.mutable.Map
        .WithDefault[Position, Int](scala.collection.mutable.Map(fromPos -> 0), _ => Int.MaxValue)
      val paths = scala.collection.mutable.Set.empty[String]
      while queue.nonEmpty do
        val node = queue.dequeue()
        if node.pos == toPos then
          if node.distance < minDistance then
            minDistance = node.distance
            paths.clear()
            paths += node.path
          else if node.distance == minDistance then paths += node.path
        else if node.distance < minDistance then
          for dir <- Direction.values do
            val nextPos = dir.move(node.pos)
            if distances(nextPos) > node.distance + 1 then distances(nextPos) = node.distance + 1
            if keyPositions.values.toSet.contains(nextPos) then
              queue.enqueue(Node(nextPos, node.path + dir.name, node.distance + 1))
      paths.toSet
    }

    protected val shortestPaths: Map[(Char, Char), Set[String]] = {
      val keys = keyPositions.keys.toList
      (for
        from <- keys
        to <- keys
      yield (from, to) -> allShortestPaths(from, to)).toMap
    }

    def inputs(output: String): Set[String] = {
      ("A" + output)
        .sliding(2)
        .map { pair => shortestPaths(pair(0) -> pair(1)).map(_ + "A") }
        .foldLeft(Set.empty[String]) { (acc, paths) =>
          if acc.isEmpty then paths
          else
            acc.flatMap { accPath =>
              paths.map { path => accPath + path }
            }
        }
    }
  }

  class NumericalKeyPad extends KeyPad {

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

  }

  class DirectionalKeyPad extends KeyPad {

    override def keyPositions: Map[Char, Position] = Map(
      '^' -> Position(1, 0),
      'A' -> Position(2, 0),
      '<' -> Position(0, 1),
      'v' -> Position(1, 1),
      '>' -> Position(2, 1)
    )
  }

  object Part1 {

    def part1(data: List[String]): Long = {
      data.map(complexity).sum
    }

    private def complexity(code: String): Long = {
      shortest(code) * code.init.toLong
    }

    private def shortest(code: String): Long = {
      val doorKeypad = new NumericalKeyPad
      val robot1 = new DirectionalKeyPad
      val robot2 = new DirectionalKeyPad
      val sequences =
        for {
          s1 <- doorKeypad.inputs(code)
          s2 <- robot1.inputs(s1)
          s3 <- robot2.inputs(s2)
        } yield s3.length
      sequences.min
    }
  }

  def part1(data: List[String]): Long = {
    Part1.part1(data)
  }

  def part2(data: List[String]): Long = {
    ??? // TODO
  }

  @main def main21(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day21.txt").asScala.toList
    val part1 = Day21.part1(data)
    println(s"part1 = $part1")
    val part2 = Day21.part2(data)
    println(s"part2 = $part2")
  }
}
