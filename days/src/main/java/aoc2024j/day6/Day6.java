
package aoc2024j.day6;

import utils.IO;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;

public class Day6 {

    enum Direction implements UnaryOperator<Position> {

        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public Position apply(Position position) {
            return new Position(position.x + dx, position.y + dy);
        }

        Direction turnRight() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }
    }

    static class Plan {
        final List<String> plan;
        final int width, height;

        Plan(List<String> plan) {
            this.plan = plan;
            this.width = plan.getFirst().length();
            this.height = plan.size();
        }

        char get(Position position) {
            return plan.get(position.y).charAt(position.x);
        }

        boolean isInside(Position position) {
            return position.x >= 0 && position.x < width && position.y >= 0 && position.y < height;
        }
    }

    record Position(int x, int y) {
    }

    record Guard(Position at, Direction facing) {
        Position inFrontOf() {
            return facing.apply(at);
        }

        Guard step() {
            return new Guard(inFrontOf(), facing);
        }

        Guard turnRight() {
            return new Guard(at, facing.turnRight());
        }
    }

    record ParsedInput(Plan plan, Guard guard) {
    }

    ParsedInput parse(List<String> lines) {
        var plan = new Plan(lines);
        var guard = new Guard(findGuard(lines), Direction.UP);
        return new ParsedInput(plan, guard);
    }

    private static Position findGuard(List<String> lines) {
        var y = (int) lines.stream().takeWhile(line -> !line.contains("^")).count();
        var x = lines.get(y).indexOf("^");
        return new Position(x, y);
    }

    private static Set<Position> initialWalk(Plan plan, Guard guard) {
        var current = guard;
        var steps = new HashSet<Position>();
        while (true) {
            steps.add(current.at);
            if (!plan.isInside(current.inFrontOf())) {
                return steps;
            } else if (plan.get(current.inFrontOf()) == '#') {
                current = current.turnRight();
            } else {
                current = current.step();
            }
        }
    }

    long part1(List<String> data) {
        var parsed = parse(data);
        var steps = initialWalk(parsed.plan, parsed.guard);
        return steps.size();
    }

    private boolean hasLoop(Plan plan, Guard guard, Position obstacle) {
        var current = guard;
        var obstacles = new HashSet<Guard>();
        while (true) {
            if (obstacles.contains(current)) {
                return true;
            } else if (!plan.isInside(current.inFrontOf())) {
                return false;
            } else if (plan.get(current.inFrontOf()) == '#' || current.inFrontOf().equals(obstacle)) {
                obstacles.add(current);
                current = current.turnRight();
            } else {
                current = current.step();
            }
        }
    }

    private long sequentialCount(Plan plan, Guard guard, List<Position> obstacles) {
        return obstacles.stream()
                .filter(o -> hasLoop(plan, guard, o))
                .count();
    }

    private List<List<Position>> partition(List<Position> obstacles, int chunkSize) {
        var chunks = new ArrayList<List<Position>>();
        for (int i = 0; i < obstacles.size(); i += chunkSize) {
            var end = Math.min(i + chunkSize, obstacles.size());
            chunks.add(obstacles.subList(i, end));
        }
        return chunks;
    }

    private long parallelCount(Plan plan, Guard guard, List<Position> obstacles) {
        var numThreads = 6; // manually adjusted
        var chunkSize = obstacles.size() / numThreads;
        var chunks = partition(obstacles, chunkSize);
        var latch = new java.util.concurrent.CountDownLatch(chunks.size());
        var result = new AtomicLong(0L);
        for (var chunk : chunks) {
            new Thread(() -> {
                var count = sequentialCount(plan, guard, chunk);
                result.addAndGet(count);
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result.get();
    }

    long part2(List<String> data) {
        var parsed = parse(data);
        var steps = initialWalk(parsed.plan, parsed.guard);
        return parallelCount(parsed.plan, parsed.guard, new ArrayList<>(steps));
    }

    public static void main() {
        var day6 = new Day6();
        var data = IO.getResourceAsList("aoc2024/day6.txt");
        var part1 = day6.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day6.part2(data);
        System.out.println("part2 = " + part2);
    }
}