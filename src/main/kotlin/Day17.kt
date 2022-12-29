import kotlin.math.min

class Day17 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_17.txt")!!
        val count = 2022
        val grid = Array(count * 4) { CharArray(7) { '.' } }
        val figures = listOf(
            Figure.Plank::class.java,
            Figure.Cross::class.java,
            Figure.Corner::class.java,
            Figure.Stick::class.java,
            Figure.Square::class.java
        )

        val result = simulate(count, input, grid, figures)
        println(result)
    }

    // Bunch of hand made calculations
    // The idea of this part is that result should contain repetitive patterns
    // Because both inputs are also repetitive.
    // Maybe there is a way to find it mathematically, but don't know it
    // Instead I was taking random samples from the grid from the random place
    // And wait until I find the one, that repeats
    // Then I was finding a place, where is happens
    // And then it's a simple math to count
    private fun solve2() {
        val input = readFile("day_17.txt")!!
        val count = 3000
        // 1000 -> 1371(1520), 2000 -> 2908(3034), 3000 -> 4445(4548), 4000 -> 5929(6061)
        val grid = Array(count * 4) { CharArray(7) { '.' } }
        val figures = listOf(
            Figure.Plank::class.java,
            Figure.Cross::class.java,
            Figure.Corner::class.java,
            Figure.Stick::class.java,
            Figure.Square::class.java
        )

        val result = simulate(count, input, grid, figures)
        println(result)
        val gridToString = grid.map { String(it) }.reversed()

        // 53 repeat pattern for test
        // 2694 repeat pattern for real
        val i = 100
        val pat = gridToString.drop(463).take(i)
        gridToString
            .windowed(i, 1)
            .forEachIndexed { index, s ->
                if(s == pat) {
                    println("Index: $index")
                }
            }
        for (j in 1..count) {
            val g = Array(count * 4) { CharArray(7) { '.' } }
            val r = simulate(j, input, g, figures)
            println("Count: $j height: $r")
        }
        val rocks = 1000000000000L
        println((rocks - 309) % 1725)
        println(464 + (rocks - 309) / 1725 * 2694 + 1991)
    }

    private fun simulate(count: Int,
        input: String, grid: Array<CharArray>, figures: List<Class<out Figure>>): Int {
        var jetIndex = 0
        var figureIndex = 0
        var bottom = grid.size
        var figure = Figure.createFigure(2, bottom - 4, figures[0])
        var settled = false
        while (figureIndex < count) {
            if(settled) {
                figure = Figure.createFigure(2, bottom - 4, figures[figureIndex % 5])
                settled = false
            }

            val jet = input[jetIndex % input.length]

            if(jet == '<') {
                figure.moveLeft(grid)
            } else {
                figure.moveRight(grid)
            }

            if(!figure.moveDown(grid)) {
                settled = true
                bottom = min(bottom, figure.topPoint())
                figure.points.forEach {
                    grid[it.y][it.x] = '#'
                }
                figureIndex++
            }

            jetIndex++
        }

        return grid.size - bottom
    }

    sealed class Figure {
        abstract val points: List<Point>

        fun topPoint(): Int {
            return points.minOf { it.y }
        }

        fun moveLeft(grid: Array<CharArray>) {
            points.forEach {
                if(it.x - 1 < 0) return
                if(grid[it.y][it.x - 1] != '.') return
            }
            points.forEach { it.x-- }
        }

        fun moveRight(grid: Array<CharArray>) {
            points.forEach {
                if(it.x + 1 > 6) return
                if(grid[it.y][it.x + 1] != '.') return
            }
            points.forEach { it.x++ }
        }

        fun moveDown(grid: Array<CharArray>): Boolean {
            points.forEach {
                if(it.y + 1 >= grid.size) return false
                if(grid[it.y + 1][it.x] != '.') return false
            }
            points.forEach { it.y++ }

            return true
        }

        class Plank(left: Int, bottom: Int): Figure() {
            private val _points = listOf(
                Point(left, bottom),
                Point(left + 1, bottom),
                Point(left + 2, bottom),
                Point(left + 3, bottom),
            )

            override val points: List<Point>
                get() = _points
        }

        class Cross(left: Int, bottom: Int): Figure() {
            private val _points = listOf(
                Point(left, bottom - 1),
                Point(left + 1, bottom),
                Point(left + 1, bottom - 1),
                Point(left + 1, bottom - 2),
                Point(left + 2, bottom - 1),
            )

            override val points: List<Point>
                get() = _points
        }

        class Corner(left: Int, bottom: Int): Figure() {
            private val _points = listOf(
                Point(left, bottom),
                Point(left + 1, bottom),
                Point(left + 2, bottom),
                Point(left + 2, bottom - 1),
                Point(left + 2, bottom - 2),
            )

            override val points: List<Point>
                get() = _points
        }

        class Stick(left: Int, bottom: Int): Figure() {
            private val _points = listOf(
                Point(left, bottom),
                Point(left, bottom - 1),
                Point(left, bottom - 2),
                Point(left, bottom - 3),
            )

            override val points: List<Point>
                get() = _points
        }

        class Square(left: Int, bottom: Int): Figure() {
            private val _points = listOf(
                Point(left, bottom),
                Point(left + 1, bottom),
                Point(left, bottom - 1),
                Point(left + 1, bottom - 1),
            )

            override val points: List<Point>
                get() = _points
        }

        companion object {
            fun createFigure(left: Int, bottom: Int, clazz: Class<out Figure>): Figure {
                return when(clazz) {
                    Plank::class.java -> Plank(left, bottom)
                    Cross::class.java -> Cross(left, bottom)
                    Corner::class.java -> Corner(left, bottom)
                    Stick::class.java -> Stick(left, bottom)
                    Square::class.java -> Square(left, bottom)
                    else -> throw IllegalArgumentException("Unsupported figure")
                }
            }
        }
    }

    data class Point(var x: Int, var y: Int)
}