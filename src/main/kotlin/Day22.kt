import kotlin.math.min

class Day22 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_22.txt")!!.split("\n\n")
        val inputBoard = input.first()
        val inputPath = input.drop(1).first()

        val board = getBoard(inputBoard)
        val path = getPath(inputPath)
        val result = move(board, path, false)

        println(result)
    }

    private fun solve2() {
        val input = readFile("day_22.txt")!!.split("\n\n")
        val inputBoard = input.first()
        val inputPath = input.drop(1).first()

        val board = getBoard(inputBoard)
        val path = getPath(inputPath)
        val result = move(board, path, true)

        println("Result 2:$result")
    }

    private fun getBoard(input: String): Array<CharArray> {
        val lines = input.split("\n")
        val width = lines.maxOf { it.length }
        val array = Array(lines.size) { CharArray(width) { ' ' } }

        lines.forEachIndexed { si, s ->
            s.forEachIndexed { ci, c ->
                array[si][ci] = c
            }
        }

        return array
    }

    private fun getPath(input: String): List<Pair<Int, Char>> {
        val steps = input.split(Regex("[RL]")).map { it.toInt() }
        val directions = input.replace(Regex("\\d"), "")

        return steps.zip(directions.asIterable()) { a, b -> a to b } + (steps.last() to ' ')
    }

    private fun move(board: Array<CharArray>, path: List<Pair<Int, Char>>, isPart2: Boolean): Long {
        var pos = Position(0, board[0].indexOfFirst { it == '.' }, Direction.Right)

        path.forEachIndexed {index, (steps, dir) ->
            for(i in 0 until steps) {
                pos = if(nextIsBorder(board, pos.dir, pos.row, pos.col)) {
                    if(nextIsWrappedWall(board, pos.dir, pos.row, pos.col, isPart2)) break
                    wrapAround(board, pos.dir, pos.row, pos.col, isPart2)
                } else {
                    if(nextIsWall(board, pos.dir, pos.row, pos.col)) break
                    step(pos.dir, pos.row, pos.col)
                }
            }
            pos = pos.copy(dir = pos.dir.turn(dir))
        }

        println("Row: ${pos.row + 1} Col: ${pos.col + 2} Dir: ${pos.dir}")
        return 1000L * (pos.row + 1) + 4L * (pos.col + 1) + pos.dir.getValue()
    }

    private fun nextIsBorder(board: Array<CharArray>, direction: Direction, row: Int, col:
    Int) : Boolean {
        return when(direction) {
            is Direction.Right -> col + 1 >= board[0].size || board[row][col + 1] == ' '
            is Direction.Up -> row - 1 < 0 || board[row - 1][col] == ' '
            is Direction.Left -> col - 1 < 0 || board[row][col - 1] == ' '
            is Direction.Down -> row + 1 >= board.size || board[row + 1][col] == ' '
        }
    }

    private fun nextIsWall(board: Array<CharArray>, direction: Direction, row: Int, col:
    Int) : Boolean {
        return when (direction) {
            is Direction.Right -> board[row][col + 1] == '#'
            is Direction.Up -> board[row-1][col] == '#'
            is Direction.Left -> board[row][col - 1] == '#'
            is Direction.Down -> board[row + 1][col] == '#'
        }
    }

    private fun nextIsWrappedWall(board: Array<CharArray>, direction: Direction, row: Int, col:
    Int, isPart2: Boolean) : Boolean {
        val nextPosition = nextWrappedStep(board, direction, row, col, isPart2)
        return board[nextPosition.row][nextPosition.col] == '#'
    }

    private fun nextWrappedStep(board: Array<CharArray>, direction: Direction, row: Int, col:
    Int, isPart2: Boolean) : Position {
        val size = min(board.size, board[0].size) / 3
        return if(isPart2) {
            when  (getFace(board, row, col)) {
                is Face.Top -> {
                    when (direction) {
                        is Direction.Up -> {
                            Position(2 * size + col, 0, Direction.Right)
                        }
                        is Direction.Left -> {
                            Position(3* size - 1 - row, 0, Direction.Right)
                        }
                        else -> throw IllegalStateException("Wrapping is not allowed. Row: $row " +
                                "col: $col dir: $direction")
                    }
                }
                is Face.Bottom -> {
                    when (direction) {
                        is Direction.Right -> {
                            Position(3 * size - 1 - row, 3 * size - 1, Direction.Left)
                        }
                        is Direction.Down -> {
                            Position(2 * size + col, size - 1, Direction.Left)
                        }
                        else -> throw IllegalStateException(
                            "Wrapping is not allowed. Row: $row " +
                                    "col: $col dir: $direction"
                        )
                    }
                }
                is Face.Left -> {
                    when (direction) {
                        is Direction.Up -> {
                            Position(col + size, size, Direction.Right)
                        }
                        is Direction.Left -> {
                            Position(3 * size - 1 - row, size, Direction.Right)
                        }
                        else -> throw IllegalStateException(
                            "Wrapping is not allowed. Row: $row " +
                                    "col: $col dir: $direction"
                        )
                    }
                }
                is Face.Right -> {
                    when (direction) {
                        is Direction.Up -> {
                            Position(4 * size - 1, col - 2 * size, Direction.Up)
                        }
                        is Direction.Down -> {
                            Position(col - size, 2 * size - 1, Direction.Left)
                        }
                        is Direction.Right -> {
                            Position(3 * size - 1 - row, 2 * size - 1, Direction.Left)
                        }
                        else -> throw IllegalStateException(
                            "Wrapping is not allowed. Row: $row " +
                                    "col: $col dir: $direction"
                        )
                    }
                }
                is Face.Front -> {
                    when (direction) {
                        is Direction.Left -> {
                            Position(2 * size, row - size, Direction.Down)
                        }
                        is Direction.Right -> {
                            Position(size - 1, row + size, Direction.Up)
                        }
                        else -> throw IllegalStateException(
                            "Wrapping is not allowed. Row: $row " +
                                    "col: $col dir: $direction"
                        )
                    }
                }
                is Face.Back -> {
                    when (direction) {
                        is Direction.Right -> {
                            Position(3 * size - 1, row - 2 * size, Direction.Up)
                        }
                        is Direction.Down -> {
                            Position(0, 2 * size + col, Direction.Down)
                        }
                        is Direction.Left -> {
                            Position(0, row - 2 * size, Direction.Down)
                        }
                        else -> throw IllegalStateException(
                            "Wrapping is not allowed. Row: $row " +
                                    "col: $col dir: $direction"
                        )
                    }
                }
            }
        } else {
            when (direction) {
                is Direction.Right -> {
                    val firstIndex = board[row].indexOfFirst { it == '.' || it == '#' }
                    Position(row, firstIndex, direction)
                }
                is Direction.Up -> {
                    val firstIndex = board.indices.reversed().indexOfFirst {
                        board[it][col] == '.' || board[it][col] == '#'
                    }
                    Position(board.lastIndex - firstIndex, col, direction)
                }
                is Direction.Left -> {
                    val firstIndex = board[row].reversed().indexOfFirst { it == '.' || it == '#' }
                    Position(row, board[row].lastIndex - firstIndex, direction)
                }
                is Direction.Down -> {
                    val firstIndex = board.indices.indexOfFirst {
                        board[it][col] == '.' || board[it][col] == '#'
                    }
                    Position(firstIndex, col, direction)
                }
            }
        }
    }

    private fun wrapAround(board: Array<CharArray>, direction: Direction, row: Int, col: Int, isPart2: Boolean):
            Position {
        return nextWrappedStep(board, direction, row, col, isPart2)
    }

    private fun getFace(board: Array<CharArray>, row: Int, col: Int): Face {
        val size = min(board.size, board[0].size) / 3
        return when {
            row in 0 until size && col in size until 2 * size -> Face.Top
            row in 2 * size until 3 * size && col in size until 2 * size -> Face.Bottom
            row in 2 * size until 3 * size && col in 0 until size -> Face.Left
            row in 0 until size && col in 2 * size until 3 * size -> Face.Right
            row in size until 2 * size && col in size until 2 * size -> Face.Front
            row in 3 * size until 4 * size && col in 0 until size -> Face.Back
            else -> throw IllegalArgumentException("Wrong coordinated. Row: $row col: $col")
        }
    }

    private fun step(direction: Direction, row: Int, col: Int):
            Position {
        return when (direction) {
            is Direction.Right -> Position(row, col + 1, direction)
            is Direction.Up -> Position(row - 1, col, direction)
            is Direction.Left -> Position(row, col - 1, direction)
            is Direction.Down -> Position(row + 1, col, direction)
        }
    }

    data class Position(val row: Int, val col: Int, val dir: Direction)

    sealed class Face {
        object Top: Face()
        object Bottom: Face()
        object Left: Face()
        object Right: Face()
        object Front: Face()
        object Back: Face()
    }

    sealed class Direction {
        object Left : Direction()
        object Right : Direction()
        object Up : Direction()
        object Down : Direction()

        fun turn(char: Char): Direction {
            return if (char == ' ')
                this
            else
                return when (this) {
                    is Left -> if (char == 'L') Down else Up
                    is Down -> if (char == 'L') Right else Left
                    is Right -> if (char == 'L') Up else Down
                    is Up -> if (char == 'L') Left else Right
                }
        }

        fun getValue(): Long {
            return when (this) {
                is Left -> 2L
                is Down -> 1L
                is Right -> 0L
                is Up -> 3L
            }
        }
    }
}