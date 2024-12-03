
package aoc2024j.day3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.IO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {

    static final String example1 =
            """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
            """;

    static final String example2 =
            """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
            """;

    static final Day3 day3 = new Day3();

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example1);
        assertEquals(161L, day3.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day3.txt");
        assertEquals(170807108L, day3.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() {
        var data = IO.splitLinesAsList(example2);
        assertEquals(48L, day3.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day3.txt");
        assertEquals(74838033L, day3.part2(data));
    }
}