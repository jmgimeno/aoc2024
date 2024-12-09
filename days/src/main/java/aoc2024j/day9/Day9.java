
package aoc2024j.day9;

import utils.IO;

import java.util.*;
import java.util.function.IntConsumer;

public class Day9 {

    record Block(long start, long size) {
        long checksum() {
            return ((2 * start + size - 1) * size) / 2;
        }
    }

    static class FileSystem {
        final LinkedHashMap<Integer, List<Block>> files = new LinkedHashMap<>();
        final List<Block> free = new ArrayList<>(); // free blocks with leftmost as last

        static FileSystem fromDense(String dense) {
            var fs = new FileSystem();
            int start = 0;
            for (int i = 0; i < dense.length(); i++) {
                int size = Integer.parseInt(dense.substring(i, i + 1));
                var block = new Block(start, size);
                if (size == 0) continue;
                if (i % 2 == 0) {
                    var blocks = new ArrayList<Block>();
                    blocks.add(block);
                    fs.files.put(i / 2, blocks);
                } else {
                    fs.free.add(block);
                }
                start += size;
            }
            Collections.reverse(fs.free); // leftmost block is last
            return fs;
        }

        void compactFileSystem(IntConsumer fileCompactor) {
            for (var entry : files.sequencedEntrySet().reversed()) {
                var id = entry.getKey();
                if (!free.isEmpty()) {
                    fileCompactor.accept(id);
                }
            }
        }

        private void compactBlocks(Integer id) {
            long fileStart = files.get(id).getFirst().start;
            long fileSize = files.get(id).getFirst().size;
            var newFileBlocks = new ArrayList<Block>();
            while (fileSize > 0 && !free.isEmpty() && free.getLast().start < fileStart) {
                var freeBlock = free.removeLast();
                var newFreeBlock = new Block(freeBlock.start + fileSize, freeBlock.size - fileSize);
                if (newFreeBlock.size > 0) free.addLast(newFreeBlock);
                var newFileBlock = new Block(freeBlock.start, Math.min(fileSize, freeBlock.size));
                newFileBlocks.add(newFileBlock);
                fileSize -= newFileBlock.size;
            }
            if (fileSize > 0) {
                var newFileBlock = new Block(fileStart, fileSize);
                newFileBlocks.add(newFileBlock);
            }
            files.put(id, newFileBlocks);
        }

        private void compactWholeFiles(Integer id) {
            long fileStart = files.get(id).getFirst().start;
            long fileSize = files.get(id).getFirst().size;
            for (int i = free.size() - 1; i >= 0; i--) {
                var freeBlock = free.get(i);
                if (freeBlock.start > fileStart) continue;
                if (freeBlock.size >= fileSize) {
                    var newFreeBlock = new Block(freeBlock.start + fileSize, freeBlock.size - fileSize);
                    if (newFreeBlock.size > 0) free.set(i, newFreeBlock);
                    else free.remove(i);
                    var newFileBlock = new Block(freeBlock.start, fileSize);
                    files.put(id, List.of(newFileBlock));
                    break;
                }
            }
        }

        long checksum() {
            return files.entrySet().stream()
                    .mapToLong(entry ->
                            entry.getValue().stream()
                                    .mapToLong(Block::checksum).sum() * entry.getKey())
                    .sum();
        }
    }

    long part1(List<String> data) {
        var fs = FileSystem.fromDense(data.getFirst());
        fs.compactFileSystem(fs::compactBlocks);
        return fs.checksum();
    }

    long part2(List<String> data) {
        var fs = FileSystem.fromDense(data.getFirst());
        fs.compactFileSystem(fs::compactWholeFiles);
        return fs.checksum();
    }

    public static void main() {
        var day9 = new Day9();
        var data = IO.getResourceAsList("aoc2024/day9.txt");
        var part1 = day9.part1(data);
        System.out.println("part1 = " + part1);
        var part2 = day9.part2(data);
        System.out.println("part2 = " + part2);
    }
}
