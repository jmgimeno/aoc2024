package aoc2024s.day17

import utils.IO

import scala.annotation.tailrec
import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day17 {

  enum OpCode {
    case ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV
  }

  import OpCode.*

  object Disassembler {

    private def combo(arg: Int): String = arg match {
      case 0 | 1 | 2 | 3 => literal(arg)
      case 4 => "@a"
      case 5 => "@b"
      case 6 => "@c"
      case 7 => sys.error("invalid combo")
    }

    private def literal(arg: Int): String = s"#$arg"

    private def assembled(opCode: OpCode, arg: Int): String =
      opCode match {
        case ADV => s"ADV ${combo(arg)}"
        case BXL => s"BXL ${literal(arg)}"
        case BST => s"BST ${combo(arg)}"
        case JNZ => s"JNZ ${literal(arg)}"
        case BXC => s"BXC"
        case OUT => s"OUT ${combo(arg)}"
        case BDV => s"BDV ${combo(arg)}"
        case CDV => s"CDV ${combo(arg)}"
      }

    def disassemble(program: List[Int]): String =
      program.zipWithIndex.grouped(2).toList.map {
        case List((op, i), (arg, _)) => f"$i%2d : ${assembled(OpCode.fromOrdinal(op), arg)}"
        case _ => sys.error("invalid program")
      }.mkString("\n")
  }

  case class Computer(a: Int, b: Int, c: Int, ip: Int, output: List[Int]) {

    assert(a >= 0 && b >= 0 && c >= 0)

    private def combo(operand: Int): Int = operand match {
      case 0 | 1 | 2 | 3 => operand
      case 4 => a
      case 5 => b
      case 6 => c
      case 7 => sys.error("invalid combo")
    }

    def execute(op: OpCode, operand: Int): Computer =
      op match {
        case ADV =>
          copy(a = a >>> combo(operand), ip = ip + 2)
        case BXL =>
          copy(b = b ^ operand, ip = ip + 2)
        case BST =>
          copy(b = combo(operand) & 0x7, ip = ip + 2)
        case JNZ =>
          if a == 0 then copy(ip = ip + 2) else copy(ip = combo(operand))
        case BXC =>
          copy(b = b ^ c, ip = ip + 2)
        case OUT =>
          copy(output = output :+ (combo(operand) & 0x7), ip = ip + 2)
        case BDV =>
          copy(b = a >> combo(operand), ip = ip + 2)
        case CDV =>
          copy(c = a >> combo(operand), ip = ip + 2)
      }

    override def toString: String =
      s"Computer(a=$a, b=$b, c=$c, ip=$ip, output=\"${output.mkString(",")}\")"
  }

  object Executor {

    def run(computer: Computer, program: List[Int]): Computer = {
      @tailrec def go(
        computer: Computer,
      ): Computer = {
        if computer.ip >= program.length then computer
        else {
          val opcode = OpCode.fromOrdinal(program(computer.ip))
          val operand = program(computer.ip + 1)
          val computer1 = computer.execute(opcode, operand)
          go(computer1)
        }
      }

      go(computer)
    }

    private def combo(operand: Int, a: Int, b: Int, c: Int): Int = operand match {
      case 0 | 1 | 2 | 3 => operand
      case 4 => a
      case 5 => b
      case 6 => c
      case 7 => sys.error("invalid combo")
    }

    def runEager(initial: Computer, program: List[Int]): List[Int] = {
      var a = initial.a
      var b = initial.b
      var c = initial.c
      var output = mutable.ArrayBuffer.empty[Int]
      var ip = 0
      while (ip < program.length) {
        val opCode = OpCode.fromOrdinal(program(ip))
        val operand = program(ip + 1)
        opCode match {
          case ADV => {
            a = a >>> combo(operand, a, b, c)
            ip += 2
          }
          case BXL => {
            b = b ^ operand
            ip += 2
          }
          case BST => {
            b = combo(operand, a, b, c) & 0x7
            ip += 2
          }
          case JNZ => {
            if a == 0 then ip += 2 else ip = combo(operand, a, b, c)
          }
          case BXC => {
            b = b ^ c
            ip += 2
          }
          case OUT => {
            output += combo(operand, a, b, c) & 0x7
            ip += 2
          }
          case BDV => {
            b = a >> combo(operand, a, b, c)
            ip += 2
          }
          case CDV => {
            c = a >> combo(operand, a, b, c)
            ip += 2
          }
        }
      }
      output.toList
    }


    def runLazy(initial: Computer, program: List[Int]): LazyList[Int] = {

      def go(a: Int, b: Int, c: Int, ip: Int): LazyList[Int] = {
        if ip >= program.length then LazyList.empty
        else {
          val opCode = OpCode.fromOrdinal(program(ip))
          val operand = program(ip + 1)
          opCode match {
            case ADV => {
              val a1 = a >>> combo(operand, a, b, c)
              go(a1, b, c, ip + 2)
            }
            case BXL => {
              val b1 = b ^ operand
              go(a, b1, c, ip + 2)
            }
            case BST => {
              val b1 = combo(operand, a, b, c) & 0x7
              go(a, b1, c, ip + 2)
            }
            case JNZ => {
              if a == 0 then go(a, b, c, ip + 2)
              else go(a, b, c, combo(operand, a, b, c))
            }
            case BXC => {
              val b1 = b ^ c
              go(a, b1, c, ip + 2)
            }
            case OUT => {
              (combo(operand, a, b, c) & 0x7) #:: go(a, b, c, ip + 2)
            }
            case BDV => {
              val b1 = a >> combo(operand, a, b, c)
              go(a, b1, c, ip + 2)
            }
            case CDV => {
              val c1 = a >> combo(operand, a, b, c)
              go(a, b, c1, ip + 2)
            }
          }
        }
      }

      go(initial.a, initial.b, initial.c, initial.ip)
    }
  }

  private class FixPointFinder(computer: Computer, program: List[Int]) {

    private def nextMachine(n: Int): Computer = computer.copy(a = n)

    private def isFixedPoint(n: Int): Boolean =
      val machine = nextMachine(n)
      Executor.runLazy(machine, program) == program

    def fixPoint: Int =
      @tailrec
      def go(n: Int): Int =
        if isFixedPoint(n) then n
        else go(n + 1)

      go(0)
  }

  def parse(data: List[String]): (Computer, List[Int]) = {
    val List(a, b, c) = data.take(3).map(_.split(" ")(2).toInt)
    val program = data.last.split(": ")(1).split(",").map(_.toInt).toList
    (
      Computer(a, b, c, 0, List.empty),
      program
    )
  }

  def part1(data: List[String]): String = {
    val (computer, program) = parse(data)
    Executor.run(computer, program).output.mkString(",")
  }


  def part1Eager(data: List[String]): String = {
    val (computer, program) = parse(data)
    Executor.runEager(computer, program).mkString(",")
  }

  def part1Lazy(data: List[String]): String = {
    val (computer, program) = parse(data)
    Executor.runLazy(computer, program).mkString(",")
  }

  def part2(data: List[String]): Int = {
    val (computer, program) = parse(data)
    FixPointFinder(computer, program).fixPoint
  }

  @main def explorePart2(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day17.txt").asScala.toList
    val (computer, program) = parse(data)
    for n <- 0 to 10000 do
      val machine = computer.copy(a = n)
      val output = Executor.runEager(machine, program).mkString(",")
      println(s"n = $n -> output = $output")
  }

  @main def main17(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day17.txt").asScala.toList
    val part1 = Day17.part1(data)
    println(s"part1 = $part1")
    val part2 = Day17.part2(data)
    println(s"part2 = $part2")
  }
}
