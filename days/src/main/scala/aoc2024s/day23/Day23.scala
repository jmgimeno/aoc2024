package aoc2024s.day23

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day23 {

  case class Triangle(n1: String, n2: String, n3: String) {
    def toList: List[String] = List(n1, n2, n3)
  }

  object Triangle {
    given Ordering[Triangle] with
      def compare(t1: Triangle, t2: Triangle): Int =
        val n1 = t1.toList.min
        val n2 = t2.toList.min
        n1.compareTo(n2)
  }

  class Graph(
      name2id: Map[String, Int],
      id2name: Map[Int, String],
      edges: Map[Int, List[Int]]
  ) {

    def triangles: List[Triangle] = {
      val result = mutable.ArrayBuffer.empty[Triangle]
      val n = id2name.keys.size
      for (v <- 0 to n - 3) {
        val marked = edges(v).filter(_ > v).toSet
        for (u <- marked) {
          for (w <- edges(u).filter(_ > u)) {
            if marked.contains(w) then {
              result += Triangle(id2name(v), id2name(u), id2name(w))
            }
          }
        }
      }
      result.toList
    }

    override def toString: String = {
      s"name2id = $name2id\n id2name = $id2name\n edges = $edges"
    }
  }

  private class GraphParser(lines: List[String]) {
    private val rawNodes = mutable.HashSet.empty[String]
    private val rawEdges =
      mutable.HashMap.empty[String, mutable.ArrayBuffer[String]]

    def parse: Graph = {
      parseLines(lines)
      val sortedNodes = rawNodes.toList.sortBy(n => rawEdges(n).length)
      val name2id = sortedNodes.zipWithIndex.toMap
      val id2name = sortedNodes.zipWithIndex.map(_.swap).toMap
      val edges = rawEdges.map { (rawNode, rawEdges) =>
        (name2id(rawNode), rawEdges.map(name2id).toList)
      }.toMap
      Graph(name2id, id2name, edges)
    }

    private def parseLines(lines: List[String]): Unit = {
      lines.foreach(parseLine)
    }

    private def parseLine(line: String): Unit = {
      val Array(left, right) = line.split("-")
      rawNodes += left
      rawNodes += right
      rawEdges.getOrElseUpdate(left, mutable.ArrayBuffer.empty) += right
      rawEdges.getOrElseUpdate(right, mutable.ArrayBuffer.empty) += left
    }
  }

  def part1(data: List[String]): Int = {
    val graph = GraphParser(data).parse
    val triangles = graph.triangles
    val filtered = triangles.filter(_.toList.exists(_.startsWith("t")))
    filtered.length
  }

  def part2(data: List[String]): Long = {
    ??? // TODO
  }

  @main def main23(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day23.txt").asScala.toList
    val part1 = Day23.part1(data)
    println(s"part1 = $part1")
    val part2 = Day23.part2(data)
    println(s"part2 = $part2")
  }
}
