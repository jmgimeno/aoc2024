
package aoc2024j.day12;

import utils.IO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

    static final String example1 =
            """
            AAAA
            BBCD
            BBCC
            EEEC
            """;

    static final String example2 =
            """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
            """;

    static final String example3 =
            """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """;

    static final Day12 day12 = new Day12();

    @Test
    @DisplayName("part1 - example1 data")
    void test1_1() {
        var data = IO.splitLinesAsList(example1);
        assertEquals(140L, day12.part1(data));
    }

    @Test
    @DisplayName("part1 - example2 data")
    void test1_2() {
        var data = IO.splitLinesAsList(example2);
        assertEquals(772L, day12.part1(data));
    }

    @Test
    @DisplayName("part1 - example3 data")
    void test1_3() {
        var data = IO.splitLinesAsList(example3);
        assertEquals(1930L, day12.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day12.txt");
        assertEquals(1549354L, day12.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() {
        var data = IO.splitLinesAsList(example1);
        assertEquals(-1L, day12.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day12.txt");
        assertEquals(-1L, day12.part2(data));
    }
}