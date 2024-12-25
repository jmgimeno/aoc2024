package aoc2024s.day22

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

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

    def prices(mask: BigInt): List[Int] = {
      (0 until changes.size - 3) // the last three does not count
        .filter(mask.testBit)
        .map(i => prices(i + 4))
        .toList
    }

    def maxBound(pattern: Pattern): Option[Int] = pattern match
      case Pattern.Zero => sys.error("zero pattern has no bound")
      case Pattern.One(one) =>
        val maskOnes = masks(one + 9)
        prices(maskOnes).maxOption
      case Pattern.Two(one, two) =>
        val maskOnes = masks(one + 9)
        val maskTwos = masks(two + 9)
        prices(maskOnes & maskTwos >> 1).maxOption
      case Pattern.Three(one, two, three) =>
        val maskOnes = masks(one + 9)
        val maskTwos = masks(two + 9)
        val maskThrees = masks(three + 9)
        prices(maskOnes & maskTwos >> 1 & maskThrees >> 2).maxOption
      case Pattern.Four(one, two, three, four) =>
        val maskOnes = masks(one + 9)
        val maskTwos = masks(two + 9)
        val maskThrees = masks(three + 9)
        val maskFours = masks(four + 9)
        prices(
          maskOnes & maskTwos >> 1 & maskThrees >> 2 & maskFours >> 3
        ).maxOption

    def evaluate(pattern: Pattern.Four): Option[Int] = {
      val Pattern.Four(one, two, three, four) = pattern
      val maskOnes = masks(one + 9)
      val maskTwos = masks(two + 9)
      val maskThrees = masks(three + 9)
      val maskFours = masks(four + 9)
      val mask = maskOnes & maskTwos >> 1 & maskThrees >> 2 & maskFours >> 3
      prices(mask).headOption
    }
  }

  class Optimizer(data: List[Long], size: Int = 2001) {

    private val changes = data.map(n => Changes(Sequence(n).prices(size)))

    case class Node(pattern: Pattern, maxBound: Int)

    given Ordering[Node] with
      def compare(n1: Node, n2: Node): Int =
        n1.maxBound - n2.maxBound // the greater the maxBound the greater the priority

    def maxBound(pattern: Pattern): Option[Int] = {
      val maxBounds = changes
        .map(_.maxBound(pattern))
        .filter(_.isDefined)
        .map(_.get)
        if maxBounds.isEmpty then None else Some(maxBounds.sum)
    }

    def evaluate(pattern: Pattern.Four): Option[Int] = {
      val matchedPrices = changes
        .map(_.evaluate(pattern))
        .filter(_.isDefined)
        .map(_.get)
      if matchedPrices.isEmpty then None else Some(matchedPrices.sum)
    }

    def greedyMinBound: Int = {
      val one = (-9 to +9).map(d => (maxBound(Pattern.One(d)), d)).filter(_._1.isDefined).map(p => (p._1.get, p._2)).max._2
      val two = (-9 to +9).map(d => (maxBound(Pattern.Two(one, d)), d)).filter(_._1.isDefined).map(p => (p._1.get, p._2)).max._2
      val three = (-9 to +9).map(d => (maxBound(Pattern.Three(one, two, d)), d)).filter(_._1.isDefined).map(p => (p._1.get, p._2)).max._2
      (-9 to +9).map(d => evaluate(Pattern.Four(one, two, three, d))).filter(_.isDefined).map(_.get).max
    }

    def max: Int = {
      var minBound = greedyMinBound
      val start = Node(Pattern.Zero, Int.MaxValue)
      val queue = mutable.PriorityQueue(start)
      while (queue.nonEmpty) {
        val current = queue.dequeue()
        current.pattern match
          case solution: Pattern.Four =>
            val value = evaluate(solution)
            if value.isDefined && value.get > minBound then minBound = value.get
          case pattern =>
            for (neighbour <- pattern.expand) {
              val bound = maxBound(neighbour)
              if bound.isDefined && bound.get > minBound then
                queue.enqueue(Node(neighbour, bound.get))
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
