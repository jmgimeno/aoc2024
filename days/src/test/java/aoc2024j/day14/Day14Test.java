
package aoc2024j.day14;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.IO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {

    static final String example =
            """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """;

    static final Day14 day14 = new Day14();

    static int runPart1(List<String> data) {
        var space = new Day14.Space(11, 7);
        var state = space.parse(data);
        state.run(100);
        return state.safety();
    }

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example);
        assertEquals(12, runPart1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day14.txt");
        assertEquals(216772608, day14.part1(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day14.txt");
        assertEquals(6888, day14.part2(data));
    }
}