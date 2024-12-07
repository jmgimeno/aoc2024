
package aoc2024s.day7;

import utils.IO

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

object Day7 {

  enum Operation {
    case Value(value: Long)
    case Add, Multiply, Concatenate
  }

  import Operation.*

  case class Equation(target: Long, numbers: List[Operation])

  object Equation {

    def apply(line: String): Equation = {
      val parts = line.split(": ");
      val target = parts(0).toLong;
      val numbers = parts(1).split(" ").map(n => Value(n.toLong)).toList;
      Equation(target, numbers);
    }
  }

  extension [A](list: List[A])
    def sequences(size: Int): LazyList[List[A]] = {
      if (size == 0) LazyList(List.empty)
      else {
        val rest = sequences(size - 1)
        list.to(LazyList).flatMap { x => rest.map(x :: _) }
      }
    }
    def zipAppend(other: List[A]): List[A] = {
      assert(list.size == other.size)
      list.zip(other).flatMap { case (a, b) => List(a, b) }
    }

  object Combinator {

    def combine(numbers: List[Operation], ops: LazyList[List[Operation]]): LazyList[List[Operation]] = {
      ops match {
        case LazyList() => LazyList()
        case head #:: tail => {
          val combined = numbers.head :: head.zip(numbers.tail).flatMap { case (op, num) => List(op, num) }
          combined #:: combine(numbers, tail)
        }
      }
    }

    def canBeTrue1(equation: Equation): Boolean = {
      val intermediateOps = List(Add, Multiply).sequences(equation.numbers.size - 1)
      val possibleExpressions = combine(equation.numbers, intermediateOps)
      possibleExpressions.exists { expression =>
        evaluatesTo(expression, equation.target)
      }
    }

    def canBeTrue2(equation: Equation): Boolean = {
      // if (canBeTrue1(equation)) return true
      val intermediateOps = List(Add, Multiply, Concatenate).sequences(equation.numbers.size - 1)
      val possibleExpressions = combine(equation.numbers, intermediateOps)
      possibleExpressions.exists { expression =>
        evaluatesTo(expression, equation.target)
      }
    }

    def evaluatesTo(expression: List[Operation], target: Long): Boolean = {
      @tailrec
      def go(acc: Long, ops: List[Operation], numbers: List[Operation]): Boolean = {
        if (acc > target) false
        else
          numbers match {
            case Nil => acc == target
            case Add :: Value(value) :: tail => go(acc + value, ops, tail)
            case Multiply :: Value(value) :: tail => go(acc * value, ops, tail)
            case Concatenate :: Value(value) :: tail => go((acc.toString ++ value.toString).toLong, ops, tail)
            case _ => sys.error("Invalid expression")
          }
      }

      expression.head match {
        case Value(value) => go(value, Nil, expression.tail)
        case _ => sys.error("Invalid expression")
      }
    }
  }

  def part1(data: List[String]): Long = {
    data
      .map(Equation(_))
      .filter(Combinator.canBeTrue1)
      .map(_.target)
      .sum
  }

  def part2(data: List[String]): Long = {
    data
      .map(Equation(_))
      .filter(Combinator.canBeTrue2)
      .map(_.target)
      .sum
  }

  @main def main7(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day7.txt").asScala.toList;
    val part1 = Day7.part1(data);
    println(s"part1 = $part1");
    val part2 = Day7.part2(data);
    println(s"part2 = $part2");
  }
}