package aoc2024j.day4;

import utils.CharGrid;

import java.util.List;

public class Part1Grid extends CharGrid {

    private final String target;

    public Part1Grid(List<String> data, String target) {
        super(data, false);
        this.target = target;
    }

    long count() {
        long counter = 0L;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                counter += countTarget(x, y);
            }
        }
        return counter;
    }

    long countTarget(int x, int y) {
        if (points[y][x] != target.charAt(0)) {
            return 0L;
        }
        long counter = 0L;
        counter += countUp(x, y);
        counter += countUpRight(x, y);
        counter += countRight(x, y);
        counter += countDownRight(x, y);
        counter += countDown(x, y);
        counter += countDownLeft(x, y);
        counter += countLeft(x, y);
        counter += countUpLeft(x, y);
        return counter;
    }

    long countUp(int x, int y) {
        if (y - target.length() + 1 >= 0) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y - i][x] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }

    long countUpRight(int x, int y) {
        if (y - target.length() + 1 >= 0 && x + target.length() - 1 < width) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y - i][x + i] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }

    long countRight(int x, int y) {
        if (x + target.length() - 1 < width) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y][x + i] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }

    long countDownRight(int x, int y) {
        if (y + target.length() - 1 < height && x + target.length() - 1 < width) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y + i][x + i] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }

    long countDown(int x, int y) {
        if (y + target.length() - 1 < height) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y + i][x] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }

    long countDownLeft(int x, int y) {
        if (y + target.length() - 1 < height && x - target.length() + 1 >= 0) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y + i][x - i] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }

    long countUpLeft(int x, int y) {
        if (y - target.length() + 1 >= 0 && x - target.length() + 1 >= 0) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y - i][x - i] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }

    long countLeft(int x, int y) {
        if (x - target.length() + 1 >= 0) {
            for (int i = 1; i < target.length(); i++) {
                if (points[y][x - i] != target.charAt(i)) {
                    return 0L;
                }
            }
            return 1L;
        } else {
            return 0L;
        }
    }
}
