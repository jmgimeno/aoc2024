
package aoc2024s.day22

import utils.IO
import scala.jdk.CollectionConverters.*

object Day22 {

  private val MODULUS = 16777216L - 1L

  def prune(n: Long): Long = {
    n & MODULUS
  }

  def next(n: Long): Long = {
    val step1 = prune((n << 6) ^ n)
    val step2 = prune((step1 >> 5) ^ step1)
    val step3 = prune((step2 << 11) ^ step2)
    step3
  }

  def ith(n: Long, i: Int): Long = {
    Iterator.iterate(n)(next).drop(i).next()
  }

  def part1(data: List[String]): Long = {
    data.map(line => ith(line.toLong, 2000)).sum
  }

  def part2(data: List[String]): Long = {
    ??? // TODO
  }

  @main def main22(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day22.txt").asScala.toList
    val part1 = Day22.part1(data)
    println(s"part1 = $part1")
    val part2 = Day22.part2(data)
    println(s"part2 = $part2")
  }
}
