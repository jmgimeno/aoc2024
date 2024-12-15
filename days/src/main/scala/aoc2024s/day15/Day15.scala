
package aoc2024s.day15;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day15 {

  enum Content(val char: Char) {
    case Box extends Content('O')
    case Empty extends Content('.')
    case Robot extends Content('@')
    case Wall extends Content('#')

    override def toString: String = char.toString
  }

  enum Movement(val char: Char) extends (Position => Position) {
    private case Up extends Movement('^')
    private case Down extends Movement('v')
    private case Left extends Movement('<')
    private case Right extends Movement('>')

    def apply(position: Position): Position = this match {
      case Up => Position(position.x, position.y - 1)
      case Down => Position(position.x, position.y + 1)
      case Left => Position(position.x - 1, position.y)
      case Right => Position(position.x + 1, position.y)
    }

    override def toString: String = char.toString
  }

  case class Position(x: Int, y: Int) extends (Movement => Position) {
    def apply(movement: Movement): Position = {
      movement(this)
    }

    def gps: Int = {
      100 * y + x
    }

    override def toString: String = s"($x, $y)"
  }

  class Warehouse(grid: IArray[IArray[Content]]) {
    val height: Int = grid.length
    val width: Int = grid(0).length

    def apply(position: Position): Content = {
      grid(position.y)(position.x)
    }

    private def updated(position: Position, content: Content): Warehouse = {
      Warehouse {
        grid.updated(position.y, grid(position.y).updated(position.x, content))
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

    def part1: Int = {
      (for {
        y <- 0 until height
        x <- 0 until width
        if grid(y)(x) == Content.Box
        gps = Position(x, y).gps
      } yield gps).sum
    }

    def tryMove(from: Position, movement: Movement): Option[(Warehouse, Position)] = {
      assert(this (from) == Content.Robot)
      val to = movement(from)
      this (to) match {
        case Content.Wall => None
        case Content.Empty =>
          // robot moves to empty space
          robotMovesToEmptySpace(from, to).map((_, to))
        case Content.Box =>
          robotMovesToBox(from, to, movement).map((_, to))
        case _ => throw new IllegalArgumentException("Invalid move")
      }
    }

    private def robotMovesToEmptySpace(robot: Position, to: Position): Option[Warehouse] = {
      assert(this (robot) == Content.Robot && this (to) == Content.Empty)
      Some {
        updated(robot, Content.Empty)
          .updated(to, Content.Robot)
      }
    }

    private def robotMovesToBox(robot: Position, boxStart: Position, movement: Movement): Option[Warehouse] = {
      assert(this (robot) == Content.Robot && this (boxStart) == Content.Box)
      val (boxEnd, boxSize) = analyseBox(boxStart, movement)
      assert(this (boxEnd) == Content.Box)
      val afterBox = movement(boxEnd)
      this (afterBox) match {
        case Content.Empty =>
          // box moves to empty space
          boxMovesToEmptySpace(robot, boxStart, afterBox)
        case _ => None
      }
    }

    private def analyseBox(position: Position, movement: Movement): (Position, Int) = {
      val trace = LazyList.iterate(position)(movement).takeWhile(p => this (p) == Content.Box)
      (trace.last, trace.size)
    }

    private def boxMovesToEmptySpace(robot: Position, boxStart: Position, afterBox: Position): Option[Warehouse] = {
      assert(this(robot)== Content.Robot &&  this(boxStart) == Content.Box && this (afterBox) == Content.Empty)
      Some {
        updated(robot, Content.Empty)
          .updated(boxStart, Content.Robot)
          .updated(afterBox, Content.Box)
      }
    }

    override def toString: String = {
      grid.map(_.mkString).mkString("\n")
    }
  }

  case class RobotPlan(movement: List[Movement])

  case class State(warehouse: Warehouse, robot: Position) {

    private def step(movement: Movement): State = {
      warehouse.tryMove(robot, movement) match {
        case Some((newWarehouse, newRobot)) => State(newWarehouse, newRobot)
        case None => this
      }
    }

    def run(plan: RobotPlan): State = {
      plan.movement.foldLeft(this)((status, movement) => status.step(movement))
    }

    def part1: Int = {
      warehouse.part1
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

    private def parsePlan(line: String): Seq[Movement] = {
      line.map(parseMovement)
    }

    private def parseMovement(char: Char): Movement = {
      Movement.values.find(_.char == char).get
    }
  }

  def part1(data: List[String]): Long = {
    val (warehouse, plan) = Parser.parse(data)
    val robot = warehouse.findRobot()
    val initial = State(warehouse, robot)
    println(warehouse)
    println(robot)
    println(plan)
    initial.run(plan).part1
  }

  def part2(data: List[String]): Long = {
    throw new UnsupportedOperationException("part2");
  }

  @main def main15(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day15.txt").asScala.toList;
    val part1 = Day15.part1(data);
    println(s"part1 = $part1");
    val part2 = Day15.part2(data);
    println(s"part2 = $part2");
  }
}
