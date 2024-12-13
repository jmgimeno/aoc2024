
package aoc2024j.day12;

import utils.CharGrid;
import utils.IO;

import java.util.*;
import java.util.function.Predicate;
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

        private boolean isInside(int x, int y) {
            return 0 <= x && x < width && 0 <= y && y < height;
        }

        private int get(int x, int y) {
            return isInside(x, y) ? points[y][x] : -1;
        }

        private boolean isCornerNW(GardenPlot p) {
            var v = get(p.x, p.y);
            var nw = get(p.x - 1, p.y - 1);
            var n = get(p.x, p.y - 1);
            var w = get(p.x - 1, p.y);
            return nw != v && n == v && w == v || nw != v && n != v && w != v || nw == v && n != v && w != v;
        }

        private boolean isCornerNE(GardenPlot p) {
            var v = get(p.x, p.y);
            var ne = get(p.x + 1, p.y - 1);
            var n = get(p.x, p.y - 1);
            var e = get(p.x + 1, p.y);
            return ne != v && n == v && e == v ||  (ne != v && n != v && e != v) || (ne == v && n != v && e != v);
        }

        private boolean isCornerSW(GardenPlot p) {
            var v = get(p.x, p.y);
            var sw = get(p.x - 1, p.y + 1);
            var s = get(p.x, p.y + 1);
            var w = get(p.x - 1, p.y);
            return sw != v && s == v && w == v || (sw != v && s != v && w != v) || (sw == v && s != v && w != v);
        }

        private boolean isCornerSE(GardenPlot p) {
            var v = get(p.x, p.y);
            var se = get(p.x + 1, p.y + 1);
            var s = get(p.x, p.y + 1);
            var e = get(p.x + 1, p.y);
            return se != v && s == v && e == v || (se != v && s != v && e != v) || (se == v && s != v && e != v);
        }
    }

    static class Regions {
        private final Map<Character, List<Region>> regions = new HashMap<>();
        private final PlantMap plantMap;

        public Regions(PlantMap plantMap) {
            this.plantMap = plantMap;
        }

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
                    .mapToLong(r -> r.price2(plantMap))
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

        public long price2(PlantMap plantMap) {
            return area() * sides(plantMap);
        }

        public long sides(PlantMap plantMap) {
            Stream<Predicate<GardenPlot>> corners =
                    Stream.of(plantMap::isCornerNW, plantMap::isCornerNE, plantMap::isCornerSW, plantMap::isCornerSE);
            return corners.map(p -> plots.stream().filter(p).count()).reduce(0L, Long::sum);
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
