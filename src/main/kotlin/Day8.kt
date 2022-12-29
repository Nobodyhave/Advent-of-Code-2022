
import kotlin.math.max

class Day8 {
    fun solve(){
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_8.txt")!!.split("\n")
        val width = input[0].length
        val height = input.size
        val trees = Array(height) { Array(width) { 0 } }
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                trees[i][j] = c.digitToInt()
            }
        }

        val ltr = getCopy(trees)
        for (i in 1 until height - 1) {
            for (j in 1 until width - 1) {
                ltr[i][j] = max(ltr[i][j], ltr[i][j - 1])
            }
        }

        val rtl = getCopy(trees)
        for (i in 1 until height - 1) {
            for (j in width - 2 downTo 1) {
                rtl[i][j] = max(rtl[i][j], rtl[i][j + 1])
            }
        }

        val ttb = getCopy(trees)
        for (j in 1 until width - 1) {
            for (i in 1 until height - 1) {
                ttb[i][j] = max(ttb[i][j], ttb[i - 1][j])
            }
        }

        val btt = getCopy(trees)
        for (j in 1 until width - 1) {
            for (i in height - 2 downTo 1) {
                btt[i][j] = max(btt[i][j], btt[i + 1][j])
            }
        }

        val visibility = Array(input.size) { Array(input[0].length) { true } }
        for (i in 1 until height - 1) {
            for (j in 1 until width - 1) {
                val curHeight = trees[i][j]
                visibility[i][j] = (ltr[i][j - 1] < curHeight || rtl[i][j + 1] < curHeight
                        || ttb[i - 1][j] < curHeight || btt[i + 1][j] < curHeight)
            }
        }

        val result = visibility.flatten().count { it }
        println(result)
    }

    private fun solve2() {
        val input = readFile("day_8.txt")!!.split("\n")
        val width = input[0].length
        val height = input.size
        val trees = Array(height) { Array(width) { 0 } }
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                trees[i][j] = c.digitToInt()
            }
        }

        var maxScore = 0
        for (i in 1 until height - 1) {
            for (j in 1 until width - 1) {
                val dist = mutableListOf(1, 1, 1, 1)

                var k = j - 1
                while (k > 0 && trees[i][k] < trees[i][j]) {
                    dist[0]++
                    k--
                }

                k = j + 1
                while (k < width - 1 && trees[i][k] < trees[i][j]) {
                    dist[1]++
                    k++
                }

                k = i - 1
                while (k > 0 && trees[k][j] < trees[i][j]) {
                    dist[2]++
                    k--
                }

                k = i + 1
                while (k < height - 1 && trees[k][j] < trees[i][j]) {
                    dist[3]++
                    k++
                }

                println(dist)
                maxScore = max(maxScore, dist.reduce { d1, d2 -> d1 * d2})
            }
        }

        println(maxScore)
    }

    private fun getCopy(array: Array<Array<Int>>): Array<Array<Int>> {
        val copy = Array(array.size) { Array(array[0].size) { 0 } }
        for (i in array.indices) {
            for (j in array[i].indices) {
                copy[i][j] = array[i][j]
            }
        }
        return copy
    }
}