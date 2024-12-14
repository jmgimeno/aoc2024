
package aoc2024j.day14;

import utils.IO;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Day14 {

    private static final int QUADRANTS = 5;
    private static final int NE = 0;
    private static final int SE = 1;
    private static final int SW = 2;
    private static final int NW = 3;
    private static final int CROSS = 4;

    record Space(int width, int height) {

        State parse(List<String> data) {
            var robots = data.stream().map(this::parse).toList();
            return new State(robots, this);
        }

        Robot parse(String line) {
            Pattern pattern = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)");
            var matcher = pattern.matcher(line);
            if (matcher.matches()) {
                var x = Integer.parseInt(matcher.group(1));
                var y = Integer.parseInt(matcher.group(2));
                var dx = Integer.parseInt(matcher.group(3));
                var dy = Integer.parseInt(matcher.group(4));
                return new Robot(x, y, dx, dy);
            } else {
                throw new IllegalArgumentException("Invalid line: " + line);
            }
        }

        public int quadrant(long x, long y) {
            if (y < height / 2 && x < width / 2) return NW;
            if (y < height / 2 && x > width / 2) return NE;
            if (y > height / 2 && x > width / 2) return SE;
            if (y > height / 2 && x < width / 2) return SW;
            return CROSS;
        }
    }

    static final class Robot {
        int x;
        int y;
        final int dx;
        final int dy;

        Robot(int x, int y, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }

        public void run(int time, Space space) {
            x = (x + dx * time) % space.width;
            y = (y + dy * time) % space.height;
            x = x < 0 ? x + space.width : x;
            y = y < 0 ? y + space.height : y;
        }
    }

    static final class State {
        private final List<Robot> robots;
        private final Space space;
        private final int[] quadrants;
        private final int[][] counters;

        State(List<Robot> robots, Space space) {
            this.robots = robots;
            this.space = space;
            this.quadrants = new int[QUADRANTS];
            this.counters = new int[(int) space.height][(int) space.width];
            for (var robot : robots) {
                quadrants[space.quadrant(robot.x, robot.y)]++;
                counters[robot.y][robot.x]++;
            }
        }

        public void run(int time) {
            robots.forEach(robot -> {
                quadrants[space.quadrant(robot.x, robot.y)]--;
                counters[robot.y][robot.x]--;
                robot.run(time, space);
                quadrants[space.quadrant(robot.x, robot.y)]++;
                counters[robot.y][robot.x]++;
            });
        }

        public int runToTreeLike(int max) {
            int t = 0;
            while (t < max && !treeLike()) {
                run(1);
                t++;
            }
            return t;
        }

        public int safety() {
            return quadrants[NW] * quadrants[NE] * quadrants[SE] * quadrants[SW];
        }

        // Ideas that didn't work:
        public boolean nonOverlapping() {
            for (int y = 0; y < space.height; y++) {
                for (int x = 0; x < space.width; x++) {
                    if (counters[y][x] > 1) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean loadedOnTop(double threshold) {
            double top = quadrants[NW] + quadrants[NE];
            double bottom = quadrants[SW] + quadrants[SE];
            return top / (top + bottom) > threshold;
        }

        public boolean verticalSymmetry(double threshold) {
            double left = quadrants[NW] + quadrants[SW];
            double right = quadrants[NE] + quadrants[SE];
            double diff = Math.abs(left - right) / (left + right);
            return diff < threshold;
        }

        // the next two ones worked (hint on reddit)
        public boolean hasBigConnectedComponent(double threshold) {
            int max = 0;
            boolean[][] visited = new boolean[space.height][space.width];
            record Position(int x, int y) {
            }
            for (int x = 0; x < space.width; x++) {
                for (int y = 0; y < space.height; y++) {
                    if (visited[y][x] || counters[y][x] == 0) {
                        continue;
                    }
                    var count = 0;
                    var Stack = new LinkedList<Position>();
                    Stack.add(new Position(x, y));
                    while (!Stack.isEmpty()) {
                        var current = Stack.removeFirst();
                        if (!visited[current.y][current.x]) {
                            visited[current.y][current.x] = true;
                            count++;
                            if (current.x > 0 && counters[current.y][current.x - 1] > 0) {
                                Stack.add(new Position(current.x - 1, current.y));
                            }
                            if (current.x < space.width - 1 && counters[current.y][current.x + 1] > 0) {
                                Stack.add(new Position(current.x + 1, current.y));
                            }
                            if (current.y > 0 && counters[current.y - 1][current.x] > 0) {
                                Stack.add(new Position(current.x, current.y - 1));
                            }
                            if (current.y < space.height - 1 && counters[current.y + 1][current.x] > 0) {
                                Stack.add(new Position(current.x, current.y + 1));
                            }
                        }
                    }
                    if (count > max) {
                        max = count;
                    }

                }
            }

            return max > threshold * robots.size();
        }

        // same idea but easier to implement
        public boolean robotsAreClustered(double threshold) {
            var count = 0;
            for (int y = 1; y < space.height - 1; y++) {
                for (int x = 1; x < space.width - 1; x++) {
                    if (counters[y][x] > 0) {
                        if (counters[y][x - 1] > 0
                                || counters[y][x + 1] > 0
                                || counters[y - 1][x] > 0 || counters[y + 1][x] > 0)
                            count++;
                    }
                }
            }
            return count > threshold * robots.size();
        }

        public boolean treeLike() {
//            return hasBigConnectedComponent(0.4);
            return robotsAreClustered(0.5);
        }

        @Override
        public String toString() {
            boolean[][] grid = new boolean[(int) space.height][(int) space.width];
            robots.forEach(robot -> grid[robot.y][robot.x] = true);
            var sb = new StringBuilder();
            sb.append('+');
            sb.append("-".repeat(space.width));
            sb.append('+');
            sb.append('\n');
            for (int y = 0; y < space.height; y++) {
                sb.append('|');
                for (int x = 0; x < space.width; x++) {
                    sb.append(grid[y][x] ? '*' : ' ');
                }
                sb.append('|');
                sb.append('\n');
            }
            sb.append("+".repeat(Math.max(0, space.width + 2)));
            return sb.toString();
        }
    }

    long part1(List<String> data) {
        var space = new Space(101, 103);
        var state = space.parse(data);
        state.run(100);
        return state.safety();
    }

    long part2(List<String> data) {
        var space = new Space(101, 103);
        var state = space.parse(data);
        var max = 10_000;
        var t = state.runToTreeLike(max);
        System.out.println(state);
        return t;
    }

    public static void main() {
        var day14 = new Day14();
        var data = IO.getResourceAsList("aoc2024/day14.txt");
        var part1 = day14.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day14.part2(data);
        System.out.println("part2 = " + part2);
    }
}