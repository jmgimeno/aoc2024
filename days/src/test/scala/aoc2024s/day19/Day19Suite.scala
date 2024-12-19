package aoc2024s.day19

import aoc2024s.day19.Day19.Trie.{Node, countNodes, countTerminals}
import aoc2024s.day19.Day19.{Parser, Trie}
import utils.IO

import scala.jdk.CollectionConverters.*
import scala.util.Random
import munit.FunSuite

class Day19Suite extends FunSuite {

  val example: String = """r, wr, b, g, bwu, rb, gb, br
                          |
                          |brwrr
                          |bggr
                          |gbbr
                          |rrbgbr
                          |ubwu
                          |bwurrg
                          |brgr
                          |bbrgwb""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day19.part1(data), 6L)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day19.txt").asScala.toList
    assertEquals(Day19.part1(data), -1L)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day19.part2(data), -1L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day19.txt").asScala.toList
    assertEquals(Day19.part2(data), -1L)
  }

  test("all the designs in the example are found in the trie") {
    val data = IO.splitLinesAsList(example).asScala.toList
    val (_, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    assert {
      designs.forall { d =>
        println(d)
        Trie.find(trie, d).isDefined
      }
    }
  }

  test("all the designs in the input are found in the trie") {
    val data = IO.getResourceAsList("aoc2024/day19.txt").asScala.toList
    val (_, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    assert {
      designs.forall { d =>
        println(d)
        Trie.find(trie, d).isDefined
      }
    }
  }

  test(
    "in the example, the number of terminal nodes is the number of patterns"
  ) {
    val data = IO.splitLinesAsList(example).asScala.toList
    val (_, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    assertEquals(countTerminals(trie), designs.length)
  }

  test("in the input, the number of terminal nodes is the number of patterns") {
    val data = IO.getResourceAsList("aoc2024/day19.txt").asScala.toList
    val (_, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    assertEquals(countTerminals(trie), designs.length)
  }

  test("any permutation of the same designs create the same trie") {
    val data = IO.splitLinesAsList(example).asScala.toList
    val (_, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    val randomizedDesigns = Random.shuffle(designs)
    val randomizedTrie = Trie.from(randomizedDesigns)
    assertEquals(trie, randomizedTrie)
  }

  test("the example trie has 35 nodes ans 8 terminals") {
    val data = IO.splitLinesAsList(example).asScala.toList
    val (_, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    assertEquals(countNodes(trie), 35)
    assertEquals(countTerminals(trie), 8)
  }

  test("trie with a single pattern") {
    import Day19.Stripe.*
    val pattern = List(B, U, R, W)
    val trie = Trie.from(List(pattern))
    assertEquals(countNodes(trie), 5) // Root is not counted !!
    assertEquals(countTerminals(trie), 1)
    assert(Trie.find(trie, pattern).get.isTerminal)
    assert(!Trie.find(trie, List(B, U, R)).get.isTerminal)
    assert(Trie.find(trie, List(B, U, W)).isEmpty)
    assert(Trie.find(trie, List(B, U, R, U)).isEmpty)
  }

}
