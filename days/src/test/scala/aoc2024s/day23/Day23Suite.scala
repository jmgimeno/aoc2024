
package aoc2024s.day23

import utils.IO
import scala.jdk.CollectionConverters.*
import munit.FunSuite

class Day23Suite extends FunSuite {

  val example: String = """kh-tc
                          |qp-kh
                          |de-cg
                          |ka-co
                          |yn-aq
                          |qp-ub
                          |cg-tb
                          |vc-aq
                          |tb-ka
                          |wh-tc
                          |yn-cg
                          |kh-ub
                          |ta-co
                          |de-co
                          |tc-td
                          |tb-wq
                          |wh-td
                          |ta-ka
                          |td-qp
                          |aq-cg
                          |wq-ub
                          |ub-vc
                          |de-ta
                          |wq-aq
                          |wq-vc
                          |wh-yn
                          |ka-de
                          |kh-ta
                          |co-tc
                          |wh-qp
                          |tb-vc
                          |td-yn""".stripMargin

  test("part1 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day23.part1(data), 7)
  }

  test("part1 - input data") {
    val data = IO.getResourceAsList("aoc2024/day23.txt").asScala.toList
    assertEquals(Day23.part1(data), 1200)
  }

  test("part2 - example data") {
    val data = IO.splitLinesAsList(example).asScala.toList
    assertEquals(Day23.part2(data), -1L)
  }

  test("part2 - input data") {
    val data = IO.getResourceAsList("aoc2024/day23.txt").asScala.toList
    assertEquals(Day23.part2(data), -1L)
  }
}
