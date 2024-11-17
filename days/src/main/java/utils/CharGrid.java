package utils;

import java.util.List;

public class CharGrid {
    protected int height;
    protected final int width;
    protected final char[][] points;

    public CharGrid(List<String> data) {
        this(data, true);
    }

    public CharGrid(List<String> data, char embedding) {
        this(data, true, embedding);
    }

    public CharGrid(List<String> data, boolean embed) {
        this(data, embed, '.');
    }

    public CharGrid(List<String> data, boolean embed, char embedding) {
        height = data.size();
        width = data.getFirst().length();
        if (embed) {
            points = new char[height + 2][width + 2];
            fillPointsEmbed(data, embedding);
        } else {
            points = new char[height][width];
            fillPointsNotEmbed(data);
        }
    }

    private void fillPointsEmbed(List<String> data, char embedding) {
        for (int y = 0; y < height + 2; y++) {
            for (int x = 0; x < width + 2; x++) {
                if (x == 0 || y == 0 || x == width + 1 || y == height + 1) {
                    points[y][x] = embedding;
                } else {
                    points[y][x] = data.get(y - 1).charAt(x - 1);
                }
            }
        }
    }

    private void fillPointsNotEmbed(List<String> data) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                points[y][x] = data.get(y).charAt(x);
            }
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (char[] row : points) {
            for (char c : row) {
                sb.append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
