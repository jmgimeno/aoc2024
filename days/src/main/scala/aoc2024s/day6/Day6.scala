
package aoc2024s.day6;

import utils.IO

import scala.collection.mutable
import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.jdk.CollectionConverters.*

case class Position(x: Int, y: Int)

enum Direction(val dx: Int, val dy: Int) {
  case Up extends Direction(0, -1)
  case Down extends Direction(0, 1)
  case Left extends Direction(-1, 0)
  case Right extends Direction(1, 0)

  def apply(position: Position): Position = Position(position.x + dx, position.y + dy)

  def turnRight: Direction = this match {
    case Up => Right
    case Right => Down
    case Down => Left
    case Left => Up
  }
}

class Plan(lines: List[String]) {
  val plan: Array[String] = lines.toArray
  val height: Int = plan.length
  val width: Int = plan.head.length

  def apply(position: Position): Char = plan(position.y)(position.x)

  def inside(position: Position): Boolean = {
    position.x >= 0 && position.x < width && position.y >= 0 && position.y < height
  }

  def inFrontOf(guard: Guard): Char = this (guard.inFrontOf)
}

case class Guard(at: Position, facing: Direction) {
  def inFrontOf: Position = facing(at)

  def step: Guard = copy(at = inFrontOf)

  def turnRight: Guard = copy(facing = facing.turnRight)
}

def parse(lines: List[String]): (Plan, Guard) = {
  val plan = Plan(lines)
  val guard = Guard(find(lines, '^'), Direction.Up)
  (plan, guard)
}

def find(lines: List[String], c: Char): Position = {
  val y = lines.indexWhere(_.contains(c))
  val x = lines(y).indexOf(c)
  Position(x, y)
}

object Day6 {

  def initialWalk(plan: Plan, guard: Guard): mutable.Set[Position] = {
    // no need to detect loops, just walk until we go out of bounds
    var current = guard
    val steps = mutable.Set.empty[Position]
    while true do
      steps.add(current.at)
      if !plan.inside(current.inFrontOf) then return steps
      else if plan(current.inFrontOf) == '#' then current = current.turnRight
      else current = current.step
    sys.error("Unreachable code")
  }

  def hasLoop(plan: Plan, guard: Guard, obstacle: Position): Boolean = {
    // In the second one, we only need to the guards that have encountered and obstacle
    var current = guard
    val obstacles = collection.mutable.Set.empty[Guard]
    while true do
      if obstacles.contains(current) then return true
      val inFrontOf = current.inFrontOf
      if !plan.inside(inFrontOf) then return false
      else if plan(inFrontOf) == '#' || inFrontOf == obstacle then {
        obstacles.add(current)
        current = current.turnRight
      }
      else current = current.step
    sys.error("Unreachable code")
  }

  def part1(data: List[String]): Long = {
    val (plan, guard) = parse(data)
    initialWalk(plan, guard).size
  }

  private def sequentialCount(plan: Plan, guard: Guard, steps: mutable.Set[Position]) = {
    steps.count {
      hasLoop(plan, guard, _)
    }
  }

  private def parallelCount(plan: Plan, guard: Guard, steps: mutable.Set[Position]) = {
    val numThreads = Runtime.getRuntime.availableProcessors
    val chunkSize = steps.size / numThreads
    val chunks = steps.grouped(chunkSize).toList // w/o it the execution time doubles
    val futures = chunks.map { chunk =>
      Future {
        sequentialCount(plan, guard, chunk)
      }
    }
    futures.map(future => Await.result(future, duration.Duration.Inf)).sum
  }

  def part2(data: List[String]): Long = {
    val (plan, guard) = parse(data)
    val steps = initialWalk(plan, guard)
    parallelCount(plan, guard, steps)
  }

  @main def main6(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day6.txt").asScala.toList;
    val part1 = Day6.part1(data);
    println(s"part1 = $part1");
    val part2 = Day6.part2(data);
    println(s"part2 = $part2");
  }
}