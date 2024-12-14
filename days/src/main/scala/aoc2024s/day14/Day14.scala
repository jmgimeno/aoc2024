
package aoc2024s.day14;

import utils.IO;
import scala.jdk.CollectionConverters.*

object Day14 {

  enum Quadrant {
    case NW, NE, SE, SW
  }

  case class Space(width: Int, height: Int) {

    def parse(data: List[String]): State = {
      State(data.map(parse), this)
    }

    def parse(line: String): Robot = {
      val pattern = "p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)".r
      line match {
        case pattern(x, y, dx, dy) => Robot(x.toInt, y.toInt, dx.toInt, dy.toInt)
        case _ => throw new IllegalArgumentException(s"Invalid line: $line")
      }
    }
  }

  case class Robot(x: Int, y: Int, dx: Int, dy: Int) {
    def run(steps: Int, space: Space): Robot = {
      val newX = (x + dx * steps) % space.width;
      val newY = (y + dy * steps) % space.height;
      copy(
        x = if newX < 0 then newX + space.width else newX,
        y = if newY < 0 then newY + space.height else newY
      )
    }

    def quadrant(space: Space): Option[Quadrant] = {
      val halfHeight = space.height / 2
      val halfWidth = space.width / 2
      if y < halfHeight && x < halfWidth then Some(Quadrant.NW)
      else if y < halfHeight && x > halfWidth then Some(Quadrant.NE)
      else if y > halfHeight && x > halfWidth then Some(Quadrant.SE)
      else if y > halfHeight && x < halfWidth then Some(Quadrant.SW)
      else None
    }
  }

  case class State(robots: List[Robot], space: Space) {
    def run(steps: Int = 1): State = {
      copy(robots = robots.map(_.run(steps, space)))
    }

    def safety: Int = {
      val quadrants = robots
        .flatMap(_.quadrant(space))
        .groupBy(identity)
        .view.mapValues(_.size)
        .toMap
      // We need to do it this way to get a 0 when a quadrant is missing
      Quadrant.values.map(quadrants.getOrElse(_, 0)).product
    }
  }

  def part1(data: List[String]): Int = {
    Space(101, 103).parse(data).run(100).safety
  }

  def part2(data: List[String]): Int = {
    val initial = Space(101, 103).parse(data)
    LazyList
      .iterate(initial)(_.run())
      .map(_.safety)
      .take(101 * 103)
      .zipWithIndex
      .minBy(_._1)
      ._2
  }

  @main def main14(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day14.txt").asScala.toList;
    val part1 = Day14.part1(data);
    println(s"part1 = $part1");
    val part2 = Day14.part2(data);
    println(s"part2 = $part2");
  }
}
