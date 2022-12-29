import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day14 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_14.txt")!!.split("\n").map {
            it.split("->")
                .map { it.trim() }
                .map {
                    val (x,y) = it.split(",")
                    Point(x.toInt(), y.toInt()) }
        }
        val left = input.flatten().minOf { it.x }
        val right = input.flatten().maxOf { it.x }
        val top = 0
        val bottom = input.flatten().maxOf { it.y }

        val grid = Array(bottom - top + 1) { CharArray(right - left + 1) { '.' } }

        setRocks(left, input, grid)
        val result = pourSand(500 - left, false, grid)

        println(result)
    }

    private fun solve2() {
        val input = readFile("day_14.txt")!!.split("\n").map {
            it.split("->")
                .map { it.trim() }
                .map {
                    val (x,y) = it.split(",")
                    Point(x.toInt(), y.toInt()) }
        }.toMutableList()
        val bottom = input.flatten().maxOf { it.y }
        input.add(listOf(Point(0, bottom + 2), Point(999, bottom + 2)))

        val grid = Array(bottom + 3) { CharArray(1000) { '.' } }

        setRocks(0, input, grid)
        val result = pourSand(500, true, grid)

        println(result)
    }

    private fun setRocks(
        left: Int,
        input: List<List<Point>>,
        grid: Array<CharArray>) {
        input.forEachIndexed { i, line ->
            var prev = line[0]
            for (j in 1 until line.size) {
                val cur = line[j]
                if(abs(prev.x - cur.x) == 0) {
                    for(k in min(prev.y, cur.y)..max(prev.y, cur.y)) {
                        grid[k][cur.x - left] = '#'
                    }
                } else {
                    for(k in min(prev.x, cur.x)..max(prev.x, cur.x)) {
                        grid[cur.y][k - left] = '#'
                    }
                }
                prev = cur
            }
        }
    }

    private fun pourSand(startX: Int, isFloor: Boolean, grid: Array<CharArray>): Int {
        var count = 0

        outer@while (true) {
            var x = startX
            var y = 0
            while (true) {
                if(!isFloor && y == grid.size - 1) break@outer
                if(grid[y+1][x] == '.') {
                    y++
                    continue
                }
                if(!isFloor && x-1 < 0) break@outer
                if(grid[y+1][x-1] == '.') {
                    x--
                    y++
                    continue
                }
                if(!isFloor && x+1 >= grid[0].size) break@outer
                if(grid[y+1][x+1] == '.') {
                    x++
                    y++
                    continue
                }
                grid[y][x] = 'o'
                count++
                if(!isFloor) break
                if(y == 0) break@outer else break
            }
        }

        return count
    }

    data class Point(val x: Int, val y: Int)
}