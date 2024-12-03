
package aoc2024j.day3;

import utils.IO;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Gatherers;

public class Day3 {

    static final Pattern part1Pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

    long sumLine(String line) {
        var matcher = part1Pattern.matcher(line).results();
        return matcher
                .mapToLong(m -> Long.parseLong(m.group(1)) * Long.parseLong(m.group(2)))
                .sum();
    }

    long part1(List<String> data) {
        return data.stream()
                .mapToLong(this::sumLine)
                .sum();
    }

    // make a regexp that matches mul(xxx,yyy) or do() or don't()

    static final Pattern part2Pattern = Pattern.compile(
            "mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");

    record State(long result, boolean doMul) {
    }

    State sumLine2(State state, String line) {
        var matcher = part2Pattern.matcher(line).results();
        return matcher.gather(
                Gatherers.fold(
                        () -> state,
                        (s, m) -> {
                            if (m.group().startsWith("mul")) {
                                if (s.doMul) {
                                    return new State(
                                            s.result + Long.parseLong(m.group(1)) * Long.parseLong(m.group(2)),
                                            s.doMul);
                                } else {
                                    return s;
                                }
                            } else if (m.group().startsWith("don't")) {
                                return new State(s.result, false);
                            } else {
                                return new State(s.result, true);
                            }
                        }
                )).findFirst().orElseThrow();
    }

    long part2(List<String> data) {
        return data.stream()
                .gather(
                        Gatherers.fold(
                                () -> new State(0, true),
                                this::sumLine2
                        ))
                .findFirst()
                .orElseThrow()
                .result;
    }

    public static void main(String[] args) {
        var day3 = new Day3();
        var data = IO.getResourceAsList("aoc2024/day3.txt");
        var part1 = day3.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day3.part2(data);
        System.out.println("part2 = " + part2);
    }
}