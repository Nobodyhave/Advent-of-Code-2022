import kotlin.math.max

class Day16 {
    fun solve() {
        solve1()
        solve2()
    }

    val INF = 9999

    private fun solve1() {
        val input = readFile("day_16.txt")!!.split("\n")
        val nodes = prepareNodes(input)
        val map: Map<String, Int> = nodes.mapIndexed { index, node -> Pair(node.name, index) }
            .toMap()
        val dist: Array<IntArray> = Array(nodes.size) { IntArray(nodes.size) { INF } }

        nodes.forEach { node ->
            node.adj.forEach { adj ->
                dist[map[node.name]!!][map[node.name]!!] = 0
                dist[map[node.name]!!][map[adj]!!] = 1
                dist[map[adj]!!][map[node.name]!!] = 1
            }
        }
        floydWarshall(nodes.size, dist)

        val flowNodes = nodes.filter { it.flow != 0 }

        println(calculateFlow(nodes[0], flowNodes, dist, map))
    }

    // Takes VEEEEEEERY long time to calculate
    private fun solve2() {
        val input = readFile("day_16.txt")!!.split("\n")
        val nodes = prepareNodes(input)
        val map: Map<String, Int> = nodes.mapIndexed { index, node -> Pair(node.name, index) }
            .toMap()
        val dist: Array<IntArray> = Array(nodes.size) { IntArray(nodes.size) { INF } }

        nodes.forEach { node ->
            node.adj.forEach { adj ->
                dist[map[node.name]!!][map[node.name]!!] = 0
                dist[map[node.name]!!][map[adj]!!] = 1
                dist[map[adj]!!][map[node.name]!!] = 1
            }
        }
        floydWarshall(nodes.size, dist)

        val flowNodes = nodes.filter { it.flow != 0 }

        println(calculateFlowWithElephant(nodes[0], nodes[0], flowNodes, dist, map))
    }

    private fun prepareNodes(input: List<String>): List<Node> {
        return input.map {
            val name = it.substringAfter("Valve ").substringBefore(" has flow")
            val adj = if(it.substringAfter(" to valves ", "-1") != "-1") {
                it.substringAfter(" to " + "valves ").split(", ")
            } else {
                it.substringAfter(" to " + "valve ").split(", ")
            }
            val flow = it.substringAfter("flow rate=").substringBefore("; tunnel")
            Node(name, adj, flow.toInt())
        }.sortedBy { it.name }
    }

    private fun calculateFlow(start: Node, nodes: List<Node>, dist: Array<IntArray>, map:
    Map<String, Int>): Int {
        val maxPath =  calculateFlow(start, nodes, BooleanArray(nodes.size) { false }, dist, map,
            30, "AA")

        return maxPath
    }

    private fun calculateFlowWithElephant(
        startH: Node, startE: Node, nodes: List<Node>, dist: Array<IntArray>,
                                          map: Map<String, Int>): Int {
        val maxPath =  calculateFlowWithElephant(startH, startE, nodes, BooleanArray(nodes.size) { false },
            dist, map,
            26, 26, "AA", "AA")

        return maxPath
    }

    private fun calculateFlow(
        start: Node,
        nodes: List<Node>,
        visited: BooleanArray,
        dist: Array<IntArray>,
        map: Map<String, Int>,
        minutes: Int,
        path: String): Int {
        if(minutes <= 1) return 0

        var maxFlow = 0
        for(i in nodes.indices) {
            if(visited[i]) continue
            val next = nodes[i]
            visited[i] = true
            val flow = calculateFlow(next, nodes, visited, dist, map,
                (if(start.flow != 0) minutes - 1 else minutes) - dist[map[start.name]!!][map[next
                    .name]!!], path + " " + next.name)

            maxFlow = max(maxFlow, flow)

            visited[i] = false
        }

        return maxFlow + (minutes - 1) * start.flow
    }

    private fun calculateFlowWithElephant(
        startH: Node,
        startE: Node,
        nodes: List<Node>,
        visited: BooleanArray,
        dist: Array<IntArray>,
        map: Map<String, Int>,
        minutesH: Int,
        minutesE: Int,
        pathH: String,
        pathE: String): Int {
        if(minutesH <= 1 && minutesE <= 1)
            return 0
        else if(minutesH <= 1)
            return calculateFlow(startE, nodes, visited, dist, map, minutesE, pathE)
        else if(minutesE <= 1)
            return calculateFlow(startH, nodes, visited, dist, map, minutesH, pathH)
        else {
            var maxFlow = 0
            for (i in nodes.indices) {
                if (visited[i]) continue
                val next = nodes[i]
                visited[i] = true
                if (visited.all { it }) {
                    val flowH = calculateFlow(
                        next, nodes, visited, dist, map,
                        (if (startH.flow != 0) minutesH - 1 else minutesH) - dist[map[startH
                            .name]!!][map[next
                            .name]!!], pathH + " " + next.name
                    )
                    val flowE = calculateFlow(
                        next, nodes, visited, dist, map,
                        (if (startE.flow != 0) minutesE - 1 else minutesE) - dist[map[startE
                            .name]!!][map[next
                            .name]!!], pathE + " " + next.name
                    )
                    maxFlow = max(maxFlow, max(flowE, flowH))
                } else {
                    for (j in nodes.indices) {
                        if (visited[j]) continue
                        val nextE = nodes[j]
                        visited[j] = true
                        val flow = calculateFlowWithElephant(
                            next, nextE, nodes, visited, dist, map,
                            (if (startH.flow != 0) minutesH - 1 else minutesH) - dist[map[startH
                                .name]!!][map[next.name]!!],
                            (if (startE.flow != 0) minutesE - 1 else minutesE) - dist[map[startE
                                .name]!!][map[nextE.name]!!],
                            pathH + " " + next.name,
                            pathE + " " + nextE.name,
                        )

                        maxFlow = max(maxFlow, flow)
                        visited[j] = false
                    }
                }
                visited[i] = false
            }

            return maxFlow + (minutesH - 1) * startH.flow + (minutesE - 1) * startE.flow
        }
    }

    fun floydWarshall(size: Int, dist: Array<IntArray>) {
        var i: Int
        var j: Int

        var k = 0
        while (k < size) {
            i = 0
            while (i < size) {
                j = 0
                while (j < size) {
                    if (dist[i][k] + dist[k][j]
                        < dist[i][j]
                    ) dist[i][j] = dist[i][k] + dist[k][j]
                    j++
                }
                i++
            }
            k++
        }
    }

    data class Node(val name: String, val adj: List<String>, val flow: Int)
}