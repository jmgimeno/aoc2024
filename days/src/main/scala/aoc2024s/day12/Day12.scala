
package aoc2024s.day12;

import utils.IO

import scala.annotation.targetName
import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day12 {

  class PlantMap(data: List[String]) {
    val points: Vector[String] = data.toVector
    val height: Int = points.size
    val width: Int = points(0).length

    def findRegions: Regions = {
      val regions = new Regions(this)
      val assigned = Array.fill(height, width)(false)
      for {
        y <- 0 until height
        x <- 0 until width
        if !assigned(y)(x)
      } {
        val plant = points(y)(x)
        val region = expandRegion(x, y, assigned)
        regions.addRegion(plant, region)
      }
      regions
    }

    def expandRegion(x: Int, y: Int, assigned: Array[Array[Boolean]]): Region = {
      val region = new Region(GardenPlot(x, y))
      val queue = mutable.Queue(GardenPlot(x, y))
      assigned(y)(x) = true
      while (queue.nonEmpty) {
        val plot = queue.dequeue()
        for {
          neighbor <- plot.neighbors
          if neighbor.x >= 0 && neighbor.x < width && neighbor.y >= 0 && neighbor.y < height
          if !assigned(neighbor.y)(neighbor.x)
          if points(neighbor.y)(neighbor.x) == points(y)(x)
        } {
          assigned(neighbor.y)(neighbor.x) = true
          region.add(neighbor)
          queue.enqueue(neighbor)
        }
      }
      region
    }

    def get(pos: GardenPlot): Int =
      if (pos.x < 0 || pos.x >= width || pos.y < 0 || pos.y >= height) -1
      else points(pos.y)(pos.x)

    def isCorner(direction1: Direction, direction2: Direction)(pos: GardenPlot): Boolean =
      val value = get(pos)
      val side1 = get(direction1(pos))
      val side2 = get(direction2(pos))
      val corner = get((direction1 + direction2)(pos))
      corner != value && side1 == value && side2 == value
        || corner != value && side1 != value && side2 != value
        || corner == value && side1 != value && side2 != value

  }

  class Regions(plantMap: PlantMap) {
    val regions: mutable.HashMap[Char, List[Region]] = mutable.HashMap.empty

    def addRegion(plant: Char, region: Region): Unit = {
      regions.get(plant) match {
        case Some(other) => regions.put(plant, region :: other)
        case None => regions.put(plant, List(region))
      }
    }

    def price1: Long =
      regions.valuesIterator.flatten.map(_.price1).sum

    def price2: Long =
      regions.valuesIterator.flatten.map(_.price2(plantMap)).sum
  }

  class Region {
    val plots: mutable.ArrayBuffer[GardenPlot] = mutable.ArrayBuffer.empty

    def this(plot: GardenPlot) = {
      this()
      plots += plot
    }

    def add(plot: GardenPlot): Unit = {
      plots += plot
    }

    def area: Long = plots.size

    def perimeter: Long =
      plots.flatMap(_.neighbors).filterNot(plots.contains).size

    def sides(plantMap: PlantMap): Long =
      (for {
        vertical <- List(Direction.N, Direction.S)
        horizontal <- List(Direction.W, Direction.E)
        isCorner = plantMap.isCorner(vertical, horizontal)
        plot <- plots
        if isCorner(plot)
      } yield ()).size

    def price1: Long = area * perimeter

    def price2(plantMap: PlantMap): Long = area * sides(plantMap)
  }

  enum Direction(val dx: Int, val dy: Int) extends (GardenPlot => GardenPlot) {
    case N extends Direction(0, -1)
    case S extends Direction(0, 1)
    case E extends Direction(1, 0)
    case W extends Direction(-1, 0)

    def apply(plot: GardenPlot): GardenPlot =
      GardenPlot(plot.x + dx, plot.y + dy)

    @targetName("andThen")
    def +(other: Direction): GardenPlot => GardenPlot = compose(other)
  }

  case class GardenPlot(x: Int, y: Int) {
    def neighbors: Iterator[GardenPlot] =
      Iterator(
        GardenPlot(x - 1, y),
        GardenPlot(x + 1, y),
        GardenPlot(x, y - 1),
        GardenPlot(x, y + 1)
      )
  }

  def part1(data: List[String]): Long = {
    val map = new PlantMap(data)
    val regions = map.findRegions
    regions.price1
  }

  def part2(data: List[String]): Long = {
    val map = new PlantMap(data)
    val regions = map.findRegions
    regions.price2
  }

  @main def main12(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day12.txt").asScala.toList;
    val part1 = Day12.part1(data);
    println(s"part1 = $part1");
    val part2 = Day12.part2(data);
    println(s"part2 = $part2");
  }
}