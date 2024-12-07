
package aoc2024j.day7;

import utils.IO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {

    static final String example =
            """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """;

    static final Day7 day7 = new Day7();

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example);
        assertEquals(3749L, day7.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day7.txt");
        assertEquals(267566105056L, day7.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() {
        var data = IO.splitLinesAsList(example);
        assertEquals(11387L, day7.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day7.txt");
        assertEquals(116094961956019L, day7.part2(data));
    }
}