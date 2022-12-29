import java.util.LinkedList

class Day18 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_18.txt")!!
        val cubes = input.split("\n").map {
            val (x, y, z) = it.split(",")
            Cube(x.toInt(), y.toInt(), z.toInt())
        }

        var count = 0
        cubes.forEach { cur ->
            var cubeCount = 6
            cubes.forEach { other ->
                if(cur.x - 1 == other.x && cur.y == other.y && cur.z == other.z) cubeCount--
                if(cur.x + 1 == other.x && cur.y == other.y && cur.z == other.z) cubeCount--
                if(cur.x == other.x && cur.y - 1 == other.y && cur.z == other.z) cubeCount--
                if(cur.x == other.x && cur.y + 1 == other.y && cur.z == other.z) cubeCount--
                if(cur.x == other.x && cur.y == other.y && cur.z - 1 == other.z) cubeCount--
                if(cur.x == other.x && cur.y == other.y && cur.z + 1 == other.z) cubeCount--
            }
            count += cubeCount
        }

        println(count)
    }

    private fun solve2() {
        val input = readFile("day_18.txt")!!
        val cubes = input.split("\n").map {
            val (x, y, z) = it.split(",")
            Cube(x.toInt(), y.toInt(), z.toInt())
        }

        val graph = Graph()
        cubes.forEach { cur ->
            cubes.forEach { other ->
                if(cur.x - 1 == other.x && cur.y == other.y && cur.z == other.z)
                    cur.hasLeft = true
                if(cur.x + 1 == other.x && cur.y == other.y && cur.z == other.z)
                    cur.hasRight = true
                if(cur.x == other.x && cur.y - 1 == other.y && cur.z == other.z)
                    cur.hasBottom = true
                if(cur.x == other.x && cur.y + 1 == other.y && cur.z == other.z)
                    cur.hasTop = true
                if(cur.x == other.x && cur.y == other.y && cur.z - 1 == other.z)
                    cur.hasBack = true
                if(cur.x == other.x && cur.y == other.y && cur.z + 1 == other.z)
                    cur.hasFront = true

                if(cur.x - 1 == other.x && cur.y - 1 == other.y && cur.z == other.z)
                    cur.hasLeftBottom = true
                if(cur.x - 1 == other.x && cur.y + 1 == other.y && cur.z == other.z)
                    cur.hasLeftTop = true
                if(cur.x + 1 == other.x && cur.y - 1 == other.y && cur.z == other.z)
                    cur.hasRightBottom = true
                if(cur.x + 1 == other.x && cur.y + 1 == other.y && cur.z == other.z)
                    cur.hasRightTop = true
                if(cur.x - 1 == other.x && cur.y == other.y && cur.z - 1 == other.z)
                    cur.hasLeftBack = true
                if(cur.x - 1 == other.x && cur.y == other.y && cur.z + 1 == other.z)
                    cur.hasLeftFront = true
                if(cur.x + 1 == other.x && cur.y == other.y && cur.z - 1 == other.z)
                    cur.hasRightBack = true
                if(cur.x + 1 == other.x && cur.y == other.y && cur.z + 1 == other.z)
                    cur.hasRightFront = true
                if(cur.x == other.x && cur.y - 1 == other.y && cur.z - 1 == other.z)
                    cur.hasBackBottom = true
                if(cur.x == other.x && cur.y - 1 == other.y && cur.z + 1 == other.z)
                    cur.hasFrontBottom = true
                if(cur.x == other.x && cur.y + 1 == other.y && cur.z - 1 == other.z)
                    cur.hasBackTop = true
                if(cur.x == other.x && cur.y + 1 == other.y && cur.z + 1 == other.z)
                    cur.hasFrontTop = true
            }
        }

        cubes.forEach {
            if(!it.hasLeft) {
                if(it.hasLeftTop) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y + 1}_${it.z}_bottom")
                } else if(it.hasTop) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z}_left")
                } else {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_top")
                }

                if(it.hasLeftBottom) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y - 1}_${it.z}_top")
                } else if(it.hasBottom) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z}_left")
                } else {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_bottom")
                }

                if(it.hasLeftBack) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z - 1}_front")
                } else if(it.hasBack) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z - 1}_left")
                } else {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_back")
                }

                if(it.hasLeftFront) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z + 1}_back")
                } else if(it.hasFront) {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z + 1}_left")
                } else {
                    (graph.connections.getOrPut("${it}_left") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_front")
                }
            }

            if(!it.hasRight) {
                if(it.hasRightTop) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y + 1}_${it.z}_bottom")
                } else if(it.hasTop) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z}_right")
                } else {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_top")
                }

                if(it.hasRightBottom) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y - 1}_${it.z}_top")
                } else if(it.hasBottom) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z}_right")
                } else {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_bottom")
                }

                if(it.hasRightBack) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z - 1}_front")
                } else if(it.hasBack) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z - 1}_right")
                } else {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_back")
                }

                if(it.hasRightFront) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z + 1}_back")
                } else if(it.hasFront) {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z + 1}_right")
                } else {
                    (graph.connections.getOrPut("${it}_right") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_front")
                }
            }

            if(!it.hasTop) {
                if(it.hasLeftTop) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y + 1}_${it.z}_right")
                } else if(it.hasLeft) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z}_top")
                } else {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_left")
                }

                if(it.hasRightTop) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y + 1}_${it.z}_left")
                } else if(it.hasRight) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z}_top")
                } else {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_right")
                }

                if(it.hasBackTop) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z - 1}_front")
                } else if(it.hasBack) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z - 1}_top")
                } else {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_back")
                }

                if(it.hasFrontTop) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z + 1}_back")
                } else if(it.hasFront) {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z + 1}_top")
                } else {
                    (graph.connections.getOrPut("${it}_top") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_front")
                }
            }

            if(!it.hasBottom) {
                if(it.hasLeftBottom) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y - 1}_${it.z}_right")
                } else if(it.hasLeft) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z}_bottom")
                } else {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_left")
                }

                if(it.hasRightBottom) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y - 1}_${it.z}_left")
                } else if(it.hasRight) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z}_bottom")
                } else {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_right")
                }

                if(it.hasBackBottom) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z - 1}_front")
                } else if(it.hasBack) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z - 1}_bottom")
                } else {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_back")
                }

                if(it.hasFrontBottom) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z + 1}_back")
                } else if(it.hasFront) {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z + 1}_bottom")
                } else {
                    (graph.connections.getOrPut("${it}_bottom") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_front")
                }
            }

            if(!it.hasBack) {
                if(it.hasLeftBack) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z - 1}_right")
                } else if(it.hasLeft) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z}_back")
                } else {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_left")
                }

                if(it.hasRightBack) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z - 1}_left")
                } else if(it.hasRight) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z}_back")
                } else {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_right")
                }

                if(it.hasBackBottom) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z - 1}_top")
                } else if(it.hasBottom) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z}_back")
                } else {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_bottom")
                }

                if(it.hasBackTop) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z - 1}_bottom")
                } else if(it.hasTop) {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z}_back")
                } else {
                    (graph.connections.getOrPut("${it}_back") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_top")
                }
            }

            if(!it.hasFront) {
                if(it.hasLeftFront) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z + 1}_right")
                } else if(it.hasLeft) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x - 1}_${it.y}_${it.z}_front")
                } else {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_left")
                }

                if(it.hasRightFront) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z + 1}_left")
                } else if(it.hasRight) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x + 1}_${it.y}_${it.z}_front")
                } else {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_right")
                }

                if(it.hasFrontBottom) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z + 1}_top")
                } else if(it.hasBottom) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y - 1}_${it.z}_front")
                } else {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_bottom")
                }

                if(it.hasFrontTop) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z + 1}_bottom")
                } else if(it.hasTop) {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y + 1}_${it.z}_front")
                } else {
                    (graph.connections.getOrPut("${it}_front") { mutableSetOf() })
                        .add("${it.x}_${it.y}_${it.z}_top")
                }
            }
        }

        val leftMostCube = getLeftMostCube(cubes)
        val result = sideCounter(graph, leftMostCube)
        println(result)
    }

    private fun getLeftMostCube(cubes: List<Cube>): Cube {
        return cubes.minByOrNull { it.x }!!
    }

    private fun sideCounter(graph: Graph, start: Cube): Int {
        val marked = mutableSetOf<String>()
        val bfs = LinkedList<String>().apply { addLast("${start}_left") }
        while (bfs.isNotEmpty()) {
            val cur = bfs.pollFirst()
            if(marked.contains(cur)) continue

            bfs.addAll(graph.connections[cur]!!)

            marked.add(cur)
        }

        return marked.size
    }

    private data class Cube(val x: Int, val y: Int, val z: Int) {
        var hasLeft = false
        var hasRight = false
        var hasFront = false
        var hasBack = false
        var hasTop = false
        var hasBottom = false
        var hasLeftTop = false
        var hasLeftBottom = false
        var hasRightTop = false
        var hasRightBottom = false
        var hasFrontTop = false
        var hasFrontBottom = false
        var hasBackTop = false
        var hasBackBottom = false
        var hasLeftFront = false
        var hasLeftBack = false
        var hasRightFront = false
        var hasRightBack = false

        override fun toString(): String {
            return "${x}_${y}_${z}"
        }
    }

    private class Graph {
        val connections: MutableMap<String, MutableSet<String>> = mutableMapOf()
    }
}