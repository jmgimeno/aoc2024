
package aoc2024s.day7;

import utils.IO

import scala.annotation.tailrec
import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day7 {

  enum Operation {
    case Add, Multiply, Concatenate
  }

  import Operation.*

  case class Equation(target: Long, numbers: List[Long])

  object Equation {

    def apply(line: String): Equation = {
      val parts = line.split(": ");
      val target = parts(0).toLong;
      val numbers = parts(1).split(" ").map(n => n.toLong).toList;
      Equation(target, numbers);
    }
  }

  case class CachedCombinator[A](list: List[A]) {

    val cache: mutable.Map[Int, List[List[A]]] = mutable.Map.empty

    def apply(size: Int): List[List[A]] = {
      cache.getOrElse(size, {
        val result = generateSequence(size)
        cache.update(size, result)
        result
      })
    }

    def generateSequence(size: Int): List[List[A]] = {
      if (size == 0) List(List.empty)
      else {
        val rest = generateSequence(size - 1)
        rest.flatMap { tail => list.map(_ :: tail) }
      }
    }
  }

  object Combinator {

    def combineAndMatch(target: Long, numbers: List[Long], ops: List[Operation]): Boolean = {
      @tailrec
      def go(acc: Long, numbers: List[Long], ops: List[Operation]): Boolean = {
        if (acc > target) false
        else
          numbers match {
            case Nil => acc == target
            case head :: tail => ops match {
              case Add :: rest => go(acc + head, tail, rest)
              case Multiply :: rest => go(acc * head, tail, rest)
              case Concatenate :: rest => go((acc.toString ++ head.toString).toLong, tail, rest)
              case _ => sys.error("Invalid expression")
            }
          }
      }

      go(numbers.head, numbers.tail, ops)
    }

    def canBeTrue(cachedCombinator: CachedCombinator[Operation])(equation: Equation): Boolean = {
      val ops = cachedCombinator(equation.numbers.size - 1)
      ops.exists(combineAndMatch(equation.target, equation.numbers, _))
    }
  }

  def part1(data: List[String]): Long = {
    val combinator = CachedCombinator(List(Add, Multiply))
    data
      .map(Equation(_))
      .filter(Combinator.canBeTrue(combinator))
      .map(_.target)
      .sum
  }

  def part2(data: List[String]): Long = {
    val combinator = CachedCombinator(List(Add, Multiply, Concatenate))
    data
      .map(Equation(_))
      .filter(Combinator.canBeTrue(combinator))
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