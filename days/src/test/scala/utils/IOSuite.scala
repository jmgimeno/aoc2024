package utils

import scala.jdk.CollectionConverters.*

class IOSuite extends munit.FunSuite {
  test("IO") {
    val contents = IO.getResourceAsList("aoc/list.txt").asScala.toList
    val expected = List("alpha", "beta", "gamma")
    assertEquals(contents, expected)
  }
}