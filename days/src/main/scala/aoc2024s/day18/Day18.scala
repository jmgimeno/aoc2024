package aoc2024s.day18

import utils.IO

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*
import scala.collection.mutable

object Day18 {

  case class Position(x: Int, y: Int) {

    def neighbours: Iterator[Position] = {
      Direction.values.iterator.map(d => d(this))
    }

    def manhattanDistance(other: Position): Int = {
      Math.abs(x - other.x) + Math.abs(y - other.y)
    }
  }

  enum Direction(dx: Int, dy: Int) {
    case UP extends Direction(0, -1)
    case DOWN extends Direction(0, 1)
    case LEFT extends Direction(-1, 0)
    case RIGHT extends Direction(1, 0)

    def apply(p: Position): Position = {
      Position(p.x + dx, p.y + dy)
    }
  }

  object Parser {
    def parse(data: List[String]): List[Position] = {
      data.map { line =>
        val parts = line.split(",")
        Position(parts(0).toInt, parts(1).toInt)
      }
    }
  }

  private case class MemorySpace(
      maxCoordinate: Int,
      corrupted: IArray[IArray[Boolean]]
  ) {

    def this(maxCoordinate: Int) = {
      this(
        maxCoordinate,
        IArray.fill(maxCoordinate + 1, maxCoordinate + 1)(false)
      )
    }

    def this(maxCoordinate: Int, fallen: List[Position]) = {
      this(
        maxCoordinate, {
          val empty = IArray.fill(maxCoordinate + 1, maxCoordinate + 1)(false)
          fallen.foldLeft(empty) { (acc, pos) =>
            acc.updated(pos.y, acc(pos.y).updated(pos.x, true))
          }
        }
      )
    }

    def isCorrupted(x: Int, y: Int): Boolean = corrupted(y)(x)

    def safeNeighbours(p: Position): Iterator[Position] = {
      p.neighbours.filter { case Position(x, y) =>
        x >= 0 && x <= maxCoordinate && y >= 0 && y <= maxCoordinate && !corrupted(
          y
        )(x)
      }
    }

    def pathToString(path: List[Position]): String = {
      val inPath = path.toSet
      (0 to maxCoordinate)
        .map { y =>
          (0 to maxCoordinate).map { x =>
            if (corrupted(y)(x)) "#"
            else if inPath(Position(x, y)) then "O"
            else "."
          }.mkString
        }
        .mkString("\n")
    }

    override def toString: String = {
      (0 to maxCoordinate)
        .map { y =>
          (0 to maxCoordinate).map { x =>
            if (corrupted(y)(x)) "#" else "."
          }.mkString
        }
        .mkString("\n")
    }

  }

  private class PathFinder(memSpace: MemorySpace) {

    case class Node(p: Position, f: Int, cameFrom: Node = null) {
      def getPath: List[Position] = {
        val result = mutable.ArrayBuffer(p)
        var current = this
        while (current.cameFrom != null) {
          current = current.cameFrom
          result.addOne(current.p)
        }
        result.toList
      }
    }

    given Ordering[Node] with {
      // The lower f the higher the priority
      def compare(n1: Node, n2: Node): Int = {
        n2.f - n1.f
      }
    }

    private class Score(maxCoordinates: Int) {
      private val values =
        Array.fill(maxCoordinates + 1, maxCoordinates + 1)(Int.MaxValue)
      def apply(position: Position): Int = values(position.y)(position.x)
      def update(position: Position, value: Int): Unit =
        values(position.y)(position.x) = value
    }

    def bestPath: List[Position] = {
      val start = Position(0, 0)
      val end = Position(memSpace.maxCoordinate, memSpace.maxCoordinate)
      // We use A* using manhattan as heuristic
      val f = Score(memSpace.maxCoordinate)
      val g = Score(memSpace.maxCoordinate)
      // val h = end.manhattanDistance
      val h = (p: Position) => 0 // BFS
      f(start) = h(start)
      g(start) = 0
      val queue = mutable.PriorityQueue(Node(start, f(start), null))
      while (queue.nonEmpty) {
        val current = queue.dequeue()
        if (current.p == end) {
          return current.getPath
        }
        for (neighbour <- memSpace.safeNeighbours(current.p)) {
          val tentativeG = g(current.p) + 1
          if (tentativeG < g(neighbour)) {
            g(neighbour) = tentativeG
            f(neighbour) = tentativeG + h(neighbour)
            queue.enqueue(Node(neighbour, f(neighbour), current))
          }
        }
      }
      List.empty
    }
  }

  private class PathCloser(incoming: List[Position], maxCoordinate: Int) {

    private def connected(n: Int): Boolean = {
      val memSpace = new MemorySpace(maxCoordinate, incoming.take(n))
      val bestPath = PathFinder(memSpace).bestPath
      bestPath.nonEmpty
    }

    @tailrec
    private def findFirstPosition(
        begin: Int,
        end: Int
    ): Int = {
      if begin == end then begin
      else {
        val mid = begin + (end - begin) / 2
        if connected(mid) then {
          findFirstPosition(mid + 1, end)
        } else {
          findFirstPosition(begin, mid)
        }
      }
    }

    def findFirstPosition: Position = {
      val i = findFirstPosition(0, incoming.length + 1)
      incoming(i - 1)
    }
  }

  def part1(maxCoordinate: Int, numBytes: Int, data: List[String]): Int = {
    val incoming = Parser.parse(data).take(numBytes)
    val memSpace = new MemorySpace(maxCoordinate, incoming)
    val start = Position(0, 0)
    val end = Position(maxCoordinate, maxCoordinate)
    val bestPath = PathFinder(memSpace).bestPath
    bestPath.length - 1
  }

  def part2(maxCoordinate: Int, data: List[String]): Position = {
    val incoming = Parser.parse(data)
    PathCloser(incoming, maxCoordinate).findFirstPosition
  }

  @main def main18(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day18.txt").asScala.toList
    val part1 = Day18.part1(70, 1024, data)
    println(s"part1 = $part1")
    val part2 = Day18.part2(70, data)
    println(s"part2 = $part2")
  }
}
