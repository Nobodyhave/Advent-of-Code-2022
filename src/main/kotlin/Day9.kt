import kotlin.math.abs

class Day9 {
    fun solve(){
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_9.txt")!!.split("\n")
        val moves = input.map { it.split(" ") }

        val result = move(moves, 2)

        println(result)
    }

    private fun solve2() {
        val input = readFile("day_9.txt")!!.split("\n")
        val moves = input.map { it.split(" ") }

        val result = move(moves, 10)

        println(result)
    }

    private fun move(moves: List<List<String>>, size: Int): Int {
        val knotsPos = Array(size) { Position(0, 0) }
        val positions = mutableSetOf(knotsPos.last())
        moves.forEach { (dir, steps) ->
            if(dir == "R") {
                for (step in 0 until steps.toInt()) {
                    knotsPos[0] = Position(knotsPos[0].x + 1, knotsPos[0].y)
                    for(i in 1 until knotsPos.size) {
                        knotsPos[i] = moveTail(knotsPos[i-1], knotsPos[i])
                    }
                    positions.add(knotsPos.last())
                }
            }
            if(dir == "L") {
                for (step in 0 until steps.toInt()) {
                    knotsPos[0] = Position(knotsPos[0].x - 1, knotsPos[0].y)
                    for(i in 1 until knotsPos.size) {
                        knotsPos[i] = moveTail(knotsPos[i-1], knotsPos[i])
                    }
                    positions.add(knotsPos.last())
                }
            }
            if(dir == "U") {
                for (step in 0 until steps.toInt()) {
                    knotsPos[0] = Position(knotsPos[0].x, knotsPos[0].y + 1)
                    for(i in 1 until knotsPos.size) {
                        knotsPos[i] = moveTail(knotsPos[i-1], knotsPos[i])
                    }
                    positions.add(knotsPos.last())
                }
            }
            if(dir == "D") {
                for (step in 0 until steps.toInt()) {
                    knotsPos[0] = Position(knotsPos[0].x, knotsPos[0].y - 1)
                    for(i in 1 until knotsPos.size) {
                        knotsPos[i] = moveTail(knotsPos[i-1], knotsPos[i])
                    }
                    positions.add(knotsPos.last())
                }
            }
        }

        return positions.size
    }

    private fun moveTail(headPos: Position, tailPos: Position): Position {
        if(abs(headPos.x - tailPos.x) <= 1 && abs(headPos.y - tailPos.y) <= 1) return tailPos

        if(headPos.x - tailPos.x == -2 && headPos.y - tailPos.y == 0) return Position(tailPos.x - 1,
            tailPos.y)
        if(headPos.x - tailPos.x == 2 && headPos.y - tailPos.y == 0) return Position(tailPos.x + 1, tailPos.y)
        if(headPos.x - tailPos.x == 0 && headPos.y - tailPos.y == -2) return Position(tailPos.x , tailPos.y - 1)
        if(headPos.x - tailPos.x == 0 && headPos.y - tailPos.y == 2) return Position(tailPos.x , tailPos.y + 1)

        if(headPos.x - tailPos.x == -1 && headPos.y - tailPos.y == 2)
            return Position(tailPos.x - 1, tailPos.y + 1)
        if(headPos.x - tailPos.x == 1 && headPos.y - tailPos.y == 2)
            return Position(tailPos.x + 1, tailPos.y + 1)

        if(headPos.x - tailPos.x == 2 && headPos.y - tailPos.y == 1)
            return Position(tailPos.x + 1, tailPos.y + 1)
        if(headPos.x - tailPos.x == 2 && headPos.y - tailPos.y == -1)
            return Position(tailPos.x + 1, tailPos.y - 1)

        if(headPos.x - tailPos.x == 1 && headPos.y - tailPos.y == -2)
            return Position(tailPos.x + 1, tailPos.y - 1)
        if(headPos.x - tailPos.x == -1 && headPos.y - tailPos.y == -2)
            return Position(tailPos.x - 1, tailPos.y - 1)

        if(headPos.x - tailPos.x == -2 && headPos.y - tailPos.y == -1)
            return Position(tailPos.x - 1, tailPos.y - 1)
        if(headPos.x - tailPos.x == -2 && headPos.y - tailPos.y == 1)
            return Position(tailPos.x - 1, tailPos.y + 1)

        if(headPos.x - tailPos.x == -2 && headPos.y - tailPos.y == 2)
            return Position(tailPos.x - 1, tailPos.y + 1)
        if(headPos.x - tailPos.x == 2 && headPos.y - tailPos.y == 2)
            return Position(tailPos.x + 1, tailPos.y + 1)
        if(headPos.x - tailPos.x == 2 && headPos.y - tailPos.y == -2)
            return Position(tailPos.x + 1, tailPos.y - 1)
        if(headPos.x - tailPos.x == -2 && headPos.y - tailPos.y == -2)
            return Position(tailPos.x - 1, tailPos.y - 1)

        throw IllegalStateException("Shouldn't happen. ($headPos) ($tailPos)")
    }

    data class Position(val x: Int, val y: Int)
}