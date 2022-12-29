import java.util.LinkedList

class Day12 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_12.txt")!!.split("\n")

        val bfs = LinkedList<Node>()
        input.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                if(c == 'S') {
                    bfs.addLast(Node(row, col, 0, 'a'))
                }
            }
        }

        routeFinder(input, bfs)
    }

    private fun solve2() {
        val input = readFile("day_12.txt")!!.split("\n")

        val bfs = LinkedList<Node>()
        input.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                if(c == 'S' || c == 'a') {
                    bfs.addLast(Node(row, col, 0, 'a'))
                }
            }
        }

        routeFinder(input, bfs)
    }

    private fun routeFinder(input: List<String>, bfs: LinkedList<Node>) {
        val width = input[0].length
        val height = input.size
        val marked = Array(height) { BooleanArray(width) { false } }

        while (bfs.isNotEmpty()) {
            val cur = bfs.pollFirst()
            if(marked[cur.row][cur.col]) continue

            if(cur.row != 0) {
                if(input[cur.row - 1][cur.col] == 'E' && cur.height >= 'y') {
                    println(cur.steps + 1)
                    break
                }
                if(input[cur.row - 1][cur.col].code - cur.height.code <= 1) {
                    bfs.addLast(
                        Node(
                            cur.row - 1,
                            cur.col,
                            cur.steps + 1,
                            input[cur.row - 1][cur.col]
                        )
                    )
                }
            }

            if(cur.col != 0) {
                if(input[cur.row][cur.col - 1] == 'E' && cur.height >= 'y') {
                    println(cur.steps + 1)
                    break
                }
                if(input[cur.row][cur.col - 1].code - cur.height.code <= 1) {
                    bfs.addLast(
                        Node(
                            cur.row,
                            cur.col - 1,
                            cur.steps + 1,
                            input[cur.row][cur.col - 1]
                        )
                    )
                }
            }

            if(cur.row != height - 1) {
                if(input[cur.row + 1][cur.col] == 'E' && cur.height >= 'y') {
                    println(cur.steps + 1)
                    break
                }
                if(input[cur.row + 1][cur.col].code - cur.height.code <= 1) {
                    bfs.addLast(
                        Node(
                            cur.row + 1,
                            cur.col,
                            cur.steps + 1,
                            input[cur.row + 1][cur.col]
                        )
                    )
                }
            }

            if(cur.col != width - 1) {
                if(input[cur.row][cur.col + 1] == 'E' && cur.height >= 'y') {
                    println(cur.steps + 1)
                    break
                }
                if(input[cur.row][cur.col + 1].code - cur.height.code <= 1) {
                    bfs.addLast(
                        Node(
                            cur.row,
                            cur.col + 1,
                            cur.steps + 1,
                            input[cur.row][cur.col + 1]
                        )
                    )
                }
            }

            marked[cur.row][cur.col] = true
        }
    }

    private data class Node(val row: Int, val col: Int, val steps: Int, val height: Char)
}