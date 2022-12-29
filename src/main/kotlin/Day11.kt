import java.util.LinkedList

class Day11 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_11.txt")!!.split("\n").chunked(7)
        val monkeys = input.map { parseMonkey(it) }

        repeat(20) {
            monkeys.forEach { it.examineAndTest(monkeys) { it / 3 } }
        }
        val result = monkeys.sortedByDescending { it.inspectCount }
            .take(2)
            .map { it.inspectCount }
            .reduce { o1, o2 -> o1 * o2 }

        println(result)
    }

    private fun solve2() {
        val input = readFile("day_11.txt")!!.split("\n").chunked(7)
        val monkeys = input.map { parseMonkey(it) }
        val worryDivider = monkeys.map { it.testValue }.reduce { o1, o2 -> o1 * o2 }

        repeat(10000) {
            monkeys.forEach { it.examineAndTest(monkeys) { it % worryDivider } }
        }
        val result = monkeys.sortedByDescending { it.inspectCount }
            .take(2)
            .map { it.inspectCount }
            .reduce { o1, o2 -> o1 * o2 }

        println(result)
    }

    private fun parseMonkey(data: List<String>): Monkey {
        val items = LinkedList(data[1].substringAfter("Starting items: ")
            .split(",")
            .map { it.trim().toLong() })

        val operation = data[2].substringAfter("Operation: new = ").let {
            val operationTokens = it.split(" ")
            if(operationTokens[1] == "*") {
                if(operationTokens[2] == "old") {
                    { i: Long -> i * i }
                } else {
                    { i: Long -> i * operationTokens[2].toLong() }
                }
            } else {
                if(operationTokens[2] == "old") {
                    { i: Long -> i + i }
                } else {
                    { i: Long -> i + operationTokens[2].toLong() }
                }
            }
        }

        val test = { i: Long ->
            if(i % data[3].split(" ").last().toLong() == 0L) {
                data[4].split(" ").last().toInt()
            } else {
                data[5].split(" ").last().toInt()
            }
        }

        return Monkey(items, operation, test, data[3].split(" ").last().toLong())
    }

    class Monkey(
        private val items: LinkedList<Long>,
        private val operation: (i: Long) -> Long,
        private val test: (i: Long) -> Int,
        val testValue: Long,
        var inspectCount: Long = 0) {

        fun examineAndTest(monkeys: List<Monkey>, worryFun: (Long) -> Long) {
            while (items.isNotEmpty()) {
                val item = items.pollFirst()
                val worryLevel =  worryFun(operation(item))
                monkeys[test(worryLevel)].items.addLast(worryLevel)
                inspectCount++
            }
        }
    }
}