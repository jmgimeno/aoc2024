
package aoc2024s.day6;

import utils.IO

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
  val plan: Vector[String] = lines.toVector
  val height: Int = plan.size
  val width: Int = plan.head.length

  def apply(x: Int)(y: Int): Char = plan(y)(x)

  def apply(position: Position): Char = this (position.x)(position.y)

  def inside(position: Position): Boolean = {
    position.x >= 0 && position.x < width && position.y >= 0 && position.y < height
  }

  def infront(guard: Guard): Char = apply(guard.infront)
}

case class Guard(at: Position, facing: Direction) {
  def infront: Position = facing(at)

  def step: Guard = copy(at = infront)

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

  enum ExitStatus {
    case Outside(steps: Vector[Position])
    case Loop
  }

  def walkImmutable(plan: Plan, guard: Guard, obstacle: Option[Position] = Option.empty): ExitStatus = {

    case class State(guard: Guard, steps: Set[Guard], obstacle: Option[Position]) {
      def step: State = copy(guard = guard.step, steps = steps + guard)

      def turnRight: State = copy(guard = guard.turnRight, steps = steps + guard)
    }

    @annotation.tailrec
    def go(current: State): State =
      if current.steps.contains(current.guard) then current // we are in a loop */
      else if !plan.inside(current.guard.infront) then current.copy(steps = current.steps + current.guard) // we exit the plan
      else if current.obstacle.contains(current.guard.infront) || plan(current.guard.infront) == '#' then go(current.turnRight)
      else go(current.step)

    val initialState = State(guard, Set.empty[Guard], obstacle)
    val finalState = go(initialState)
    if !plan.inside(finalState.guard.infront) then ExitStatus.Outside(finalState.steps.map(_.at).toVector)
    else ExitStatus.Loop
  }

  def walk(plan: Plan, guard: Guard, obstacle: Option[Position] = Option.empty): ExitStatus = {
    var current = guard
    val steps = collection.mutable.Set.empty[Guard]
    while true do
      if steps.contains(current) then return ExitStatus.Loop
      steps.add(current)
      if !plan.inside(current.infront) then return ExitStatus.Outside(steps.map(_.at).toVector)
      else if obstacle.contains(current.infront) || plan(current.infront) == '#' then current = current.turnRight
      else current = current.step
    sys.error("Unreachable code")
  }

  def part1(data: List[String]): Long = {
    val (plan, guard) = parse(data)
    walk(plan, guard) match {
      case ExitStatus.Outside(steps) => steps.size
      case ExitStatus.Loop => sys.error("No loops in original map")
    }
  }

  def part2(data: List[String]): Long = {
    val (plan, guard) = parse(data)
    walk(plan, guard) match {
      case ExitStatus.Outside(steps) =>
        parallelCount(plan, guard, steps)
      case ExitStatus.Loop => sys.error("No loops in original map")
    }
  }

  private def sequentialCount(plan: Plan, guard: Guard, steps: Vector[Position]) = {
    steps.count { position =>
      walk(plan, guard, Some(position)) == ExitStatus.Loop
    }
  }

  private def parallelCount(plan: Plan, guard: Guard, steps: Vector[Position]) = {
    val numThreads = Runtime.getRuntime.availableProcessors
    val chunkSize = steps.size / numThreads
    val chunks = steps.grouped(chunkSize).toList
    val futures = chunks.map { chunk =>
      Future {
        sequentialCount(plan, guard, chunk)
      }
    }
    futures.map(future => Await.result(future, duration.Duration.Inf)).sum
  }

  @main def main6(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day6.txt").asScala.toList;
    val part1 = Day6.part1(data);
    println(s"part1 = $part1");
    val part2 = Day6.part2(data);
    println(s"part2 = $part2");
  }
}