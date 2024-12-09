
package aoc2024s.day9;

import utils.IO

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

object Day9 {

  def checksum(id: Int, start: Long, size: Long): Long =
    id * ((2 * start + size - 1) * size) / 2;

  case class FileBlock(id: Int, start: Long, size: Long) {
    def checksum: Long = Day9.checksum(id, start, size);
  }

  case class FreeBlock(start: Long, size: Long)

  def fromDense(dense: String): (List[FileBlock], List[FreeBlock]) = {
    val lengths = dense.split("").map(Integer.parseInt).toList
    val starts = lengths.scanLeft(0L)(_ + _)
    val initial = starts.zip(lengths).zipWithIndex
    val (files, free) = initial.partition(_._2 % 2 == 0)
    val fileBlocks = files.map { case ((start, size), id) => FileBlock(id / 2, start, size) }
    val freeBlocks = free.map { case ((start, size), _) => FreeBlock(start, size) }
    (fileBlocks, freeBlocks)
  }

  @tailrec
  final def part1(fileBlocks: List[FileBlock], freeBlocks: List[FreeBlock], checksum: Long = 0L): Long = {
    fileBlocks match {
      case Nil => checksum
      case (lastFile@FileBlock(id, fileStart, fileSize)) :: restFiles =>
        freeBlocks match
          case Nil => checksum + fileBlocks.map(_.checksum).sum
          case FreeBlock(freeStart, freeSize) :: restFree =>
            if freeStart >= fileStart then
              checksum + fileBlocks.map(_.checksum).sum
            else if fileSize <= freeSize then
              val newChecksum = checksum + Day9.checksum(id, freeStart, fileSize)
              val newFreeBlock = FreeBlock(freeStart + fileSize, freeSize - fileSize)
              if fileSize == freeSize then
                part1(restFiles, restFree, newChecksum)
              else
                part1(restFiles, newFreeBlock :: restFree, newChecksum)
            else
              val restSize = fileSize - freeSize
              val newChecksum = checksum + Day9.checksum(id, freeStart, freeSize)
              val newFileBlock = FileBlock(id, fileStart, restSize)
              part1(newFileBlock :: restFiles, restFree, newChecksum)
    }
  }

  @tailrec
  final def part2(fileBlocks: List[FileBlock], freeBlocks: Vector[FreeBlock], checksum: Long = 0L): Long = {
    fileBlocks match {
      case Nil => checksum
      case (lastFile@FileBlock(id, fileStart, fileSize)) :: restFiles =>
        freeBlocks.indexWhere(fileSize <= _.size) match
          case -1 => part2(restFiles, freeBlocks, checksum + lastFile.checksum)
          case i =>
            val FreeBlock(freeStart, freeSize) = freeBlocks(i)
            if freeStart >= fileStart then
              part2(restFiles, freeBlocks, checksum + lastFile.checksum)
            else
              val newChecksum = checksum + Day9.checksum(id, freeStart, fileSize)
              val newFreeBlock = FreeBlock(freeStart + fileSize, freeSize - fileSize)
              if fileSize == freeSize then
                part2(restFiles, freeBlocks.patch(i, Nil, 1), newChecksum)
              else
                part2(restFiles, freeBlocks.updated(i, newFreeBlock), newChecksum)
    }
  }

  def part1(data: List[String]): Long = {
    val (fileBlocks, freeBlocks) = fromDense(data.head)
    part1(fileBlocks.reverse, freeBlocks)
  }

  def part2(data: List[String]): Long = {
    val (fileBlocks, freeBlocks) = fromDense(data.head)
    part2(fileBlocks.reverse, freeBlocks.toVector)
  }

  @main def main9(): Unit = {
    val data = IO.getResourceAsList("aoc2024/day9.txt").asScala.toList;
    val part1 = Day9.part1(data);
    println(s"part1 = $part1");
    val part2 = Day9.part2(data);
    println(s"part2 = $part2");
  }
}