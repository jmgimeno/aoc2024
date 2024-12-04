
package aoc2024j.day4;

import utils.IO;

import java.util.List;

public class Day4 {

    long part1(List<String> data) {
        var grid = new Part1Grid(data, "XMAS");
        return grid.count();
    }

    long part2(List<String> data) {
        var grid = new Part2Grid(data, "MAS");
        return grid.count();
    }

    public static void main(String[] args) {
        var day4 = new Day4();
        var data = IO.getResourceAsList("aoc2024/day4.txt");
        var part1 = day4.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day4.part2(data);
        System.out.println("part2 = " + part2);
    }
}