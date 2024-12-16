
package aoc2024j.day16;

import utils.CharGrid;
import utils.IO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day16 {


    enum Direction {
        North(0, -1), East(1, 0), South(0, 1), West(-1, 0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Position move(Position p) {
            return new Position(p.x + dx, p.y + dy);
        }

        Direction clockwise() {
            return switch (this) {
                case North -> East;
                case East -> South;
                case South -> West;
                case West -> North;
            };
        }

        Direction counterClockwise() {
            return switch (this) {
                case North -> West;
                case East -> North;
                case South -> East;
                case West -> South;
            };
        }
    }

    record Position(int x, int y) {
        Position move(Direction d) {
            return d.move(this);
        }

        int manhattanDistance(Position other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }
    }

    enum Move {
        Forward, Clockwise, CounterClockwise;

        int cost() {
            return switch (this) {
                case Forward -> 1;
                case Clockwise, CounterClockwise -> 1000;
            };
        }
    }

    record Expansion(Reindeer reindeer, Move move) {
    }

    record Reindeer(Position position, Direction direction) {
        Reindeer forward() {
            return new Reindeer(position.move(direction), direction);
        }

        Reindeer counterClockwise() {
            return new Reindeer(position, direction.counterClockwise());
        }

        Reindeer clockwise() {
            return new Reindeer(position, direction.clockwise());
        }

        Stream<Expansion> expand() {
            return Stream.of(
                    new Expansion(forward(), Move.Forward),
                    new Expansion(counterClockwise(), Move.CounterClockwise),
                    new Expansion(clockwise(), Move.Clockwise)
            );
        }
    }

    static class Maze extends CharGrid {
        // Always starts at bottom-left and ends at top-right

        final Position start;
        final Position end;

        Maze(List<String> data) {
            super(data, false);
            start = new Position(1, height - 2);
            end = new Position(width - 2, 1);
        }

        boolean isWall(Position p) {
            return points[p.y][p.x] == '#';
        }

        boolean isValid(Reindeer r) {
            return !isWall(r.position);
        }

        List<Expansion> validExpansions(Reindeer r) {
            return r.expand().filter(e -> isValid(e.reindeer)).toList();
        }
    }

    static class AStar {

        record Node(Reindeer reindeer, int fScore, Node cameFrom) implements Comparable<Node> {
            @Override
            public int compareTo(Node o) {
                return Integer.compare(fScore, o.fScore);
            }
        }

        static class Score {
            final Map<Reindeer, Integer> scores = new HashMap<>();

            int get(Reindeer reindeer) {
                return scores.getOrDefault(reindeer, Integer.MAX_VALUE);
            }

            void set(Reindeer reindeer, int score) {
                scores.put(reindeer, score);
            }
        }

        final Maze maze;
        final PriorityQueue<Node> openQueue;
        final Set<Node> openSet;
        final Function<Reindeer, Integer> heuristic;
        final Score fScore;
        final Score gScore;

        AStar(Maze maze) {
            this.maze = maze;
            openQueue = new PriorityQueue<>();
            openSet = new HashSet<>();
            this.heuristic =
                    r -> {
                        int d = r.position.manhattanDistance(maze.end);
                        // Reindeer is always at the left and down the target so it must turn to face the target
                        if (r.position.x != maze.end.x && r.position.y != maze.end.y) {
                            if (r.direction == Direction.North || r.direction == Direction.East) {
                                d += 1000;
                            } else {
                                d += 2000;
                            }
                        } else if (r.position.x == maze.end.x) { // Vertical
                            if (r.direction == Direction.East || r.direction == Direction.West) {
                                d += 1000;
                            } else if (r.direction == Direction.South) {
                                d += 2000;
                            }
                        } else {
                            if (r.direction == Direction.North || r.direction == Direction.South) {
                                d += 1000;
                            } else if (r.direction == Direction.West) {
                                d += 2000;
                            }
                        }
                        return d;
                    };
            
            fScore = new Score();
            gScore = new Score();
        }

        Optional<Node> solve() {
            var initial = new Reindeer(maze.start, Direction.East);
            gScore.set(initial, 0);
            fScore.set(initial, heuristic.apply(initial));

            var newNode = new Node(initial, fScore.get(initial), null);
            openQueue.add(newNode);
            openSet.add(newNode);

            while (!openQueue.isEmpty()) {
                var current = openQueue.poll();
                openSet.remove(current.reindeer);
                
                if (current.reindeer.position.equals(maze.end)) {
                    System.out.println("gScore: " + gScore.get(current.reindeer));
                    System.out.println("next in queue: " + openQueue.peek());
                    return Optional.of(current);
                }

                for (var expansion : maze.validExpansions(current.reindeer)) {
                    var neighbour = expansion.reindeer;
                    var move = expansion.move;
                    var tentativeGScore = gScore.get(current.reindeer) + move.cost();
                    if (tentativeGScore < gScore.get(neighbour)) {
                        gScore.set(neighbour, tentativeGScore);
                        fScore.set(neighbour, tentativeGScore + heuristic.apply(neighbour));
                        var neighbourNode = new Node(neighbour, fScore.get(neighbour), current);
                        if (!openSet.contains(neighbourNode)) {
                            openQueue.add(neighbourNode);
                            openSet.add(neighbourNode);
                        }
                    }
                }
            }
            return Optional.empty();
        }
    }

    long part1(List<String> data) {
        var maze = new Maze(data);
        var aStar = new AStar(maze);
        var solution = aStar.solve();
        return solution.map(n -> aStar.gScore.get(n.reindeer)).orElseThrow();
    }

    long part2(List<String> data) {
        throw new UnsupportedOperationException("part2");
    }

    public static void main() {
        var day16 = new Day16();
        var data = IO.getResourceAsList("aoc2024/day16.txt");
        var part1 = day16.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day16.part2(data);
        System.out.println("part2 = " + part2);
    }
}