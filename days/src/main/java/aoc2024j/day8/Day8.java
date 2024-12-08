
package aoc2024j.day8;

import utils.IO;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 {

    record Position(int x, int y) {
        Position add(Position other) {
            return new Position(x + other.x, y + other.y);
        }

        Position sub(Position other) {
            return new Position(x - other.x, y - other.y);
        }

        Position mul(int factor) {
            return new Position(x * factor, y * factor);
        }

        static BiFunction<Position, Position, List<Position>> antinodes(int factor) {
            return (p1, p2) -> {
                var delta = p1.sub(p2);
                var anti1 = p1.add(delta.mul(factor));
                var anti2 = p2.sub(delta.mul(factor));
                return List.of(anti1, anti2);
            };
        }
    }

    record Antennas(Map<Character, List<Position>> antennas, int height, int width) {
        boolean isInside(Position p) {
            return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
        }

        Set<Position> antinodes(int hops) {
            return antennas.values().stream()
                    .flatMap(positions ->
                            positions.stream()
                                    .flatMap(p1 ->
                                            positions.stream()
                                                    .flatMap(p2 ->
                                                            (!p1.equals(p2))
                                                                    ? Position.antinodes(hops).apply(p1, p2).stream().filter(this::isInside)
                                                                    : Stream.empty()
                                                    )
                                    )
                    )
                    .collect(Collectors.toSet());
        }

        static Antennas parse(List<String> lines) {
            var antennas = new HashMap<Character, List<Position>>();
            int height = lines.size();
            int width = lines.getFirst().length();
            for (int y = 0; y < height; y++) {
                var line = lines.get(y);
                for (int x = 0; x < width; x++) {
                    var c = line.charAt(x);
                    if (c != '.') {
                        antennas.computeIfAbsent(c, _ -> new ArrayList<>()).add(new Position(x, y));
                    }
                }
            }
            return new Antennas(antennas, height, width);
        }

        Set<Position> antinodesForPart2() {
            return IntStream.iterate(0, i -> i + 1)
                    .mapToObj(this::antinodes)
                    .takeWhile(set -> !set.isEmpty())
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }
    }

    long part1(List<String> data) {
        var antennas = Antennas.parse(data);
        return antennas.antinodes(1).size();
    }

    long part2(List<String> data) {
        var antennas = Antennas.parse(data);
        return antennas.antinodesForPart2().size();
    }

    public static void main() {
        var day8 = new Day8();
        var data = IO.getResourceAsList("aoc2024/day8.txt");
        var part1 = day8.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day8.part2(data);
        System.out.println("part2 = " + part2);
    }
}
