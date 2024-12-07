
package aoc2024j.day7;

import utils.IO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 {

    enum Operation {
        Add, Multiply, Concatenate
    }

    record Equation(long target, ArrayList<Long> numbers) {
        static Equation parse(String line) {
            var parts = line.split(": ");
            var target = Long.parseLong(parts[0]);
            var numbers = Arrays.stream(parts[1].split(" "))
                    .map(Long::parseLong)
                    .collect(Collectors.toCollection(ArrayList::new));
            return new Equation(target, numbers);
        }
    }

    record Node<A>(A value, Node<A> next) {
    }

    static class CachedCombinator<A> {
        final ArrayList<List<Node<A>>> cache = new ArrayList<>();
        final List<A> list;

        CachedCombinator(List<A> list) {
            this.list = list;
        }

        List<Node<A>> get(int n) {
            if (n < cache.size()) {
                return cache.get(n);
            } else if (n == 0) {
                var result = new ArrayList<Node<A>>(1);
                result.add(null);
                cache.add(result);
                return result;
            } else {
                var result = get(n - 1).stream()
                        .flatMap(parent -> list.stream().map(head -> new Node<>(head, parent)))
                        .toList();
                cache.add(result);
                return result;
            }
        }
    }

    static class Generator {

        static long concatLong(long a, long b) {
            long acc = a * 10;
            long n = b / 10;
            while (n > 0) {
                acc *= 10;
                n /= 10;
            }
            return acc + b;
        }

        static boolean generateAndTest(long target, ArrayList<Long> numbers, Node<Operation> ops) {
            int i = 0;
            var acc = numbers.get(i++);
            while (acc <= target && ops != null) {
                var n = numbers.get(i++);
                var op = ops.value();
                ops = ops.next();
                switch (op) {
                    case Add -> acc += n;
                    case Multiply -> acc *= n;
                    case Concatenate -> acc = concatLong(acc, n);
                }
                if (acc > target) {
                    return false;
                }
            }
            return acc == target;
        }

        static boolean canBeTrue(CachedCombinator<Operation> cachedCombinator, Equation equation) {
            var ops = cachedCombinator.get(equation.numbers.size() - 1);
            return ops.stream()
                    .anyMatch(op ->
                            generateAndTest(equation.target, equation.numbers, op));
        }
    }

    long part1(List<String> data) {
        var combinator = new CachedCombinator<>(List.of(Operation.Add, Operation.Multiply));
        return data.stream()
                .map(Equation::parse)
                .filter(equation -> Generator.canBeTrue(combinator, equation))
                .mapToLong(Equation::target)
                .sum();
    }

    long part2(List<String> data) {
        var combinator = new CachedCombinator<>(List.of(Operation.Add, Operation.Multiply,
                Operation.Concatenate));
        return data.stream()
                .map(Equation::parse)
                .filter(equation -> Generator.canBeTrue(combinator, equation))
                .mapToLong(Equation::target)
                .sum();
    }

    public static void main() {
        var day7 = new Day7();
        var data = IO.getResourceAsList("aoc2024/day7.txt");
        var part1 = day7.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day7.part2(data);
        System.out.println("part2 = " + part2);
    }
}