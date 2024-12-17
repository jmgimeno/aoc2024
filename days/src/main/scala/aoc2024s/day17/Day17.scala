package aoc2024s.day17

import utils.IO

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

object Day17 {

  type Register = Int
  type Output = IndexedSeq[Int]
  type Program = List[Int]

  enum OpCode {
    case ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV

  }

  import OpCode.*

  object Disassembler {

    private def combo(arg: Int): String = arg match {
      case 0 | 1 | 2 | 3  => literal(arg)
      case 4 => "@a"
      case 5 => "@b"
      case 6 => "@c"
      case 7 => sys.error("invalid combo")
    }

    private def literal(arg: Int): String = s"#$arg"

    def assembled(opCode: OpCode, arg: Int): String =
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

    def disassemble(program: Program): String =
      program.zipWithIndex.grouped(2).toList.map {
        case List((op, i), (arg, _)) => f"$i%2d : ${assembled(OpCode.fromOrdinal(op), arg)}"
        case List((op, i)) => f"$i%2d: assembled(OpCode.fromOrdinal(op), 0)}"
      }.mkString("\n")
  }

  case class Computer(a: Register, b: Register, c: Register, ip: Int, output: Output) {

    assert(a >= 0 && b >= 0 && c >= 0)

    def combo(operand: Int): Int = operand match {
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

    def run(computer: Computer, program: Program): Computer = {
      @tailrec def go(
        computer: Computer,
        program: Program
      ): Computer = {
        if computer.ip >= program.length then computer
        else {
          val opcode = OpCode.fromOrdinal(program(computer.ip))
          val operand = program(computer.ip + 1)
          val computer1 = computer.execute(opcode, operand)
          go(computer1, program)
        }
      }
      go(computer, program)
    }
  }

  def parse(data: List[String]): (Computer, Program) = {
    val registers = data.take(3).map(_.split(" ")(2).toInt)
    val program = data.last.split(": ")(1).split(",").map(_.toInt).toList
    (
      Computer(registers(0), registers(1), registers(2), 0, IndexedSeq.empty),
      program
    )
  }

  def part1(data: List[String]): String = {
    val (computer, program) = parse(data)
    Executor.run(computer, program).output.mkString(",")
  }

  def part2(data: List[String]): Long = {
    throw new UnsupportedOperationException("part2")
  }

  @main def main17(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day17.txt").asScala.toList
    val part1 = Day17.part1(data)
    println(s"part1 = $part1")
    val part2 = Day17.part2(data)
    println(s"part2 = $part2")
  }
}
