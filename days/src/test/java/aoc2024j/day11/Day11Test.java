
package aoc2024j.day11;

import utils.IO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    static final String example = "125 17";

    static final Day11 day11 = new Day11();

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example);
        assertEquals(55312L, day11.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day11.txt");
        assertEquals(183620L, day11.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() { // not provided
        var data = IO.splitLinesAsList(example);
        assertEquals(65601038650482L, day11.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day11.txt");
        assertEquals(220377651399268L, day11.part2(data));
    }
}