import java.util.LinkedList

class Day5 {
    fun solve(){
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_5.txt")!!
        val split = input.split("\n")

        val width = 9
        val height = 8
        val chunk = 4

        val stacks = Array(width) { LinkedList<Char>() }
        split
            .take(height)
            .map { it + " ".repeat(width * chunk - it.length) }
            .map { it.chunked(chunk)
                .map { s-> s.trim() }
                .withIndex()
                .forEach { if(it.value.isNotBlank()) stacks[it.index].addFirst(it.value[1]) }
            }

        input
            .split("\n")
            .drop(height + 2)
            .map { it.split(" ") }
            .map { listOf(it[1].toInt(), it[3].toInt(), it[5].toInt()) }
            .forEach {(count, from, to) ->
                repeat(count) {
                    stacks[to - 1].addLast(stacks[from - 1].pollLast())
                }
            }

        val result = stacks.fold(StringBuilder()) { acc, chars ->
            acc.apply { if(chars.isNotEmpty()) append(chars.peekLast()) } }

        println(result)
    }

    private fun solve2() {
        val input = readFile("day_5.txt")!!
        val split = input.split("\n")

        val width = 9
        val height = 8
        val chunk = 4

        val stacks = Array(width) { LinkedList<Char>() }
        split
            .take(height)
            .map { it + " ".repeat(width * chunk - it.length) }
            .map { it.chunked(chunk)
                .map { s-> s.trim() }
                .withIndex()
                .forEach { if(it.value.isNotBlank()) stacks[it.index].addFirst(it.value[1]) }
            }

        input
            .split("\n")
            .drop(height + 2)
            .map { it.split(" ") }
            .map { listOf(it[1].toInt(), it[3].toInt(), it[5].toInt()) }
            .forEach {(count, from, to) ->
                val tempList = LinkedList<Char>()
                repeat(count) {
                    tempList.addLast(stacks[from - 1].pollLast())
                }
                repeat(count) {
                    stacks[to - 1].addLast(tempList.pollLast())
                }
            }

        val result = stacks.fold(StringBuilder()) { acc, chars ->
            acc.apply { if(chars.isNotEmpty()) append(chars.peekLast()) } }

        println(result)
    }
}