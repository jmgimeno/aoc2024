package aoc2024s.day4

class Part1Grid(lines: List[String]) {
  val grid: Vector[String] = lines.toVector
  val height: Int = grid.size
  val width: Int = grid(0).length

  def charAt(x: Int, y: Int): Char = {
    grid(y)(x)
  }

  def horizontal: Iterator[String] = {
    grid.iterator
  }

  def vertical: Iterator[String] = {
    (0 until width)
      .iterator
      .map(x => (0 until height).map(y => charAt(x, y)).mkString)
  }

  def diagonal: Iterator[String] = {
    assert(width == height)
    val bigDiagonal =
      (0 until width).map(x => charAt(x, x)).mkString
    val higherDiagonals =
      (1 until width).iterator.map { x =>
        (0 until width - x).map(y => charAt(x + y, y)).mkString
      }
    val lowerDiagonals =
      (1 until width).iterator.map { y =>
        (0 until width - y).map(x => charAt(x, y + x)).mkString
      }
    Iterator(bigDiagonal) ++ higherDiagonals ++ lowerDiagonals
  }

  def contraDiagonal: Iterator[String] = {
    // (x, y) in diagonal => (x, width - y - 1) in contra-diagonal
    assert(width == height)
    val bigContraDiagonal =
      (0 until width).map(x => charAt(x, width - x - 1)).mkString
    val upperContradiagonals =
      (1 until width).iterator.map { x =>
        (0 until width - x).map(y => charAt(x + y, width - y - 1)).mkString
      }
    val lowerContraDiagonals =
      (1 until width).iterator.map { y =>
        (0 until width - y).map(x => charAt(x, width - y - x - 1)).mkString
      }
    Iterator(bigContraDiagonal) ++ upperContradiagonals ++ lowerContraDiagonals
  }

  def all: Iterator[String] = {
    horizontal ++ vertical ++ diagonal ++ contraDiagonal
  }

  def count(target: String): Long = {
    val reversed = target.reverse
    all
      .flatMap(_.sliding(target.length))
      .count(word => {
        word == target || word == reversed
      })
  }
}

