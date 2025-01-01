package aoc2024s.day20

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day20 {

  extension (c: Char) {
    private def isEnd: Boolean = c == 'E'
    private def isStart: Boolean = c == 'S'
    def isTrack: Boolean = c == '.' || c == 'S' || c == 'E'
    def isWall: Boolean = c == '#'
  }

  private class Racetrack(data: List[String]) {
    private val racetrack = data.toVector
    val height: Int = racetrack.size
    val width: Int = racetrack(0).length
    val start: Position = find(_.isStart).head
    val end: Position = find(_.isEnd).head

    def apply(p: Position): Char = {
      racetrack(p.y)(p.x)
    }

    private def find(f: Char => Boolean): Seq[Position] = {
      for {
        x <- 0 until width
        y <- 0 until height
        if f(racetrack(y)(x))
      } yield Position(x, y)
    }

    private def isInside(p: Position): Boolean = {
      // The border is composed only of walls
      0 <= p.x && p.x < width && 0 <= p.y && p.y < height
    }

    def expandNoCheat(node: Node): Seq[Position] = {
      Direction.values
        .map(d => d(node.p))
        .filter(p => isInside(p) && !racetrack(p.y)(p.x).isWall)
    }

    def distances: Score[Position] = {
      val f = Score[Position](height, width)
      val g = Score[Position](height, width)
      val h = (p: Position) => end.manhattanDistance(p)
      f(start) = h(start)
      g(start) = 0
      val initial = Node(start, f(start))
      val queue = mutable.PriorityQueue(initial)
      val solutions = mutable.ArrayBuffer.empty[Node]
      while (queue.nonEmpty) {
        val current = queue.dequeue()
        if current.p == end then return g
        for (neighbour <- expandNoCheat(current)) {
          val tentativeG = g(current.p) + 1
          if (tentativeG < g(neighbour)) {
            g(neighbour) = tentativeG
            f(neighbour) = tentativeG + h(neighbour)
            queue.enqueue(
              Node(neighbour, f(neighbour))
            )
          }
        }
      }
      sys.error("No solution found")
    }

    def canCheat(p1: Position, p2: Position): Boolean = {
      p1.manhattanDistance(p2) == 2 && this(p1 mid p2).isWall
    }

    def savings(minSavings: Int): Int = {
      val d = distances
      d.keys.sortBy(p => d(p)).tails.flatMap {
        case p1 :: ps => ps.filter(canCheat(p1, _)).map(p2 => d(p2) - d(p1) - 2)
        case _ => Nil
      }.count(_ >= minSavings)
    }
  }

  case class Position(x: Int, y: Int) {

    def neighbours: Iterator[Position] = {
      Direction.values.iterator.map(d => d(this))
    }

    def manhattanDistance(other: Position): Int = {
      Math.abs(x - other.x) + Math.abs(y - other.y)
    }

    def mid(other: Position): Position = {
      Position((x + other.x) / 2, (y + other.y) / 2)
    }

    override def toString: String = s"($x, $y)"
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

  class Score[A](height: Int, width: Int) {
    private val values =
      mutable.Map.WithDefault(mutable.HashMap.empty[A, Int], (_: A) => Int.MaxValue)

    def apply(a: A): Int = values(a)

    def update(a: A, value: Int): Unit = values(a) = value

    def keys: List[A] = values.keys.toList

    override def toString: String = values.toString
  }

  case class Node(p: Position, f: Int) {}

  object Node {
    given Ordering[Node] with {
      // The lower f the higher the priority
      def compare(n1: Node, n2: Node): Int = {
        n2.f - n1.f
      }
    }
  }

  def part1(data: List[String], minSavings: Int): Int = {
    Racetrack(data).savings(minSavings)
  }

  def part2(data: List[String]): Long = {
    ??? // TODO
  }

  @main def main20(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day20.txt").asScala.toList
    val part1 = Day20.part1(data, 100)
    println(s"part1 = $part1")
    val part2 = Day20.part2(data)
    println(s"part2 = $part2")
  }
}
