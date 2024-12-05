
package aoc2024j.day5;

import utils.IO;

import java.util.*;
import java.util.function.Predicate;

class Rules {
    Map<Integer, Set<Integer>> afters = new HashMap<>();

    void parseAndAdd(String line) {
        var parts = line.split("\\|");
        var before = Integer.parseInt(parts[0]);
        var after = Integer.parseInt(parts[1]);
        this.afters.computeIfAbsent(before, k -> new HashSet<>()).add(after);
    }

    long part1(Updates update) {
        return update.updates.stream()
                .filter(this::countValid)
                .mapToLong(Update::middle)
                .sum();
    }

    private boolean countValid(Update update) {
        for (int i = 0; i < update.pages.size() - 1; i++) {
            var validAfters = this.afters.get(update.pages.get(i));
            if (validAfters == null) {
                return false;
            }
            for (int j = i + 1; j < update.pages.size(); j++) {
                if (!validAfters.contains(update.pages.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    Comparator<Integer> comparator() {
        return (i, j) -> {
            if (i.equals(j)) {
                return 0;
            }
            var aftersOfI = this.afters.getOrDefault(i, Set.of());
            if (aftersOfI.contains(j)) {
                return -1;
            }
            return 1;
        };
    }

    long part2(Updates update) {
        return update.updates.stream()
                .filter(Predicate.not(this::countValid))
                .map(u -> Update.sort(u, this.comparator()))
                .mapToLong(Update::middle)
                .sum();
    }
}

class Update {
    final List<Integer> pages;

    Update(List<Integer> pages) {
        if (pages.size() % 2 != 1) {
            throw new IllegalArgumentException("Expected odd pages, got " + pages.size());
        }
        this.pages = pages;
    }

    static Update parse(String line) {
        var parts = line.split(",");
        var list = Arrays.stream(parts)
                .map(Integer::parseInt)
                .toList();
        return new Update(list);
    }

    public long middle() {
        return pages.get(pages.size() / 2);
    }

    static Update sort(Update update, Comparator<Integer> comparator) {
        var sorted = new ArrayList<>(update.pages);
        sorted.sort(comparator);
        return new Update(sorted);
    }
}

class Updates {
    List<Update> updates = new ArrayList<>();

    void parseAndAdd(String line) {
        this.updates.add(Update.parse(line));
    }

}

record ParseResult(Rules rules, Updates updates) {
    static ParseResult parse(List<String> data) {
        var rules = new Rules();
        var updates = new Updates();
        for (var line : data) {
            if (line.isBlank()) {
                continue;
            }
            if (line.contains("|")) {
                rules.parseAndAdd(line);
            } else {
                updates.parseAndAdd(line);
            }
        }
        return new ParseResult(rules, updates);
    }
}

public class Day5 {

    long part1(List<String> data) {
        var parsed = ParseResult.parse(data);
        return parsed.rules().part1(parsed.updates());
    }

    long part2(List<String> data) {
        var parsed = ParseResult.parse(data);
        return parsed.rules().part2(parsed.updates());
    }

    public static void main(String[] args) {
        var day5 = new Day5();
        var data = IO.getResourceAsList("aoc2024/day5.txt");
        var part1 = day5.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day5.part2(data);
        System.out.println("part2 = " + part2);
    }
}