package aoc2024j.day4;

import utils.CharGrid;

import java.util.List;

public class Part2Grid extends CharGrid {
    private final String target;

    public Part2Grid(List<String> data, String target) {
        assert target.length() == 3;
        super(data, false);
        this.target = target;
    }

    public long count() {
        long counter = 0L;
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                counter += countTarget(x, y);
            }
        }
        return counter;
    }

    private long countTarget(int x, int y) {
        if (points[y][x] != target.charAt(1)) {
            return 0L;
        }
        if (diagonal(x, y) && antiDiagonal(x, y)) {
            return 1L;
        }
        return 0L;
    }

    private boolean diagonal(int x, int y) {
        return (points[y - 1][x - 1] == target.charAt(0) && points[y + 1][x + 1] == target.charAt(2))
                || (points[y - 1][x - 1] == target.charAt(2) && points[y + 1][x + 1] == target.charAt(0));
    }

    private boolean antiDiagonal(int x, int y) {
        return (points[y - 1][x + 1] == target.charAt(0) && points[y + 1][x - 1] == target.charAt(2))
                || (points[y - 1][x + 1] == target.charAt(2) && points[y + 1][x - 1] == target.charAt(0));
    }
}
