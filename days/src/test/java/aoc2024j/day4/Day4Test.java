
package aoc2024j.day4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.IO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test {

    static final String example =
            """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """;

    static final Day4 day4 = new Day4();

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example);
        assertEquals(18L, day4.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day4.txt");
        assertEquals(2567L, day4.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() {
        var data = IO.splitLinesAsList(example);
        assertEquals(9L, day4.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day4.txt");
        assertEquals(2029L, day4.part2(data));
    }
}