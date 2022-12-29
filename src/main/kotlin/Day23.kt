class Day23 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_23.txt")!!.split("\n")
        val elfs = getElfsPositions(input)

        val result = spread(elfs.toSet(), 10)
        println(result)
    }

    private fun solve2() {
        val input = readFile("day_23.txt")!!.split("\n")
        val elfs = getElfsPositions(input)

        val result = spread(elfs.toSet(), Int.MAX_VALUE)
        println(result)
    }

    private fun getElfsPositions(input: List<String>): List<Position> {
        val elfs = mutableListOf<Position>()
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (c == '#') elfs.add(Position(i, j))
            }
        }

        return elfs
    }

    private fun proposePositions(startPositions: Set<Position>, dir: Direction): Map<Position,
            List<Position>> {
        val map = mutableMapOf<Position, MutableList<Position>>()
        startPositions.forEach {
            val isLeftFree = isLeftFree(it, startPositions)
            val isRightFree = isRightFree(it, startPositions)
            val isTopFree = isTopFree(it, startPositions)
            val isBottomFree = isBottomFree(it, startPositions)

            if (!isLeftFree || !isRightFree || !isTopFree || !isBottomFree) {
                dirLoop@
                for (i in 0 until 4) {
                    when (Direction.values()[(dir.ordinal + i) % 4]) {
                        Direction.NORTH -> {
                            if (isTopFree) {
                                map.getOrPut(Position(it.row - 1, it.col)) { mutableListOf() }
                                    .add(it)
                                break@dirLoop
                            }
                        }
                        Direction.SOUTH -> {
                            if (isBottomFree) {
                                map.getOrPut(Position(it.row + 1, it.col)) { mutableListOf() }
                                    .add(it)
                                break@dirLoop
                            }
                        }
                        Direction.WEST -> {
                            if (isLeftFree) {
                                map.getOrPut(Position(it.row, it.col - 1)) { mutableListOf() }
                                    .add(it)
                                break@dirLoop
                            }
                        }
                        Direction.EAST -> {
                            if (isRightFree) {
                                map.getOrPut(Position(it.row, it.col + 1)) { mutableListOf() }
                                    .add(it)
                                break@dirLoop
                            }
                        }
                    }
                }
            }
        }

        return map
    }

    private fun isLeftFree(pos: Position, startPositions: Set<Position>): Boolean {
        return !startPositions.contains(Position(pos.row - 1, pos.col - 1)) &&
                !startPositions.contains(Position(pos.row, pos.col - 1)) &&
                !startPositions.contains(Position(pos.row + 1, pos.col - 1))
    }

    private fun isRightFree(pos: Position, startPositions: Set<Position>): Boolean {
        return !startPositions.contains(Position(pos.row - 1, pos.col + 1)) &&
                !startPositions.contains(Position(pos.row, pos.col + 1)) &&
                !startPositions.contains(Position(pos.row + 1, pos.col + 1))
    }

    private fun isTopFree(pos: Position, startPositions: Set<Position>): Boolean {
        return !startPositions.contains(Position(pos.row - 1, pos.col - 1)) &&
                !startPositions.contains(Position(pos.row - 1, pos.col)) &&
                !startPositions.contains(Position(pos.row - 1, pos.col + 1))
    }

    private fun isBottomFree(pos: Position, startPositions: Set<Position>): Boolean {
        return !startPositions.contains(Position(pos.row + 1, pos.col - 1)) &&
                !startPositions.contains(Position(pos.row + 1, pos.col)) &&
                !startPositions.contains(Position(pos.row + 1, pos.col + 1))
    }

    private fun move(
        oldPositions: Set<Position>,
        map: Map<Position, List<Position>>,
    ): Set<Position> {
        val newPositions = mutableSetOf<Position>().apply { addAll(oldPositions) }
        map.forEach { (pos, proposals) ->
            if (proposals.size == 1) {
                newPositions.remove(proposals[0])
                newPositions.add(pos)
            }
        }

        return newPositions
    }

    private fun spread(startPositions: Set<Position>, turns: Int): Int {
        val directions = Direction.values()
        var curPositions = startPositions
        for (i in 0 until turns) {
            val proposals = proposePositions(curPositions, directions[i % directions.size])
            if(proposals.isNotEmpty()) {
                curPositions = move(curPositions, proposals)
            } else {
                println("No moves on turn: ${i + 1}")
                break
            }
        }

        return countEmptySpace(curPositions)
    }

    private fun countEmptySpace(positions: Set<Position>): Int {
        val left = positions.minOf { it.col }
        val right = positions.maxOf { it.col }
        val top = positions.minOf { it.row }
        val bottom = positions.maxOf { it.row }

        var count = 0
        for (i in top..bottom) {
            for (j in left..right) {
                if (!positions.contains(Position(i, j))) count++
            }
        }

        return count
    }

    data class Position(val row: Int, val col: Int)

    enum class Direction {
        NORTH, SOUTH, WEST, EAST
    }
}