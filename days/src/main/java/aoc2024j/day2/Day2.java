
package aoc2024j.day2;

import utils.IO;

import java.util.AbstractList;
import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day2 {

    record Interval(int min, int max) {
        boolean contains(int value) {
            return min <= value && value <= max;
        }
    }

    // step is defined as x1 - x0
    static Interval INCREASING = new Interval(1, 3);
    static Interval DECREASING = new Interval(-3, -1);

    record Report(List<Integer> levels) {
        static Report parse(String line) {
            var levels = Stream.of(line.split(" "))
                    .map(Integer::parseInt)
                    .toList();
            return new Report(levels);
        }

        boolean isValid() {
            return containsAllDifferences(levels, INCREASING)
                    || containsAllDifferences(levels, DECREASING);
        }

        private boolean containsAllDifferences(List<Integer> levels, Interval interval) {
            return levels.stream()
                    .gather(Gatherers.windowSliding(2))
                    .allMatch(pair -> interval.contains(pair.get(1) - pair.get(0)));
        }

        boolean isValidWithHoles() {
            return isValid() || IntStream.range(0, levels.size())
                    .mapToObj(i -> new Report(new HoledList<>(levels, i)))
                    .anyMatch(Report::isValid);
        }
    }

    long part1(List<String> data) {
        return data.stream()
                .map(Report::parse)
                .filter(Report::isValid)
                .count();
    }

    static class HoledList<E> extends AbstractList<E> {
        private final List<E> list;
        private final int hole;

        HoledList(List<E> list, int hole) {
            assert 0 <= hole && hole < list.size();
            this.list = list;
            this.hole = hole;
        }

        @Override
        public E get(int index) {
            return list.get(index < hole ? index : index + 1);
        }

        @Override
        public int size() {
            return list.size() - 1;
        }
    }

    long part2(List<String> data) {
        return data.stream()
                .map(Report::parse)
                .filter(Report::isValidWithHoles)
                .count();
    }

    public static void main(String[] args) {
        var day2 = new Day2();
        var data = IO.getResourceAsList("aoc2024/day2.txt");
        var part1 = day2.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day2.part2(data);
        System.out.println("part2 = " + part2);
    }
}