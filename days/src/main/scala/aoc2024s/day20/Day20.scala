package aoc2024s.day20

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day20 {

  extension (c: Char) {
    def isEnd: Boolean = c == 'E'
    def isStart: Boolean = c == 'S'
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

    def expandNoCheat(node: Node): Seq[(Position, CheatingHistory)] = {
      Direction.values
        .map(d => d(node.p))
        .filter(p => isInside(p) && !racetrack(p.y)(p.x).isWall)
        .map((_, node.cheatingHistory))
    }

    def expandToStart(node: Node): Seq[(Position, CheatingHistory)] = {
      Direction.values
        .map(d => d(node.p))
        .filter(p => isInside(p) && racetrack(p.y)(p.x).isWall)
        .map(p => (p, CheatingHistory.Started(p)))
    }

    def expandToEnd(
        node: Node,
        start: Position
    ): Seq[(Position, CheatingHistory)] = {
      Direction.values
        .map(d => d(node.p))
        .filter(p => isInside(p) && !racetrack(p.y)(p.x).isWall)
        .map(p => (p, CheatingHistory.Ended(start, p)))
    }

    def expand(
        allowCheating: Boolean
    )(node: Node): Seq[(Position, CheatingHistory)] = {
      if !allowCheating then expandNoCheat(node)
      else
        node match {
          case Node(p: Position, _, CheatingHistory.NoCheating) =>
            expandNoCheat(node) ++ expandToStart(node)
          case Node(p: Position, _, CheatingHistory.Started(start)) =>
            expandToEnd(node, start)
          case Node(p: Position, _, CheatingHistory.Ended(_, _)) =>
            expandNoCheat(node)
        }
    }
  }

  case class Position(x: Int, y: Int) {

    def neighbours: Iterator[Position] = {
      Direction.values.iterator.map(d => d(this))
    }

    def manhattanDistance(other: Position): Int = {
      Math.abs(x - other.x) + Math.abs(y - other.y)
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

  private class Score[A](height: Int, width: Int) {
    private val values =
      mutable.Map.WithDefault(mutable.HashMap.empty[A, Int], _ => Int.MaxValue)
    def apply(a: A): Int = values(a)
    def update(a: A, value: Int): Unit = values(a) = value
  }

  enum CheatingHistory {
    case NoCheating
    case Started(start: Position)
    case Ended(start: Position, end: Position)
  }

  case class Node(p: Position, f: Int, cheatingHistory: CheatingHistory) {}

  object Node {
    given Ordering[Node] with {
      // The lower f the higher the priority
      def compare(n1: Node, n2: Node): Int = {
        n2.f - n1.f
      }
    }
  }

  def allPaths(data: List[String],
                canCheat: Boolean,
                findMany: Boolean,
                maxCost: Int = Int.MaxValue
  ): List[Node] = {
    val racetrack = Racetrack(data)
    val start = racetrack.start
    val end = racetrack.end
    val f =
      Score[(Position, CheatingHistory)](racetrack.height, racetrack.width)
    val g =
      Score[(Position, CheatingHistory)](racetrack.height, racetrack.width)
    val h = (p: Position) => end.manhattanDistance(p)
    val expand = racetrack.expand(canCheat)
    f((start, CheatingHistory.NoCheating)) = h(start)
    g((start, CheatingHistory.NoCheating)) = 0
    val initial = Node(
      start,
      f((start, CheatingHistory.NoCheating)),
      CheatingHistory.NoCheating
    )
    val queue = mutable.PriorityQueue(initial)
    val solutions = mutable.ArrayBuffer.empty[Node]
    while (queue.nonEmpty) {
      val current = queue.dequeue()
      if (
        current.p == end && !current.cheatingHistory
          .isInstanceOf[CheatingHistory.Started]
      ) {
        solutions += current
        if (!findMany) return solutions.toList
      }
      for ((neighbour, cheatingHistory) <- expand(current)) {
        val tentativeG = g((current.p, current.cheatingHistory)) + 1
        if (tentativeG < g((neighbour, cheatingHistory))) {
          g((neighbour, cheatingHistory)) = tentativeG
          f((neighbour, cheatingHistory)) = tentativeG + h(neighbour)
          if f((neighbour, cheatingHistory)) <= maxCost then {
            queue.enqueue(
              Node(neighbour, f((neighbour, cheatingHistory)), cheatingHistory)
            )
          }
        }
      }
    }
    solutions.toList
  }

  def part1(data: List[String], minSavings: Int): Int = {
    val noCheating = allPaths(data, canCheat = false, findMany = false)
    println(s"without cheating = ${noCheating.head.f}")
    val threshold = noCheating.head.f - minSavings
    println(s"threshold = $threshold")
    val cheating =
      allPaths(data, canCheat = true, findMany = true, maxCost = threshold)
    cheating.size
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
