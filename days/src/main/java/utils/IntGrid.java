package utils;

import java.util.List;

public class IntGrid {
    protected final int[][] points;
    protected int height;
    protected final int width;

    public IntGrid(List<String> data) {
        height = data.size();
        width = data.getFirst().length();
        points = new int[height][width];
        fillPoints(data);
    }

    private void fillPoints(List<String> data) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                points[y][x] = data.get(y).charAt(x) - '0';
            }
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(points[y][x]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
