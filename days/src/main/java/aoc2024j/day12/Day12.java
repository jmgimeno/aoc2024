
package aoc2024j.day12;

import utils.CharGrid;
import utils.IO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class Day12 {

    static class PlantMap extends CharGrid {

        public PlantMap(List<String> data) {
            super(data, false);
        }

        Regions findRegions() {
            var regions = new Regions();
            var assigned = new boolean[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (assigned[y][x]) continue;
                    var plant = points[y][x];
                    Region region = expandRegion(x, y, assigned);
                    regions.addRegion(plant, region);
                }
            }
            return regions;
        }

        private Region expandRegion(int x, int y, boolean[][] assigned) {
            var region = new Region(new GardenPlot(x, y));
            var queue = new LinkedList<GardenPlot>();
            queue.add(new GardenPlot(x, y));
            assigned[y][x] = true;
            while (!queue.isEmpty()) {
                var plot = queue.poll();
                for (var neighbor : neighbors(plot)) {
                    if (!assigned[neighbor.y][neighbor.x] && points[neighbor.y][neighbor.x] == points[y][x]) {
                        assigned[neighbor.y][neighbor.x] = true;
                        region.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            return region;
        }

        private List<GardenPlot> neighbors(GardenPlot plot) {
            return Stream.of(
                    new GardenPlot(plot.x - 1, plot.y),
                    new GardenPlot(plot.x + 1, plot.y),
                    new GardenPlot(plot.x, plot.y - 1),
                    new GardenPlot(plot.x, plot.y + 1)
            ).filter(p -> p.x >= 0 && p.x < width && p.y >= 0 && p.y < height).toList();
        }

    }

    static class Regions {
        private final Map<Character, List<Region>> regions = new HashMap<>();

        public void addRegion(char plant, Region region) {
            regions.computeIfAbsent(plant, _ -> new ArrayList<>()).add(region);
        }

        public long price1() {
            return regions.values().stream()
                    .flatMap(List::stream)
                    .mapToLong(Region::price1)
                    .sum();
        }

        public long price2() {
            return regions.values().stream()
                    .flatMap(List::stream)
                    .mapToLong(Region::price2)
                    .sum();
        }
    }

    static class Region {
        final List<GardenPlot> plots = new ArrayList<>();
        Map<Orientation, Map<Integer, List<Wall>>> cachedWalls = null;

        private Map<Orientation, Map<Integer, List<Wall>>> walls() {
            if (cachedWalls == null) {
                cachedWalls = GardenPlot.generateWalls(plots);
            }
            return cachedWalls;
        }

        public Region(GardenPlot plot) {
            plots.add(plot);
        }

        public void add(GardenPlot plot) {
            plots.add(plot);
            cachedWalls = null;
        }

        public long price1() {
            return area() * perimeter();
        }

        public long price2() {
            return area() * sides();
        }

        private long area() {
            return plots.size();
        }

        private long perimeter() {
            return walls().values().stream()
                    .flatMap(m -> m.values().stream())
                    .mapToLong(List::size)
                    .sum();
        }

        private long sides() {
            return walls().values().stream()
                    .flatMap(m -> m.values().stream())
                    .mapToLong(Wall::countWalls)
                    .sum();
        }

    }

    record GardenPlot(int x, int y) {

        private static Map<Orientation, Map<Integer, List<Wall>>> generateWalls(List<GardenPlot> plots) {
            return plots.stream()
                    .flatMap(GardenPlot::walls)
                    .collect(Collectors.groupingBy(
                            s -> s.direction().orientation(),
                            Collectors
                                    .groupingBy(
                                            Wall::ref,
                                            Collectors.collectingAndThen(
                                                    Collectors.toList(),
                                                    walls -> {
                                                        walls.sort(Comparator.comparingInt(Wall::coordinate));
                                                        return Wall.filterWalls(walls);
                                                    }
                                            ))));
        }

        private Stream<Wall> walls() {
            return Stream.of(
                    new Wall(y, x, Direction.UP),
                    new Wall(y + 1, x, Direction.DOWN),
                    new Wall(x, y, Direction.LEFT),
                    new Wall(x + 1, y, Direction.RIGHT)
            );
        }
    }

    enum Orientation {
        HORIZONTAL, VERTICAL
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        Orientation orientation() {
            return this == UP || this == DOWN ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        }

        static Stream<Direction> all() {
            return Stream.of(UP, DOWN, LEFT, RIGHT);
        }
    }

    record Wall(int ref, int coordinate, Direction direction) {

        static long countWalls(List<Wall> walls) {
            return
                    walls.stream().collect(
                            Collectors.collectingAndThen(
                                    Collectors.groupingBy(Wall::direction,
                                            Collectors.mapping(Wall::coordinate,
                                                    Collectors.toList())),
                                    coordinatesByDirection -> Direction.all()
                                            .mapToLong(d -> countGaps(coordinatesByDirection.getOrDefault(d, List.of())))
                                            .sum()));
        }

        private static long countGaps(List<Integer> coordinates) {
            if (coordinates.isEmpty()) return 0;
            long counter = 1;
            var it = coordinates.iterator();
            var current = it.next();
            while (it.hasNext()) {
                var next = it.next();
                if (next - current > 1) {
                    counter++;
                }
                current = next;
            }
            return counter;
        }

        private static List<Wall> filterWalls(List<Wall> candidateWalls) {
            if (candidateWalls.size() < 2) {
                return candidateWalls;
            }
            var purged = candidateWalls.stream()
                    .gather(Gatherers.windowSliding(2))
                    .filter(l -> l.get(0).coordinate() == l.get(1).coordinate())
                    .map(l -> l.get(0).coordinate())
                    .collect(Collectors.toSet());
            return candidateWalls.stream()
                    .filter(wall -> !purged.contains(wall.coordinate()))
                    .collect(Collectors.toList());
        }
    }

    long part1(List<String> data) {
        var map = new PlantMap(data);
        var regions = map.findRegions();
        return regions.price1();
    }

    long part2(List<String> data) {
        var map = new PlantMap(data);
        var regions = map.findRegions();
        return regions.price2();
    }

    public static void main() {
        var day12 = new Day12();
        var data = IO.getResourceAsList("aoc2024/day12.txt");
        var part1 = day12.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day12.part2(data);
        System.out.println("part2 = " + part2);
    }
}