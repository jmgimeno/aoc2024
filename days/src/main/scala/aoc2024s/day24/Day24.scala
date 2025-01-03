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

  extension (gate: Gate) {

    def input1: String = gate match {
      case Gate.AND(input1, _, _) => input1
      case Gate.OR(input1, _, _)  => input1
      case Gate.XOR(input1, _, _) => input1
    }

    def input2: String = gate match {
      case Gate.AND(_, input2, _) => input2
      case Gate.OR(_, input2, _)  => input2
      case Gate.XOR(_, input2, _) => input2
    }

    def output: String = gate match {
      case Gate.AND(_, _, output) => output
      case Gate.OR(_, _, output)  => output
      case Gate.XOR(_, _, output) => output
    }
  }

  class Device(val wires: Map[String, Int], val gates: List[Gate]) {

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
        case Gate.OR(input1, input2, output)  => order(output)
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

  class Analyser(device: Device) {
    val inputs2Gates: Map[String, List[Gate]] =
      val byInput1 = device.gates.groupBy {
        _.input1
      }
      val byInput2 = device.gates.groupBy {
        _.input2
      }
      (byInput1.keySet ++ byInput2.keySet).map { input =>
        val gates1 = byInput1.getOrElse(input, List.empty)
        val gates2 = byInput2.getOrElse(input, List.empty)
        (input, gates1 ++ gates2)
      }.toMap

    val outputs2Gates: Map[String, List[Gate]] =
      device.gates.groupBy {
        _.output
      }

    private val inputX = inputs2Gates.keys.filter(_.startsWith("x")).toList.sorted
    private val inputY = inputs2Gates.keys.filter(_.startsWith("y")).toList.sorted
    private val outputZ = outputs2Gates.keys.filter(_.startsWith("z")).toList.sorted

    // The last bit should be the output of a OR gate (it's a carry)
    def condition1: Set[String] = {
      val last = outputZ.last
      outputs2Gates(last).find(_.isInstanceOf[Gate.OR]).map(_ => Set.empty[String]).getOrElse(Set(last))
    }

    // All other output bits should be the result of an XOR gate
    def condition2: Set[String] = {
      outputZ.init.filterNot { output =>
        val gates = outputs2Gates(output)
        assert(gates.length == 1)
        gates.head.isInstanceOf[Gate.XOR]
      }.toSet
    }

    // One output of (xi XOR yi) should be the input of the XOR gate that outputs zi
    // Except for the first one, because it has no carry
    def condition3: Set[String] = {
      inputX
        .flatMap(x => {
          val y = x.replace("x", "y")
          val xXOR = inputs2Gates(x).find(_.isInstanceOf[Gate.XOR]).get
          val yXOR = inputs2Gates(y).find(_.isInstanceOf[Gate.XOR]).get
          assert(xXOR == yXOR)
          val intermediate = xXOR.output
          val z = x.replace("x", "z")
          outputs2Gates(z).find(_.isInstanceOf[Gate.XOR]).flatMap { zXOR =>
            if zXOR.input1 == intermediate || zXOR.input2 == intermediate then None else Some(intermediate)
          }
        })
        .filterNot(_.endsWith("00"))
        .toSet
    }

    // The output of (xi AND yi) should go to OR gate
    // Except for the first one, because it has no carry
    def condition4: Set[String] = {
      device.gates
        .filter { gate =>
          gate.isInstanceOf[Gate.AND]
          && (gate.input1.startsWith("x") && gate.input2.startsWith("y")
            || gate.input1.startsWith("y") && gate.input2.startsWith("x"))
          && !gate.input1.endsWith("00") && !gate.input2.endsWith("00")
        }
        .flatMap { gate =>
          val output = gate.output
          if inputs2Gates.getOrElse(output, List.empty).forall(_.isInstanceOf[Gate.OR]) then None else Some(output)
        }
        .toSet
    }

    // AND gates should not belong to the output
    def condition5: Set[String] = {
      device.gates.collect {
        case Gate.AND(_, _, output) if output.startsWith("z") => output
      }.toSet
    }

    // OR gates should not belong to the output except for the last bit
    def condition6: Set[String] = {
      val other = outputZ.init
      other.filter { output =>
        val gates = outputs2Gates(output)
        gates.exists(_.isInstanceOf[Gate.OR])
      }.toSet
    }

    // An XOR gate either has x and y as inputs or z as an output
    // Except for the first one, because it has no carry
    def condition7: Set[String] = {
      device.gates
        .filter(_.isInstanceOf[Gate.XOR])
        .filterNot(gate => gate.input1.endsWith("00") || gate.input2.endsWith("00") || gate.output.endsWith("00"))
        .flatMap { gate =>
          val outputIsZ = gate.output.startsWith("z")
          val inputIsXAndY =
            gate.input1.startsWith("x") && gate.input2.startsWith("y")
              || gate.input1.startsWith("y") && gate.input2.startsWith("x")
          if outputIsZ || inputIsXAndY then None else Some(gate.output)
        }
        .toSet
    }

    def faultyGates: Set[String] = {
      println(s"condition1 = $condition1")
      println(s"condition2 = $condition2")
      println(s"condition3 = $condition3")
      println(s"condition4 = $condition4")
      println(s"condition5 = $condition5")
      println(s"condition6 = $condition6")
      println(s"condition7 = $condition7")
      condition1 ++ condition2 ++ condition3 ++ condition4 ++ condition5 ++ condition6 ++ condition7
    }
  }

  def part2(data: List[String]): String = {
    val device = Parser.parse(data)
    val analyser = new Analyser(device)
    val faultyGates = analyser.faultyGates
    assert(faultyGates.size == 8)
    faultyGates.toList.sorted.mkString(",")
  }

  @main def main24(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day24.txt").asScala.toList
    val part1 = Day24.part1(data)
    println(s"part1 = $part1")
    val part2 = Day24.part2(data)
    println(s"part2 = $part2")
  }
}
