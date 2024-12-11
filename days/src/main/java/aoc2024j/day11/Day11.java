
package aoc2024j.day11;

import utils.IO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11 {

    record Stones(Map<Long, Long> stones) {
        Stones(String data) {
            this(Arrays
                    .stream(data.split(" "))
                    .collect(Collectors.groupingBy(Long::parseLong, Collectors.counting()))
            );
        }

        long numStones() {
            return stones.values().stream().mapToLong(Long::longValue).sum();
        }

        Stones blink(int steps) {
            var result = this;
            for (int i = 0; i < steps; i++) {
                result = result.blink();
            }
            return result;
        }

        private Stones blink() {
            var newStones =
                    stones.entrySet().stream()
                            .flatMap(entry -> {
                                var value = entry.getKey();
                                var count = entry.getValue();
                                return blinkValue(value).map(v -> Map.entry(v, count));
                            })
                            .collect(Collectors.groupingBy(Map.Entry::getKey,
                                    Collectors.summingLong(Map.Entry::getValue)));

            return new Stones(newStones);
        }

        private Stream<Long> blinkValue(long value) {
            if (value == 0) {
                return Stream.of(1L);
            } else {
                int digits = numberOfDigits(value);
                if (digits % 2 == 0) {
                    long div = pow10(digits / 2);
                    long high = value / div;
                    long low = value % div;
                    return Stream.of(high, low);
                } else {
                    return Stream.of(value * 2024);
                }
            }
        }

        private int numberOfDigits(long value) {
            int result = 1;
            while (value >= 10) {
                value /= 10;
                result++;
            }
            return result;
        }

        private long pow10(int exp) {
            long result = 1;
            for (int i = 0; i < exp; i++) {
                result *= 10;
            }
            return result;
        }
    }

    long part1(List<String> data) {
        var stones = new Stones(data.getFirst());
        var result = stones.blink(25);
        return result.numStones();
    }

    long part2(List<String> data) {
        var stones = new Stones(data.getFirst());
        var result = stones.blink(75);
        return result.numStones();
    }

    public static void main() {
        var day11 = new Day11();
        var data = IO.getResourceAsList("aoc2024/day11.txt");
        var part1 = day11.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day11.part2(data);
        System.out.println("part2 = " + part2);
    }
}