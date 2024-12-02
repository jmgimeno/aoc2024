
package aoc2024j.day2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.IO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2Test {

    static final String example =
            """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;

    static final Day2 day2 = new Day2();

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example);
        assertEquals(2L, day2.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day2.txt");
        assertEquals(402L, day2.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() {
        var data = IO.splitLinesAsList(example);
        assertEquals(4L, day2.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day2.txt");
        assertEquals(455L, day2.part2(data));
    }
}