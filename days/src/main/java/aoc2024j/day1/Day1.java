package aoc2024j.day1;

import utils.IO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day1 {

    record Unzip(List<Integer> first, List<Integer> second) {
    }

    Unzip unzip(List<String> data) {
        var first = new ArrayList<Integer>(data.size());
        var second = new ArrayList<Integer>(data.size());
        for (var line : data) {
            var parts = line.split("\\s+");
            first.add(Integer.parseInt(parts[0]));
            second.add(Integer.parseInt(parts[1]));
        }
        return new Unzip(first, second);
    }

    long distance(List<Integer> first, List<Integer> second) {
        long sum = 0;
        for (int i = 0; i < first.size(); i++) {
            sum += Math.abs(first.get(i) - second.get(i));
        }
        return sum;
    }

    long part1(List<String> data) {
        var unzip = unzip(data);
        Collections.sort(unzip.first);
        Collections.sort(unzip.second);
        return distance(unzip.first, unzip.second);
    }

    long part2(List<String> data) {
        var unzip = unzip(data);
        var frequencies = frequencies(unzip.second);
        return similarity(unzip.first, frequencies);
    }

    Map<Integer, Long> frequencies(List<Integer> second) {
        return second.stream().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
    }

    private long similarity(List<Integer> first, Map<Integer, Long> frequencies) {
        return first.stream().mapToLong(f -> f * frequencies.getOrDefault(f, 0L)).sum();
    }

    public static void main(String[] args) {
        var day1 = new Day1();
        var data = IO.getResourceAsList("aoc2024/day1.txt");
        var part1 = day1.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day1.part2(data);
        System.out.println("part2 = " + part2);
    }
}