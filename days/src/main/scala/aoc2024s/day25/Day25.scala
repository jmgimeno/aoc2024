package aoc2024s.day25

import utils.IO

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

object Day25 {

  case class Lock(p1: Int, p2: Int, p3: Int, p4: Int, p5: Int)
  case class Key(h1: Int, h2: Int, h3: Int, h4: Int, h5: Int)

  private def fits(l: Lock, k: Key): Boolean = {
    l.p1 + k.h1 <= 5 && l.p2 + k.h2 <= 5 && l.p3 + k.h3 <= 5 && l.p4 + k.h4 <= 5 && l.p5 + k.h5 <= 5
  }

  object Parser {
    def parse(data: List[String]): (List[Lock], List[Key]) = {
      val shapes = data.grouped(8).map(_.take(7))
      parseShapes(shapes.toList)
    }

    private def parseShapes(
        shapes: List[List[String]]
    ): (List[Lock], List[Key]) = {
      val locks = mutable.ArrayBuffer.empty[Lock]
      val keys = mutable.ArrayBuffer.empty[Key]
      shapes.foreach { shape =>
        if (shape.head == "#####") {
          locks += parseLock(shape.tail)
        } else if (shape.head == ".....") {
          keys += parseKey(shape.init)
        }
      }
      (locks.toList, keys.toList)
    }

    private def countColumns(shape: List[String], col: Int) = {
      shape.count(_(col) == '#')
    }

    private def parseLock(lock: List[String]) = {
      val cols = (0 until 5).map(countColumns(lock, _))
      Lock(cols(0), cols(1), cols(2), cols(3), cols(4))
    }

    private def parseKey(init: List[String]) = {
      val cols = (0 until 5).map(countColumns(init, _))
      Key(cols(0), cols(1), cols(2), cols(3), cols(4))
    }
  }

  def part1(data: List[String]): Int = {
    val (locks, keys) = Parser.parse(data)
    countFits(locks, keys)
  }

  private def countFits(locks: List[Lock], keys: List[Key]) = {
    (for {
      lock <- locks
      key <- keys
      if fits(lock, key)
    } yield 1).sum
  }

  @main def main25(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day25.txt").asScala.toList
    val part1 = Day25.part1(data)
    println(s"part1 = $part1")
  }
}
