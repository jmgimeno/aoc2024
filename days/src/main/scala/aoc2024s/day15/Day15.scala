
package aoc2024s.day15;

import utils.IO

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
      case Up => Position(position.x, position.y - 1)
      case Down => Position(position.x, position.y + 1)
      case Left => Position(position.x - 1, position.y)
      case Right => Position(position.x + 1, position.y)
    }

    def clockWise: Direction = this match {
      case Up => Right
      case Down => Left
      case Left => Up
      case Right => Down
    }

    def counterClockWise: Direction = this match {
      case Up => Left
      case Down => Right
      case Left => Down
      case Right => Up
    }

    override def toString: String = char.toString
  }

  import Direction.*

  case class Position(x: Int, y: Int) {
    def apply(movement: Direction): Position = {
      movement(this)
    }

    def gps: Int = {
      100 * y + x
    }

    override def toString: String = s"($x, $y)"
  }

  type Grid = IArray[IArray[Content]]

  case class Update(position: Position, content: Content)

  class Warehouse(val grid: Grid) {
    val height: Int = grid.length
    val width: Int = grid(0).length

    def at(position: Position): Content = {
      grid(position.y)(position.x)
    }

    def execute(updates: List[Update]): Warehouse = {
      Warehouse {
        updates.foldLeft(grid) {
          case (grid, Update(position, content)) =>
            grid.updated(position.y, grid(position.y).updated(position.x, content))
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

    def part(isBig: Boolean): Int = {
      (for {
        y <- 0 until height
        x <- 0 until width
        if grid(y)(x) == (if isBig then Content.BoxLeft else Content.Box)
        gps = Position(x, y).gps
      } yield gps).sum
    }

    def tryMoveNew(robot: Position, direction: Direction): Option[(Warehouse, Position)] = {
      findUpdates(robot, direction).map { updates =>
        val newWarehouse = execute(updates)
        val newPosition = direction(robot)
        (newWarehouse, newPosition)
      }
    }

    def findUpdates(robot: Position, direction: Direction): Option[List[Update]] = {
      val trace = makeTrace(robot, direction)
      val end = direction(trace.last)
      println(s"Trace: ${trace.map(at).mkString} and end: ${at(end)}")
      at(end) match {
        case Wall =>
          None
        case Empty => {
          direction match {
            case Left | Right => {
              rotateNew(trace ::: List(end))
            }
            case Up | Down => {
              for {
                centerUpdates <- rotateNew(trace ::: List(end))
                boxLeft = trace.find(p => at(p) == BoxLeft)
                leftUpdates <- expandNew(boxLeft, direction, Left)
                boxRight = trace.find(p => at(p) == BoxRight)
                rightUpdates <- expandNew(boxRight, direction, Right)
              } yield centerUpdates ::: leftUpdates ::: rightUpdates
            }
          }
        }
        case _ => sys.error("Unexpected content")
      }
    }

    def expandNew(box: Option[Position], forward: Direction, expansion: Direction): Option[List[Update]] = {
      box match {
        case Some(position) =>
          val base = expansion(position)
          println(s"Expanding $forward from $position (${at(position)}) at $base (${at(base)}) in direction $expansion with target ${at(position)}")
          val trace = makeTrace(base, forward)
          println(s"Trace: ${trace.map(at).mkString}")
          if (trace.isEmpty) {
            println(s"Empty trace with box at $position in direction $forward and expanding $expansion" )
            None
          } else {
            val end = forward(trace.last)
            at(end) match {
              case Wall => None
              case Empty =>
                for {
                  forwardUpdates <- rotateNew(trace ::: List(end))
                  box = trace.find(p => at(p) == at(position))
                  expansionUpdates <- expandNew(box, forward, expansion)
                } yield forwardUpdates ::: expansionUpdates
              case _ => sys.error("Unexpected content")
            }
          }
        case None => Some(List())
      }
    }

    private def makeTrace(base: Position, forward: Direction) = {
      Iterator.iterate(base)(forward.apply).takeWhile(p => at(p) != Wall && at(p) != Empty).toList
    }

    def rotateNew(trace: List[Position]): Option[List[Update]] = {
      Some {
        val contents = trace.map(p => at(p))
        val newContents = Content.Empty +: contents.init
        trace.zip(newContents).map { case (position, content) => Update(position, content) }
      }
    }

    override def toString: String = {
      grid.map(_.mkString).mkString("\n")
    }
  }

  case class RobotPlan(movement: List[Direction])

  case class State(warehouse: Warehouse, robot: Position) {

    private def step(movement: Direction): State = {
      warehouse.tryMoveNew(robot, movement) match {
        case Some((newWarehouse, newRobot)) => State(newWarehouse, newRobot)
        case None => this
      }
    }

    def run(plan: RobotPlan): State = {
      plan.movement.foldLeft(this) { (state, movement) =>
        state.step(movement)
      }
    }

    def part1: Int = {
      warehouse.part(isBig = false)
    }

    def part2: Int = {
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
        case Content.Wall => Seq(Content.Wall, Content.Wall)
        case Content.Box => Seq(Content.BoxLeft, Content.BoxRight)
        case Content.Empty => Seq(Content.Empty, Content.Empty)
        case Content.Robot => Seq(Content.Robot, Content.Empty)
        case _ => sys.error("Only part 1 grids are updatable")
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
    val data = IO.getResourceAsList("aoc2024/day15.txt").asScala.toList;
    val part1 = Day15.part1(data);
    println(s"part1 = $part1");
    val part2 = Day15.part2(data);
    println(s"part2 = $part2");
  }
}
