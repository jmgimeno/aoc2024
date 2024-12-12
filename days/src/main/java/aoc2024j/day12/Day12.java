
package aoc2024j.day12;

import utils.CharGrid;
import utils.IO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 {

    static class PlantMap extends CharGrid {

        public PlantMap(List<String> data) {
            super(data, false);
        }

        Regions findRegions() {
            var regions = new Regions(this);
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
        private final PlantMap map;

        Regions(PlantMap map) {
            this.map = map;
        }

        public void addRegion(char plant, Region region) {
            regions.computeIfAbsent(plant, k -> new ArrayList<>()).add(region);
        }

        public int price() {
            return regions.values().stream()
                    .flatMap(List::stream)
                    .mapToInt(this::regionPrice)
                    .sum();
        }

        public int regionPrice(Region region) {
            return region.price();
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

        public int area() {
            return plots.size();
        }

        public int innerPerimeter() {
            return (int) plots.stream().flatMap(GardenPlot::neighbors)
                    .filter(p -> !plots.contains(p))
                    .count();
        }

        public int price() {
            return area() * innerPerimeter();
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
    }

    long part1(List<String> data) {
        var map = new PlantMap(data);
        var regions = map.findRegions();
        return regions.price();
    }

    long part2(List<String> data) {
        throw new UnsupportedOperationException("part2");
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