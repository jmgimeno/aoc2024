package utils;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IOTest {

    @Test
    void readResource() {
        var contents = IO.getResourceAsList("aoc/list.txt");
        var expected = List.of("alpha", "beta", "gamma");
        assertEquals(expected, contents);
    }
}