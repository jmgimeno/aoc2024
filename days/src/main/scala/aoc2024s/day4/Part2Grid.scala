package aoc2024s.day4

class Part2Grid(lines: List[String]) {
  val grid: Vector[String] = lines.toVector
  val height: Int = grid.size
  val width: Int = grid(0).length

  def charAt(x: Int, y: Int): Char = {
    grid(y)(x)
  }

  def count(target: String): Long = {
    assert(target.length == 3)
    (for
      x <- 1 until width - 1
      y <- 1 until height - 1
      if charAt(x, y) == target(1)
      if charAt(x - 1, y - 1) == target(0) && charAt(x + 1, y + 1) == target(2)
        || charAt(x - 1, y - 1) == target(2) && charAt(x + 1, y + 1) == target(0)
      if charAt(x + 1, y - 1) == target(0) && charAt(x - 1, y + 1) == target(2)
        || charAt(x + 1, y - 1) == target(2) && charAt(x - 1, y + 1) == target(0)
    yield ()).size
  }
}

