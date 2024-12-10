
package aoc2024j.day10;

import utils.IO;
import utils.IntGrid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 {

    enum Direction implements UnaryOperator<Position> {

        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public Position apply(Position position) {
            return new Position(position.x() + dx, position.y() + dy);
        }

        static Stream<Position> expand(Position position) {
            return Stream.of(Direction.values())
                    .map(d -> d.apply(position));
        }
    }

    record Position(int x, int y) {
    }

    record Path(Position head, Path tail) {

        static Path of(Position position) {
            return new Path(position, null);
        }

        Path add(Position position) {
            return new Path(position, this);
        }
    }

    static class TopographicalMap extends IntGrid implements Predicate<Position> {
        public TopographicalMap(List<String> data) {
            super(data);
        }

        int get(Position p) {
            return points[p.y()][p.x()];
        }

        @Override
        public boolean test(Position position) {
            return position.y() >= 0 && position.y() < height &&
                    position.x() >= 0 && position.x() < width;
        }

        Stream<Position> trailHeads() {
            return IntStream.range(0, height)
                    .boxed()
                    .flatMap(y ->
                            IntStream.range(0, width)
                                    .mapToObj(x -> new Position(x, y)))
                    .filter(p -> get(p) == 0);
        }

        Stream<Position> expand(Position position) {
            return Direction.expand(position)
                    .filter(this);
        }

        Set<Path> trailsFrom(Position trailHead) {
            var trails = new HashSet<Path>();
            trails(Path.of(trailHead), trails);
            return trails;
        }

        void trails(Path path, Set<Path> found) {
            var position = path.head();
            int value = get(position);
            if (value == 9) {
                found.add(path);
            } else {
                expand(position)
                        .filter(p -> get(p) == value + 1)
                        .forEach(p -> trails(path.add(p), found));
            }
        }

        long part1() {
            return trailHeads()
                    .flatMap(p -> trailsFrom(p).stream().map(Path::head).distinct())
                    .count();
        }

        long part2() {
            return trailHeads()
                    .map(this::trailsFrom)
                    .mapToLong(Set::size)
                    .sum();
        }
    }

    long part1(List<String> data) {
        return new TopographicalMap(data).part1(); //totalScore1();
    }

    long part2(List<String> data) {
        return new TopographicalMap(data).part2(); //totalScore2();
    }

    public static void main() {
        var day10 = new Day10();
        var data = IO.getResourceAsList("aoc2024/day10.txt");
        var part1 = day10.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day10.part2(data);
        System.out.println("part2 = " + part2);
    }
}
