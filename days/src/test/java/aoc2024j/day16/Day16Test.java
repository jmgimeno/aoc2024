
package aoc2024j.day16;

import utils.IO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {

    static final String example1 =
            """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
            """;

    static final String example2 =
            """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
            """;
    
    static final Day16 day16 = new Day16();

    @Test
    @DisplayName("part1 - example1 data")
    void test1_1() {
        var data = IO.splitLinesAsList(example1);
        assertEquals(7036L, day16.part1(data));
    }

    @Test
    @DisplayName("part1 - example2 data")
    void test1_2() {
        var data = IO.splitLinesAsList(example2);
        assertEquals(11048L, day16.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day16.txt");
        assertEquals(78428, day16.part1(data));
    }

    @Test
    @DisplayName("part2 - example1 data")
    void test3_1() {
        var data = IO.splitLinesAsList(example1);
        assertEquals(45L, day16.part2(data));
    }


    @Test
    @DisplayName("part2 - example2 data")
    void test3_2() {
        var data = IO.splitLinesAsList(example2);
        assertEquals(64L, day16.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day16.txt");
        assertEquals(463L, day16.part2(data));
    }
}