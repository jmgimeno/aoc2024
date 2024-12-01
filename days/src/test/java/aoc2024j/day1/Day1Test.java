
package aoc2024j.day1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.IO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test {

    static final String example =
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;

    static final Day1 day1 = new Day1();

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example);
        assertEquals(11L, day1.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day1.txt");
        assertEquals(1197984L, day1.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() {
        var data = IO.splitLinesAsList(example);
        assertEquals(31L, day1.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day1.txt");
        assertEquals(23387399L, day1.part2(data));
    }
}