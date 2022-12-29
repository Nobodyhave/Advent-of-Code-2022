class Day24 {
    fun solve() {
        solve1()
        solve2()
    }

    private var min = Int.MAX_VALUE
    private val visited = mutableSetOf<State>()

    private fun solve1() {
        val input = readFile("day_24.txt")!!.split("\n")
        val grid = getGrid(input)
        val path = Path(-1, input[0].indexOf('.') - 1, grid.size, input.last().lastIndexOf('.') - 1)

        move(grid, 0, path, path.startRow, path.startCol)

        println("Result: $min")
    }

    private fun solve2() {
        val input = readFile("day_24.txt")!!.split("\n")
        val grid = getGrid(input)
        var path = Path(-1, input[0].indexOf('.') - 1, grid.size, input.last().lastIndexOf('.') - 1)

        var result = 0
        min = Int.MAX_VALUE
        visited.clear()
        move(grid, 0, path, path.startRow, path.startCol)
        result = min
        println("Min 2-1: $min")
        println("Result 2: $result")

        min = Int.MAX_VALUE
        visited.clear()
        path = path.copy(
            startRow = path.destRow, startCol = path.destCol,
            destRow = path.startRow, destCol = path.startCol
        )
        move(grid, result, path, path.startRow, path.startCol)
        result = min
        println("Min 2-2: $min")
        println("Result2: $result")

        min = Int.MAX_VALUE
        visited.clear()
        path = path.copy(
            startRow = path.destRow, startCol = path.destCol,
            destRow = path.startRow, destCol = path.startCol
        )
        move(grid, result, path, path.startRow, path.startCol)
        result = min

        println("Min 2-3: $min")
        println("Result2: $result")
    }

    private fun getGrid(input: List<String>): List<String> {
        return input
            .drop(1)
            .map { it.substring(1, it.lastIndex) }
            .dropLast(1)
    }

    private fun move(
        grid: List<String>,
        minutes: Int,
        path: Path,
        row: Int,
        col: Int
    ) {
        if (minutes >= min) return
        if (path.destRow == row && path.destCol == col) {
            min = minutes
            return
        }
        if (visited.contains(State(row, col, minutes))) return
        if (minutes >= 1000) return

        if (canMove(grid, row - 1, col, path, minutes + 1)) {
            move(grid, minutes + 1, path, row - 1, col)
            visited.add(State(row - 1, col, minutes + 1))
        }
        if (canMove(grid, row + 1, col, path, minutes + 1)) {
            move(grid, minutes + 1, path, row + 1, col)
            visited.add(State(row + 1, col, minutes + 1))
        }
        if (canMove(grid, row, col - 1, path, minutes + 1)) {
            move(grid, minutes + 1, path, row, col - 1)
            visited.add(State(row, col - 1, minutes + 1))
        }
        if (canMove(grid, row, col + 1, path, minutes + 1)) {
            move(grid, minutes + 1, path, row, col + 1)
            visited.add(State(row, col + 1, minutes + 1))
        }
        if (canMove(grid, row, col, path, minutes + 1)) {
            move(grid, minutes + 1, path, row, col)
            visited.add(State(row, col, minutes + 1))
        }
    }

    private fun canMove(
        grid: List<String>,
        row: Int, col: Int, path: Path, minutes: Int
    ): Boolean {
        return (row == path.startRow && col == path.startCol) ||
                (row == path.destRow && col == path.destCol) ||
                (row in 0..grid.lastIndex &&
                        col in 0..grid[0].lastIndex &&
                        !isBlizzard(grid, row, col, minutes))
    }

    private fun isBlizzard(grid: List<String>, row: Int, col: Int, minutes: Int): Boolean {
        return isLeftBlizzard(grid, row, col, minutes) ||
                isRightBlizzard(grid, row, col, minutes) ||
                isTopBlizzard(grid, row, col, minutes) ||
                isDownBlizzard(grid, row, col, minutes)
    }

    private fun isRightBlizzard(grid: List<String>, row: Int, col: Int, minutes: Int): Boolean {
        val width = grid[0].length
        val index = (width + (col - minutes % width)) % width

        return grid[row][index] == '>'
    }

    private fun isLeftBlizzard(grid: List<String>, row: Int, col: Int, minutes: Int): Boolean {
        val width = grid[0].length
        val index = (col + minutes % width) % width

        return grid[row][index] == '<'
    }

    private fun isTopBlizzard(grid: List<String>, row: Int, col: Int, minutes: Int): Boolean {
        val height = grid.size
        val index = (row + minutes % height) % height

        return grid[index][col] == '^'
    }

    private fun isDownBlizzard(grid: List<String>, row: Int, col: Int, minutes: Int): Boolean {
        val height = grid.size
        val index = (height + (row - minutes % height)) % height

        return grid[index][col] == 'v'
    }

    private data class State(val row: Int, val col: Int, val minutes: Int)

    private data class Path(
        val startRow: Int,
        val startCol: Int,
        val destRow: Int,
        val destCol: Int
    )
}