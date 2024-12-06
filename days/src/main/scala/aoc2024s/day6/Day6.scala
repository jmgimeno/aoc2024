
package aoc2024s.day6;

import utils.IO

import java.util.concurrent.Executors
import scala.concurrent.*
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

  def initialWalk(plan: Plan, guard: Guard): Set[Position] = {
    @annotation.tailrec
    def go(current: Guard, steps: Set[Position]): Set[Position] = {
      val newSteps = steps + current.at
      if !plan.inside(current.inFrontOf) then newSteps
      else if plan(current.inFrontOf) == '#' then go(current.turnRight, newSteps)
      else go(current.step, newSteps)
    }

    go(guard, Set.empty)
  }

  def hasLoop(plan: Plan, guard: Guard, obstacle: Position): Boolean = {
    // In the second one, we only need to the guards that have encountered and obstacle
    @annotation.tailrec
    def go(current: Guard, obstacles: Set[Guard]): Boolean = {
      if obstacles.contains(current) then true
      else if !plan.inside(current.inFrontOf) then false
      else if plan(current.inFrontOf) == '#' || current.inFrontOf == obstacle then go(current.turnRight, obstacles + current)
      else go(current.step, obstacles)
    }

    go(guard, Set.empty)
  }

  def part1(data: List[String]): Long = {
    val (plan, guard) = parse(data)
    initialWalk(plan, guard).size
  }

  private def sequentialCount(plan: Plan, guard: Guard, steps: Vector[Position]) = {
    steps.count {
      hasLoop(plan, guard, _)
    }
  }

  private def parallelCount(plan: Plan, guard: Guard, steps: Vector[Position]) = {
    val numThreads = math.min(8, Runtime.getRuntime.availableProcessors()) // manually adjusted
    val pool = Executors.newFixedThreadPool(numThreads)
    given ec: ExecutionContext = ExecutionContext.fromExecutor(pool)
    val chunkSize = steps.size / numThreads + 1
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
    val steps = initialWalk(plan, guard) // remove the starting point
    parallelCount(plan, guard, (steps - guard.at).toVector)
  }

  @main def main6(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day6.txt").asScala.toList;
    val part1 = Day6.part1(data);
    println(s"part1 = $part1");
    val part2 = Day6.part2(data);
    println(s"part2 = $part2");
  }
}