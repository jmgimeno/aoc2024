package aoc2024s.day19

import aoc2024s.day19.Day19.Trie.{Node, go}
import utils.IO

import scala.annotation.tailrec
import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day19 {

  enum Stripe {
    case B, U, R, G, W
  }

  enum Trie {
    case Empty
    case Node(children: Map[Stripe, Trie], isTerminal: Boolean = false)
  }

  type Pattern = List[Stripe]
  type Design = List[Stripe]

  object Trie {

    private val noChildren: Map[Stripe, Trie] =
      Map.WithDefault(Map.empty, _ => Trie.Empty)

    private val emptyNode = Node(noChildren)

    def insert(trie: Trie, path: Design): Trie = {
      (trie, path) match {
        case (Empty, Nil) =>
          Node(noChildren, true)
        case (Empty, s :: ss) =>
          Node(noChildren.updated(s, insert(emptyNode, ss)), false)
        case (node @ Node(_, _), Nil) =>
          node.copy(isTerminal = true)
        case (node @ Node(children, _), s :: ss) =>
          node.copy(children = children.updated(s, insert(children(s), ss)))
      }
    }

    @tailrec
    def go(start: Trie, path: Pattern): Option[Node] = {
      (start, path) match {
        case (Empty, _) => None
        case (node @ Node(_, _), Nil) => Some(node)
        case (Node(children, _), s :: ss) => go(children(s), ss)
      }
    }

    def from(designs: List[Design]): Trie = {
      designs.foldLeft(Empty) { (trie, design) =>
        insert(trie, design)
      }
    }

    def countTerminals(trie: Trie): Int = {
      trie match {
        case Empty => 0
        case Node(children, isTerminal) =>
          val terminals = if isTerminal then 1 else 0
          terminals + children.values.map(countTerminals).sum
      }
    }

    def countNodes(trie: Trie): Int = {
      trie match {
        case Empty => 0
        case Node(children, _) =>
          1 + children.values.map(countNodes).sum
      }
    }
  }

  object Parser {
    def parse(data: List[String]): (List[Pattern], List[Design]) = {
      val patterns = data.head.split(", ").map(parsePatternOrDesign).toList
      val designs = data.drop(2).map(parsePatternOrDesign)
      (patterns, designs)
    }

    private def parsePatternOrDesign(line: String): List[Stripe] =
      line.toCharArray.map(c => Stripe.valueOf(c.toUpper.toString)).toList
  }

  class Counter(trie: Trie, patterns: List[Pattern]) {

    private def neighbours(node: Trie) : List[Node] =
      patterns.flatMap { pattern =>
        go(node, pattern)
      }

    def countPossible: Int = {
      val start = trie.asInstanceOf[Node]
      val explored = mutable.Set.empty[Node]
      val stack = mutable.Stack(start)
      val possibles = mutable.Set.empty[Node]
      while stack.nonEmpty do {
        val current = stack.pop()
        if !explored.contains(current) then {
          explored += current
          if current.isTerminal then possibles += current
          neighbours(current).foreach { neighbour =>
            stack.push(neighbour)
          }
        }
      }
      possibles.size
    }
  }

  def part1(data: List[String]): Long = {
    val (patterns, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    Counter(trie, patterns).countPossible
  }

  def part2(data: List[String]): Long = {
    ??? // TODO
  }

  @main def main19(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day19.txt").asScala.toList
    val part1 = Day19.part1(data)
    println(s"part1 = $part1")
    val part2 = Day19.part2(data)
    println(s"part2 = $part2")
  }
}
