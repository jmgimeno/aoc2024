
package aoc2024s.day19

import aoc2024s.day19.Day19.Trie.{Node, countTerminals}
import aoc2024s.day19.Day19.{Parser, Trie}
import utils.IO

import scala.jdk.CollectionConverters.*
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

  test("in the example, the number of terminal nodes is the number of patterns") {
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
}
