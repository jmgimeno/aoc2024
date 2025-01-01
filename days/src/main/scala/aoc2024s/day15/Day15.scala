package aoc2024s.day15

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day15 {

  enum Content(val char: Char) {
    case Box extends Content('O')
    case Empty extends Content('.')
    case Robot extends Content('@')
    case Wall extends Content('#')
    case BoxLeft extends Content('[')
    case BoxRight extends Content(']')

    override def toString: String = char.toString
  }

  import Content.*

  type Step = Position => Position

  enum Direction(val char: Char) {
    case Up extends Direction('^')
    case Down extends Direction('v')
    case Left extends Direction('<')
    case Right extends Direction('>')

    def apply(position: Position): Position = this match {
      case Up    => Position(position.x, position.y - 1)
      case Down  => Position(position.x, position.y + 1)
      case Left  => Position(position.x - 1, position.y)
      case Right => Position(position.x + 1, position.y)
    }

    override def toString: String = char.toString
  }

  import Direction.*

  case class Position(x: Int, y: Int) {
    def apply(movement: Direction): Position = {
      movement(this)
    }

    def gps: Long = {
      100L * y + x
    }

    override def toString: String = s"($x, $y)"
  }

  private type Grid = IArray[IArray[Content]]

  case class Update(position: Position, content: Content)

  case class Counters(
      boxes: Int,
      boxesLeft: Int,
      boxesRight: Int,
      empty: Int,
      robot: Int,
      walls: Int
  )

  class Warehouse(val grid: Grid) {
    val height: Int = grid.length
    val width: Int = grid(0).length

    def at(position: Position): Content = {
      grid(position.y)(position.x)
    }

    def execute(updates: List[Update]): Warehouse = {
      Warehouse {
        updates.foldLeft(grid) { case (grid, Update(Position(x, y), content)) =>
          grid.updated(y, grid(y).updated(x, content))
        }
      }
    }

    def findRobot(): Position = {
      val robots = for {
        y <- 0 until height
        x <- 0 until width
        if grid(y)(x) == Content.Robot
      } yield Position(x, y)
      robots.head
    }

    def part(isBig: Boolean): Long = {
      (for {
        y <- 0 until height
        x <- 0 until width
        if grid(y)(x) == (if isBig then Content.BoxLeft else Content.Box)
        gps = Position(x, y).gps
      } yield gps).sum
    }

    def move(robot: Position, forward: Direction): (Warehouse, Position) = {
      findUpdates(robot, forward) match {
        case Nil     => (this, robot)
        case updates => (execute(updates), forward(robot))
      }
    }

    private def findUpdates(from: Position, forward: Direction): List[Update] = {
      forward match {
        case Left | Right =>
          updatesForward(from, forward)
        case Up | Down =>
          updatesForwardAndSideways(from, forward)
      }
    }

    private def updatesForward(from: Position, forward: Direction): List[Update] = {
      val positions = findShapeForward(from, forward)
      val end = forward(positions.last)
      at(end) match {
        case Wall =>
          List.empty
        case Empty =>
          rotate(positions ::: List(end))
        case _ => sys.error("Impossible")
      }
    }

    private def findShapeForward(from: Position, forward: Direction): List[Position] = {
      Iterator
        .iterate(from)(forward.apply)
        .takeWhile(p => at(p) != Wall && at(p) != Empty)
        .toList
    }

    private def updatesForwardAndSideways(from: Position, forward: Direction): List[Update] = {
      val positions = findShapeForwardAndSideways(from, forward)
      val comparator =
        if forward == Down then Ordering.by[Position, Int](_.y) else Ordering.by[Position, Int](_.y).reverse
      val columns = positions.groupBy(_.x).map((x, positions) => (x, positions.sorted(comparator)))
      val segments = columns.values.flatMap(column => splitSegments(column))
      val traces = segments.map(segment => segment :+ forward(segment.last))
      if traces.forall(trace => at(trace.last) == Empty) then
          traces.flatMap(trace => rotate(trace)).toList
      else List.empty
    }

    private def splitSegments(column: List[Position]): List[List[Position]] = {
      column
        .foldLeft(List.empty[List[Position]]) { (acc, position) =>
          acc match {
            case Nil => List(List(position))
            case head :: tail =>
              if math.abs(head.last.y - position.y) == 1 then (head :+ position) :: tail
              else List(position) :: acc
          }
        }
        .reverse
    }

    private def findShapeForwardAndSideways(from: Position, forward: Direction): List[Position] = {
      val explored = mutable.HashSet.empty[Position]
      val stack = mutable.Stack(from)
      while stack.nonEmpty do
        val current = stack.pop()
        if !explored.contains(current) then
          at(current) match
            case Robot | Box =>
              explored += current
              stack.push(forward(current))
            case BoxLeft =>
              explored += current
              stack.push(forward(current))
              stack.push(Right(current))
            case BoxRight =>
              explored += current
              stack.push(forward(current))
              stack.push(Left(current))
            case Wall | Empty => ()
      explored.toList
    }

    private def rotate(trace: List[Position]): List[Update] = {
      val contents = trace.map(p => at(p))
      val newContents = contents.last +: contents.init
      trace.zip(newContents).map { case (position, newContent) =>
        Update(position, newContent)
      }
    }

    override def toString: String = {
      grid.map(_.mkString).mkString("\n")
    }

    def count: Counters = {
      grid.foldLeft(Counters(0, 0, 0, 0, 0, 0)) { (counters, row) =>
        row.foldLeft(counters) { (counters, content) =>
          content match {
            case Box      => counters.copy(boxes = counters.boxes + 1)
            case BoxLeft  => counters.copy(boxesLeft = counters.boxesLeft + 1)
            case BoxRight => counters.copy(boxesRight = counters.boxesRight + 1)
            case Empty    => counters.copy(empty = counters.empty + 1)
            case Robot    => counters.copy(robot = counters.robot + 1)
            case Wall     => counters.copy(walls = counters.walls + 1)
          }
        }
      }
    }

    private def wellFormedLeftBoxes: Boolean = {
      for {
        y <- 0 until height
        x <- 0 until width
        if grid(y)(x) == BoxLeft
        if grid(y)(x + 1) != BoxRight
      } return false
      true
    }

    private def wellFormedRightBoxes: Boolean = {
      for {
        y <- 0 until height
        x <- 0 until width
        if grid(y)(x) == BoxRight
        if grid(y)(x - 1) != BoxLeft
      } return false
      true
    }

    def wellFormedBoxes: Boolean = {
      wellFormedLeftBoxes && wellFormedRightBoxes
    }
  }

  case class RobotPlan(movement: List[Direction])

  case class State(warehouse: Warehouse, robot: Position) {

    private def step(movement: Direction): State = {
      warehouse.move(robot, movement) match {
        case (newWarehouse, newRobot) => State(newWarehouse, newRobot)
      }
    }

    def run(plan: RobotPlan): State = {
      plan.movement.foldLeft(this) { (state, movement) =>
        state.step(movement)
      }
    }

    def part1: Long = {
      warehouse.part(isBig = false)
    }

    def part2: Long = {
      warehouse.part(isBig = true)
    }
  }

  object Parser {
    def parse(data: List[String]): (Warehouse, RobotPlan) = {
      val (gridData, planData) = data.span(_.nonEmpty)
      (parseGrid(gridData), parsePlan(planData.tail))
    }

    private def parseGrid(strings: List[String]): Warehouse = {
      Warehouse {
        IArray.from(strings.map(parseRow))
      }
    }

    private def parseRow(row: String): IArray[Content] = {
      IArray.from(row.map(parseContent))
    }

    private def parseContent(char: Char): Content = {
      Content.values.find(_.char == char).get
    }

    private def parsePlan(lines: List[String]): RobotPlan = {
      RobotPlan {
        lines.flatMap(parsePlan)
      }
    }

    private def parsePlan(line: String): Seq[Direction] = {
      line.map(parseMovement)
    }

    private def parseMovement(char: Char): Direction = {
      Direction.values.find(_.char == char).get
    }
  }

  def part1(data: List[String]): Long = {
    val (warehouse, plan) = Parser.parse(data)
    val robot = warehouse.findRobot()
    val initial = State(warehouse, robot)
    initial.run(plan).part1
  }

  private object Part2Transformer {
    def transform(warehouse: Warehouse): Warehouse = {
      Warehouse {
        IArray.from(warehouse.grid.map(upgradeRow))
      }
    }

    private def upgradeRow(row: IArray[Content]): IArray[Content] = {
      IArray.from(row.flatMap(upgradeContent))
    }

    private def upgradeContent(content: Content): Seq[Content] = {
      content match {
        case Content.Wall  => Seq(Content.Wall, Content.Wall)
        case Content.Box   => Seq(Content.BoxLeft, Content.BoxRight)
        case Content.Empty => Seq(Content.Empty, Content.Empty)
        case Content.Robot => Seq(Content.Robot, Content.Empty)
        case _             => sys.error("Only part 1 grids are updatable")
      }
    }
  }

  def part2(data: List[String]): Long = {
    val (warehouse, plan) = Parser.parse(data)
    val transformed = Part2Transformer.transform(warehouse)
    val robot = transformed.findRobot()
    val initial = State(transformed, robot)
    initial.run(plan).part2
  }

  @main def main15(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day15.txt").asScala.toList
    val part1 = Day15.part1(data)
    println(s"part1 = $part1")
    val part2 = Day15.part2(data)
    println(s"part2 = $part2")
  }
}
