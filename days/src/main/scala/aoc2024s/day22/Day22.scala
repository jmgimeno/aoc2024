package aoc2024s.day22

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*
import scala.util.Random

object Day22 {

  class Sequence(start: Long) {

    private val MODULUS = 16777216L - 1L

    private def prune(n: Long): Long = {
      n & MODULUS
    }

    private def next(n: Long): Long = {
      val step1 = prune((n << 6) ^ n)
      val step2 = prune((step1 >> 5) ^ step1)
      val step3 = prune((step2 << 11) ^ step2)
      step3
    }

    def nth(n: Int): Long = {
      Iterator.iterate(start)(next).drop(n).next()
    }

    def prices(n: Int): Vector[Int] = {
      Iterator.iterate(start)(next).take(n).map(l => (l % 10).toInt).toVector
    }
  }

  enum Pattern {
    case Zero
    case One(one: Int)
    case Two(one: Int, two: Int)
    case Three(one: Int, two: Int, three: Int)
    case Four(one: Int, two: Int, three: Int, four: Int)

    def expand: Seq[Pattern] = {
      val diffs = -9 to +9
      this match {
        case Zero                   => diffs.map(One(_))
        case One(one)               => diffs.map(Two(one, _))
        case Two(one, two)          => diffs.map(Three(one, two, _))
        case Three(one, two, three) => diffs.map(Four(one, two, three, _))
        case _ => sys.error("the pattern is already complete")
      }
    }
  }

  class Changes(prices: Vector[Int]) {

    val changes: Vector[Int] =
      prices.sliding(2).map { case Vector(a, b) => b - a }.toVector

    val masks: Array[BigInt] =
      Array.fill(19)(BigInt(0)) // one mask per difference, from -9 to +9

    changes.zipWithIndex.foreach((c, i) =>
      masks(c + 9) = masks(c + 9).setBit(i)
    )

    def indices(mask: BigInt): List[Int] = {
      (0 to changes.size)
        .filter(mask.testBit)
        .toList // the last three does not count
    }

    def maxBound(pattern: Pattern): Int = pattern match
      case Pattern.Zero => 9 // maximum value for a difference
      case Pattern.One(one) =>
        val maskOnes = masks(one + 9)
        indices(maskOnes)
          .filter(_ < prices.size - 4)
          .map(i => prices(i + 4))
          .maxOption.getOrElse(0)
      case Pattern.Two(one, two) =>
        val maskOnes = masks(one + 9)
        val maskTwos = masks(two + 9)
        indices(maskOnes & maskTwos >> 1)
          .filter(_ < prices.size - 4)
          .map(i => prices(i + 4))
          .maxOption.getOrElse(0)
      case Pattern.Three(one, two, three) =>
        val maskOnes = masks(one + 9)
        val maskTwos = masks(two + 9)
        val maskThrees = masks(three + 9)
        indices(maskOnes & maskTwos >> 1 & maskThrees >> 2)
          .filter(_ < prices.size - 4)
          .map(i => prices(i + 4))
          .maxOption.getOrElse(0)
      case Pattern.Four(one, two, three, four) =>
        val maskOnes = masks(one + 9)
        val maskTwos = masks(two + 9)
        val maskThrees = masks(three + 9)
        val maskFours = masks(four + 9)
        indices(maskOnes & maskTwos >> 1 & maskThrees >> 2 & maskFours >> 3)
          .filter(_ < prices.size - 4)
          .map(i => prices(i + 4))
          .maxOption.getOrElse(0)

    def evaluate(pattern: Pattern.Four): Int = {
      val Pattern.Four(one, two, three, four) = pattern
      val maskOnes = masks(one + 9)
      val maskTwos = masks(two + 9)
      val maskThrees = masks(three + 9)
      val maskFours = masks(four + 9)
      val mask = maskOnes & maskTwos >> 1 & maskThrees >> 2 & maskFours >> 3
      val found = indices(mask)
      if found.isEmpty then 0
      else prices(found.head + 4)
    }
  }

  class Optimizer(data: List[Long], size: Int = 2001) {

    private val changes = data.map(n => Changes(Sequence(n).prices(size)))

    case class Node(pattern: Pattern, maxBound: Int)

    given Ordering[Node] with
      def compare(n1: Node, n2: Node): Int =
        n1.maxBound - n2.maxBound // the greater the maxBound the greater the priority

    def randomPattern: Pattern.Four = {
      val random = Random()
      val one = random.between(-9, 10)
      val two = random.between(-9, 10)
      val three = random.between(-9, 10)
      val four = random.between(-9, 10)
      Pattern.Four(one, two, three, four)
    }

    def maxBound(pattern: Pattern): Int = {
      changes.map(c => c.maxBound(pattern)).sum
    }

    def evaluate(pattern: Pattern.Four): Int = {
      changes.map(c => c.evaluate(pattern)).sum
    }

    def max: Int = {
      var minBound = evaluate(randomPattern)
      val start = Node(Pattern.Zero, maxBound(Pattern.Zero))
      val queue = mutable.PriorityQueue(start)
      while (queue.nonEmpty) {
        val current = queue.dequeue()
        current.pattern match
          case solution: Pattern.Four =>
            val value = evaluate(solution)
            if value > minBound then minBound = value
          case pattern =>
            for (neighbour <- pattern.expand) {
              val bound = maxBound(neighbour)
              if bound > minBound then queue.enqueue(Node(neighbour, bound))
            }
      }
      minBound
    }
  }

  def parse(data: List[String]): List[Long] = data.map(_.toLong)

  def part1(data: List[String]): Long = {
    parse(data).map(Sequence(_).nth(2000)).sum
  }

  def part2(data: List[String]): Int = {
    val parsed = parse(data)
    val changes = parsed.map(Sequence(_).prices(2000))
    Optimizer(parsed).max
  }

  @main def main22(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    val part1 = Day22.part1(data)
    println(s"part1 = $part1")
    val part2 = Day22.part2(data)
    println(s"part2 = $part2")
  }
}
