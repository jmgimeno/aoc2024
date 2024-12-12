
package aoc2024j.day12;

import utils.CharGrid;
import utils.IO;

import java.util.*;
import java.util.stream.Collectors;
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
                    if (assigned[y][x]) {
                        continue;
                    }
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
        List<GardenPlot> plots = new ArrayList<>();

        public Region(GardenPlot plot) {
            plots.add(plot);
        }

        public void add(GardenPlot plot) {
            plots.add(plot);
        }

        public long area() {
            return plots.size();
        }

        public long totalPerimeter() {
            return plots.stream().flatMap(GardenPlot::neighbors)
                    .filter(p -> !plots.contains(p))
                    .count();
        }

        public long price1() {
            return area() * totalPerimeter();
        }

        public long price2() {
            return area() * sides();
        }

        long sides() {
            var groups =
                    plots.stream().flatMap(GardenPlot::sides).collect(Collectors.groupingBy(Side::orientation));
            var horizontal = groups.get(Orientation.HORIZONTAL);
            var vertical = groups.get(Orientation.VERTICAL);
            var horizontalPerY = horizontal.stream().collect(Collectors.groupingBy(Side::ref,
                    Collectors.toCollection(ArrayList::new)));
            var verticalPerX = vertical.stream().collect(Collectors.groupingBy(Side::ref,
                    Collectors.toCollection(ArrayList::new)));
            horizontalPerY.values().forEach(l -> l.sort(Comparator.comparingInt(s -> s.min)));
            verticalPerX.values().forEach(l -> l.sort(Comparator.comparingInt(s -> s.min)));
            var horizontalSides =
                    horizontalPerY.values().stream().mapToLong(this::countSides).sum();
            var verticalSides = verticalPerX.values().stream().mapToLong(this::countSides).sum();
            return horizontalSides + verticalSides;
        }

        long countSides(List<Side> sides) {
            // sides corresponds to the same value of ref
            // and sorted incrementally by min
            // it's not empty
            var it = sides.iterator();
            var current = it.next();
            int count = 1;
            while (it.hasNext()) {
                var next = it.next();
                if (current.min == next.min) {
                    // current (nor next) form a side
                    if (it.hasNext()) {
                        // a segment has two sides so the new cannot be equal to the current
                        current = it.next();
                    }
                    continue;
                }
                if (current.max < next.min) {
                    // there is a gap between the segments
                    count++;
                }
                current = next;
            }
            return count;
        }
    }

    record GardenPlot(int x, int y) {

        Stream<GardenPlot> neighbors() {
            return Stream.of(
                    new GardenPlot(x - 1, y),
                    new GardenPlot(x + 1, y),
                    new GardenPlot(x, y - 1),
                    new GardenPlot(x, y + 1)
            );
        }

        Stream<Side> sides() {
            return Stream.of(
                    new Side(y, x, x + 1, Orientation.HORIZONTAL),
                    new Side(y + 1, x, x + 1, Orientation.HORIZONTAL),
                    new Side(x, y, y + 1, Orientation.VERTICAL),
                    new Side(x + 1, y, y + 1, Orientation.VERTICAL)
            );
        }
    }

    enum Orientation {
        HORIZONTAL, VERTICAL
    }

    record Side(int ref, int min, int max, Orientation orientation) {
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