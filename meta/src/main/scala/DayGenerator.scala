import scala.io.StdIn.readLine

object DayGenerator {

  def javaProduction(day: Int): String = s"""
    |package aoc2024j.day$day;
    |
    |import utils.IO;
    |import java.util.List;
    |
    |public class Day$day {
    |
    |    long part1(List<String> data) {
    |        throw new UnsupportedOperationException("part1");
    |    }
    |
    |    long part2(List<String> data) {
    |        throw new UnsupportedOperationException("part2");
    |    }
    |
    |    public static void main() {
    |        var day$day = new Day$day();
    |        var data = IO.getResourceAsList("aoc2024/day$day.txt");
    |        var part1 = day$day.part1(data);
    |        System.out.println("part1 = " + part1);
    |        var part2 = day$day.part2(data);
    |        System.out.println("part2 = " + part2);
    |    }
    |}""".stripMargin

  def javaTest(day: Int): String = s"""
    |package aoc2024j.day$day;
    |
    |import utils.IO;
    |
    |import org.junit.jupiter.api.DisplayName;
    |import org.junit.jupiter.api.Test;
    |
    |import static org.junit.jupiter.api.Assertions.assertEquals;
    |
    |class Day${day}Test {
    |
    |    static final String example =
    |            \"\"\"
    |            \"\"\";
    |
    |    static final Day$day day$day = new Day$day();
    |
    |    @Test
    |    @DisplayName("part1 - example data")
    |    void test1() {
    |        var data = IO.splitLinesAsList(example);
    |        assertEquals(-1L, day$day.part1(data));
    |    }
    |
    |    @Test
    |    @DisplayName("part1 - input data")
    |    void test2() {
    |        var data = IO.getResourceAsList("aoc2024/day$day.txt");
    |        assertEquals(-1L, day$day.part1(data));
    |    }
    |
    |    @Test
    |    @DisplayName("part2 - example data")
    |    void test3() {
    |        var data = IO.splitLinesAsList(example);
    |        assertEquals(-1L, day$day.part2(data));
    |    }
    |
    |    @Test
    |    @DisplayName("part2 - input data")
    |    void test4() {
    |        var data = IO.getResourceAsList("aoc2024/day$day.txt");
    |        assertEquals(-1L, day$day.part2(data));
    |    }
    |}""".stripMargin

  def generateJava(day: Int): Unit = {
    val production = s"days/src/main/java/aoc2024j/day$day/Day$day.java"
    val test = s"days/src/test/java/aoc2024j/day$day/Day${day}Test.java"
    val productionFile = new java.io.File(production)
    val testFile = new java.io.File(test)
    productionFile.getParentFile.mkdirs()
    testFile.getParentFile.mkdirs()
    java.nio.file.Files.write(productionFile.toPath, javaProduction(day).getBytes)
    java.nio.file.Files.write(testFile.toPath, javaTest(day).getBytes)
  }

  def scalaProduction(day: Int): String = s"""
    |package aoc2024s.day$day;
    |
    |import utils.IO;
    |import scala.jdk.CollectionConverters.*
    |
    |object Day$day {
    |
    |    def part1(data: List[String]): Long = {
    |      throw new UnsupportedOperationException("part1");
    |    }
    |
    |    def part2(data: List[String]): Long = {
    |      throw new UnsupportedOperationException("part2");
    |    }
    |
    |    @main def main$day(): Unit = {
    |      val data = IO.getResourceAsList("aoc2024/day$day.txt").asScala.toList;
    |      val part1 = Day$day.part1(data);
    |      println(s"part1 = $$part1");
    |//        val part2 = Day$day.part2(data);
    |//        println(s"part2 = $$part2");
    |    }
    |}""".stripMargin

  def scalaTest(day: Int): String = s"""
    |package aoc2024s.day$day
    |
    |import utils.IO
    |import scala.jdk.CollectionConverters.*
    |import munit.FunSuite
    |
    |class Day${day}Suite extends FunSuite {
    |
    |  val example: String = \"\"\"\"\"\".stripMargin
    |
    |  test("part1 - example data") {
    |    val data = IO.splitLinesAsList(example).asScala.toList
    |    assertEquals(Day$day.part1(data), -1L)
    |  }
    |
    |  test("part1 - input data") {
    |    val data = IO.getResourceAsList("aoc2024/day$day.txt").asScala.toList
    |    assertEquals(Day$day.part1(data), -1L)
    |  }
    |
    |  test("part2 - example data") {
    |    val data = IO.splitLinesAsList(example).asScala.toList
    |    assertEquals(Day$day.part2(data), -1L)
    |  }
    |
    |  test("part2 - input data") {
    |    val data = IO.getResourceAsList("aoc2024/day$day.txt").asScala.toList
    |    assertEquals(Day$day.part2(data), -1L)
    |  }
    |}""".stripMargin

  def generateScala(day: Int): Unit = {
    val production = s"days/src/main/scala/aoc2024s/day$day/Day$day.scala"
    val test = s"days/src/test/scala/aoc2024s/day$day/Day${day}Suite.scala"
    val productionFile = new java.io.File(production)
    val testFile = new java.io.File(test)
    productionFile.getParentFile.mkdirs()
    testFile.getParentFile.mkdirs()
    java.nio.file.Files.write(productionFile.toPath, scalaProduction(day).getBytes)
    java.nio.file.Files.write(testFile.toPath, scalaTest(day).getBytes)
  }

  def downloadData(day: Int): Unit = {
    val url = s"https://adventofcode.com/2024/day/$day/input"
    val cookie = sys.env("AOC_COOKIE")
    val data = requests.get(url, headers = Map("Cookie" -> cookie)).text()
    val file = s"days/src/main/resources/aoc2024/day$day.txt"
    java.nio.file.Files.write(new java.io.File(file).toPath, data.getBytes)
  }

  @main def runJava(): Unit = {
    val day = readLine("Enter day number: ").toInt
    val production = new java.io.File(s"days/src/main/java/aoc2024j/day$day")
    if (production.exists()) {
      println("Day already exists")
    } else {
      generateJava(day)
      downloadData(day)
    }
  }

  @main def runScala(): Unit = {
    val day = readLine("Enter day number: ").toInt
    val production = new java.io.File(s"days/src/main/scala/aoc2024s/day$day")
    if (production.exists()) {
      println("Day already exists")
    } else {
      generateScala(day)
      downloadData(day)
    }
  }

  @main def runDay(): Unit = {
    val day = readLine("Enter day number: ").toInt
    val resource = new java.io.File(s"days/src/main/resources/aoc2024/day$day.txt")
    resource.getParentFile.mkdirs()
    if (resource.exists()) {
      println("Day already exists")
    } else {
      downloadData(day)
    }
  }
}