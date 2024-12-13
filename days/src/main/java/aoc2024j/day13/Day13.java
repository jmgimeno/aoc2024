
package aoc2024j.day13;

import utils.IO;

import java.math.BigInteger;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Gatherers;

public class Day13 {

    static class Parser {

        // Pattern to match "Button A: X+94, Y+34" capturing the two numbers but not the plus signs
        static Pattern buttonPattern = Pattern.compile("Button [AB]: X\\+(\\d+), Y\\+(\\d+)");
        static Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

        static List<Machine> parse(List<String> data) {
            return parse(data, BigInteger.ZERO);
        }

        static List<Machine> parse(List<String> data, BigInteger increment) {
            return data.stream().filter(s -> !s.isEmpty())
                    .gather(Gatherers.windowFixed(3))
                    .map(trio -> parseMachine(trio, increment))
                    .toList();
        }

        static Machine parseMachine(List<String> triplet, BigInteger increment) {
            var matchA = buttonPattern.matcher(triplet.get(0));
            var matchB = buttonPattern.matcher(triplet.get(1));
            var matchPrize = prizePattern.matcher(triplet.get(2));
            matchA.find();
            matchB.find();
            matchPrize.find();
            var a = new Button(new BigInteger(matchA.group(1)), new BigInteger(matchA.group(2)));
            var b = new Button(new BigInteger(matchB.group(1)), new BigInteger(matchB.group(2)));
            var incrementX = increment.add(new BigInteger(matchPrize.group(1)));
            var incrementY = increment.add(new BigInteger(matchPrize.group(2)));
            var prize = new Prize(incrementX, incrementY);
            return new Machine(a, b, prize);
        }
    }

    record Button(BigInteger dx, BigInteger dy) {
    }

    record Prize(BigInteger x, BigInteger y) {
    }

    record Execution(BigInteger prizes, BigInteger tokens) {
    }

    record Machine(Button a, Button b, Prize prize) {

        static BigInteger determinant(BigInteger m00, BigInteger m01, BigInteger m10,
                                      BigInteger m11) {
            return m00.multiply(m11).subtract(m01.multiply(m10));
        }

        Execution execute() {
            var det = determinant(a.dx(), b.dx(), a.dy(), b.dy());
            if (det.equals(BigInteger.ZERO)) {
                return new Execution(BigInteger.ZERO, BigInteger.ZERO);
            }
            var detA = determinant(prize.x(), b.dx(), prize.y(), b.dy());
            var detB = determinant(a.dx(), prize.x(), a.dy(), prize.y());
            var absDet = det.abs();
            if (!detA.mod(absDet).equals(BigInteger.ZERO) || !detB.mod(absDet).equals(BigInteger.ZERO)) {
                return new Execution(BigInteger.ZERO, BigInteger.ZERO);
            } else {
                var pushA = detA.divide(det);
                var pushB = detB.divide(det);
                var tokens = new BigInteger("3").multiply(pushA).add(pushB);
                return new Execution(BigInteger.ONE, tokens);
            }
        }
    }

    BigInteger totalTokens(List<Machine> machines) {
        return machines.stream()
                .map(Machine::execute)
                .map(Execution::tokens)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    long part1(List<String> data) {
        var machines = Parser.parse(data);
        return totalTokens(machines).longValue();
    }

    BigInteger part2(List<String> data) {
        var machines = Parser.parse(data, new BigInteger("10000000000000"));
        return totalTokens(machines);
    }

    public static void main() {
        var day13 = new Day13();
        var data = IO.getResourceAsList("aoc2024/day13.txt");
        var part1 = day13.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day13.part2(data);
        System.out.println("part2 = " + part2);
    }
}