
package aoc2024s.day5;

import utils.IO

import scala.jdk.CollectionConverters.*
import scala.util.chaining.scalaUtilChainingOps

object Day5 {

  case class Rules(afters: Map[Int, Set[Int]]) {

    def part1(updates: Updates): Long =
      updates.updates
        .filter(isValid)
        .map(_.middle)
        .sum

    def isValid(update: Update): Boolean =
      update.pages
        .tails
        .filter(_.nonEmpty)
        .forall: suffix =>
          suffix.tail
            .forall: page =>
              afters.getOrElse(suffix.head, Set.empty).contains(page)

    def ordering: Ordering[Int] = (i, j) =>
      if (i == j) 0
      else if (afters.getOrElse(i, Set.empty).contains(j)) -1
      else 1


    def part2(updates: Updates): Long =
      updates.updates
        .filterNot(isValid)
        .map(_.sorted(ordering))
        .map(_.middle)
        .sum
  }

  object Rules {

    def parse(ruleLines: List[String]): Rules =
      ruleLines
        .map(line => line.split('|').map(_.toInt))
        .groupMapReduce(_(0))(a => Set(a(1)))(_ ++ _)
        .pipe(Rules.apply)
  }

  case class Updates(updates: List[Update])

  object Updates {

    def parse(updateLines: List[String]): Updates =
      updateLines
        .map(Update.parse)
        .pipe(Updates.apply)
  }

  case class Update(pages: Vector[Int]) {
    def middle: Long = pages(pages.size / 2).toLong

    def sorted(ordering: Ordering[Int]): Update =
      Update(pages.sorted(ordering))
  }

  object Update {
    def parse(line: String): Update =
      Update(line.split(',').map(_.toInt).toVector)
  }

  def parse(data: List[String]): (Rules, Updates) = {
    val (ruleLines, updateLines) = data.span(_.nonEmpty);
    val rules = Rules.parse(ruleLines);
    val updates = Updates.parse(updateLines.tail);
    (rules, updates);
  }

  def part1(data: List[String]): Long = {
    val (rules, updates) = parse(data);
    rules.part1(updates);
  }

  def part2(data: List[String]): Long = {
    val (rules, updates) = parse(data);
    rules.part2(updates);
  }

  @main def main5(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day5.txt").asScala.toList;
    val part1 = Day5.part1(data);
    println(s"part1 = $part1");
    val part2 = Day5.part2(data);
    println(s"part2 = $part2");
  }
}