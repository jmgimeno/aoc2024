
package aoc2024j.day13;

import utils.IO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Day13Test {

    static final String example =
            """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400
            
            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176
            
            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450
            
            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279""";

    static final Day13 day13 = new Day13();

    @Test
    @DisplayName("part1 - example data")
    void test1() {
        var data = IO.splitLinesAsList(example);
        assertEquals(480L, day13.part1(data));
    }

    @Test
    @DisplayName("part1 - input data")
    void test2() {
        var data = IO.getResourceAsList("aoc2024/day13.txt");
        assertEquals(27105L, day13.part1(data));
    }

    @Test
    @DisplayName("part2 - example data")
    void test3() {
        var data = IO.splitLinesAsList(example);
        assertEquals(new BigInteger("875318608908"), day13.part2(data));
    }

    @Test
    @DisplayName("part2 - input data")
    void test4() {
        var data = IO.getResourceAsList("aoc2024/day13.txt");
        assertEquals(new BigInteger("101726882250942"), day13.part2(data));
    }
}