
package aoc2024j.day9;

import utils.IO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test {

    static final String exampleA =
            """
            12345
            """;

    static final String exampleB =
            """
            2333133121414131402
            """;

    static final Day9 day9 = new Day9();

    @Test
    @DisplayName("part1 - example1A data")
    void test1A() {
        var data = IO.splitLinesAsList(exampleA);
        assertEquals(60L, day9.part1(data));
    }

    @Test
    @DisplayName("part1 - example1B data")
    void test1B() {
        var data = IO.splitLinesAsList(exampleB);
        assertEquals(1928L, day9.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day9.txt");
        assertEquals(6430446922192L, day9.part1(data));
    }

    @Test
    @DisplayName("part2 - exampleB data")
    void test3() {
        var data = IO.splitLinesAsList(exampleB);
        assertEquals(2858L, day9.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day9.txt");
        assertEquals(6460170593016L, day9.part2(data));
    }
}
