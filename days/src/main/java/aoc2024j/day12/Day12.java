
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
                    plots.stream()
                            .flatMap(GardenPlot::sides)
                            .collect(Collectors.groupingBy(
                                    Side::orientation,
                                    Collectors
                                            .groupingBy(
                                                    Side::ref,
                                                    Collectors.mapping(Side::coord,
                                                            Collectors.toCollection(ArrayList::new)
                                                    ))));
            // groups contains the sides grouped by orientation and ref
            // sort each list of sides by coord
            groups.values().forEach(m ->
                    m.values().forEach(l ->
                            l.sort(Comparator.naturalOrder())));

            // count the number of sides with a gap of 1
            return groups.values().stream()
                    .flatMap(m -> m.values().stream())
                    .mapToLong(this::countSides)
                    .sum();
        }

        long countSides(List<Integer> sides) {
            var purgedSides = new ArrayList<>(sides);
            for (int i = sides.size() - 2; i >= 0; i--) {
                if (Objects.equals(sides.get(i), sides.get(i + 1))) {
                    purgedSides.remove(i + 1);
                    purgedSides.remove(i);
                    i--;
                }
            }
            long counter = purgedSides.isEmpty() ? 0 : 1;
            for (int i = 0; i < purgedSides.size() - 1; i++) {
                if (purgedSides.get(i + 1) - purgedSides.get(i) > 1) {
                    counter++;
                }
            }
            return counter;
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
                    new Side(y, x, Orientation.HORIZONTAL),
                    new Side(y + 1, x, Orientation.HORIZONTAL),
                    new Side(x, y, Orientation.VERTICAL),
                    new Side(x + 1, y, Orientation.VERTICAL)
            );
        }
    }

    enum Orientation {
        HORIZONTAL, VERTICAL
    }

    record Side(int ref, int coord, Orientation orientation) {
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
