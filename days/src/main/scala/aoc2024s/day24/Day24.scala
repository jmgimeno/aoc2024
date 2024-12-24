package aoc2024s.day24

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day24 {

  class Graph {

    val vertices: mutable.HashSet[String] =
      mutable.HashSet.empty[String]

    val edges: mutable.HashMap[String, mutable.HashSet[String]] =
      mutable.HashMap.empty[String, mutable.HashSet[String]]

    def addVertex(vertex: String): Unit = {
      vertices += vertex
    }

    def addEdge(from: String, to: String): Unit = {
      vertices += from
      vertices += to
      edges.getOrElseUpdate(from, mutable.HashSet.empty[String]) += to
    }

    def topologicalSort: List[String] = {
        val visited = mutable.HashSet.empty[String]
        val stack = mutable.Stack[String]()

        def visit(vertex: String): Unit = {
            if (!visited.contains(vertex)) {
            visited += vertex
            edges.get(vertex).foreach { neighbors =>
                neighbors.foreach(visit)
            }
            stack.push(vertex)
            }
        }

        vertices.foreach(visit)
        stack.toList
    }
  }

  enum Gate {
    case AND(input1: String, input2: String, output: String)
    case OR(input1: String, input2: String, output: String)
    case XOR(input1: String, input2: String, output: String)
  }

  class Device(wires: Map[String, Int], gates: List[Gate]) {

    def toPrecedenceGraph: Graph = {
      val graph = new Graph
      wires.keys.foreach(graph.addVertex)
      gates.foreach {
        case Gate.AND(input1, input2, output) =>
          graph.addEdge(input1, output)
          graph.addEdge(input2, output)
        case Gate.OR(input1, input2, output) =>
          graph.addEdge(input1, output)
          graph.addEdge(input2, output)
        case Gate.XOR(input1, input2, output) =>
          graph.addEdge(input1, output)
          graph.addEdge(input2, output)
      }
      graph
    }

    def gateOrdering(topoSort: List[String]): Ordering[Gate] = {
        val order = topoSort.zipWithIndex.toMap
        Ordering.by[Gate, Int] {
            case Gate.AND(input1, input2, output) => order(output)
            case Gate.OR(input1, input2, output) => order(output)
            case Gate.XOR(input1, input2, output) => order(output)
        }
    }

    def run: Map[String, Int] = {
        val graph = toPrecedenceGraph
        val topoSort = graph.topologicalSort
        val gateOrdering = this.gateOrdering(topoSort)
        val gates = this.gates.sorted(gateOrdering)
        val values = mutable.HashMap.empty[String, Int]
        values ++= wires
        gates.foreach {
            case Gate.AND(input1, input2, output) =>
                values += (output -> (values(input1) & values(input2)))
            case Gate.OR(input1, input2, output) =>
                values += (output -> (values(input1) | values(input2)))
            case Gate.XOR(input1, input2, output) =>
                values += (output -> (values(input1) ^ values(input2)))
        }
        values.toMap
    }

    def part1: Long = {
        val wires = run
        val zWires = wires.keys.filter(_.startsWith("z")).toList.sorted.reverse
        zWires.foldLeft(0L) { (acc, wire) =>
            acc * 2 + wires(wire)
        }
    }
  }

  object Parser {
    def parse(data: List[String]): Device = {
      val (wiresData, gatesData) = data.span(_.nonEmpty)
      val wires = parseWires(wiresData)
      val gates = parseGates(gatesData.tail)
      Device(wires, gates)
    }

    def parseWires(data: List[String]): Map[String, Int] = {
      data.foldLeft(Map.empty[String, Int]) { (wires, line) =>
        // x00: 1 -> should map  "x00" to 1
        val Array(wire, value) = line.split(": ")
        wires + (wire -> value.toInt)
      }
    }

    def parseGates(data: List[String]): List[Gate] = {
      data.foldLeft(List.empty[Gate]) { (gates, line) =>
        // x00 AND y00 -> z00
        val Array(inputs, output) = line.split(" -> ")
        val Array(input1, op, input2) = inputs.split(" ")
        op match {
          case "AND" => Gate.AND(input1, input2, output) :: gates
          case "OR"  => Gate.OR(input1, input2, output) :: gates
          case "XOR" => Gate.XOR(input1, input2, output) :: gates
        }
      }
    }
  }

  def part1(data: List[String]): Long = {
    val device = Parser.parse(data)
    device.part1
  }

  def part2(data: List[String]): Long = {
    ??? // TODO
  }

  @main def main24(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day24.txt").asScala.toList
    val part1 = Day24.part1(data)
    println(s"part1 = $part1")
    val part2 = Day24.part2(data)
    println(s"part2 = $part2")
  }
}
