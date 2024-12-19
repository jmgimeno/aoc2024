package aoc2024s.day19

import utils.IO

import scala.annotation.tailrec
import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day19 {

  enum Stripe {
    case B, U, R, G, W
  }

  type Pattern = List[Stripe]

  type Design = List[Stripe]

  class Trie {

    import Trie.*

    var root = new Node

    def insert(path: List[Stripe]): Unit = {
      @tailrec
      def go(path: List[Stripe], node: Node): Unit = {
        path match {
          case Nil => node.isTerminal = true
          case s :: ss => {
            if (node.children(s.ordinal) == null) then {
              node.children(s.ordinal) = new Node
            }
            go(ss, node.children(s.ordinal))
          }
        }
      }
      go(path, root)
    }

    def find(path: List[Stripe]): Option[Node] = {
      root.find(path)
    }
  }

  object Trie {

    class Node {
      val children: Array[Node] = Array.fill(Stripe.values.length)(null)
      var isTerminal: Boolean = false

      def find(path: Pattern): Option[Node] = {
        path match {
          case Nil                                    => Some(this)
          case s :: ss if children(s.ordinal) == null => None
          case s :: ss => children(s.ordinal).find(ss)
        }
      }
    }

    def from(patterns: List[List[Stripe]]): Trie = {
      val trie = new Trie
      patterns.foreach(pattern => trie.insert(pattern))
      trie
    }

    class Counter(trie: Trie, patterns: List[Pattern]) {
      private def expand(node: Node): List[Node] = {
        patterns.flatMap {
          node.find
        }
      }

      def countPossible: Int = {
        val start = trie.root
        val explored = mutable.Set.empty[Node]
        val stack = mutable.Stack(start)
        val possibles = mutable.Set.empty[Node]
        while stack.nonEmpty do {
          val current = stack.pop()
          if !explored.contains(current) then {
            explored += current
            if current.isTerminal then {
              possibles += current
            }
            expand(current).foreach { neighbour =>
              stack.push(neighbour)
            }
          }
        }
        possibles.size
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

  def part1(data: List[String]): Int = {
    val (patterns, designs) = Parser.parse(data)
    val trie = Trie.from(designs)
    Trie.Counter(trie, patterns).countPossible
  }

  def part2(data: List[String]): Int = {
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
