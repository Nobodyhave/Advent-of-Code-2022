class Day4 {
    fun solve1() {
        val input = readFile("day_4.txt")!!
        val split = input.split("\n")

        val result = split
            .asSequence()
            .map { it.split(",") }
            .flatten()
            .map { it.split("-").map { id -> id.toInt() } }
            .flatten()
            .chunked(4)
            .map { checkOverlap1(it) }
            .sum()

        println(result)
    }

    fun solve2() {
        val input = readFile("day_4.txt")!!
        val split = input.split("\n")

        val result = split
            .asSequence()
            .map { it.split(",") }
            .flatten()
            .map { it.split("-").map { id -> id.toInt() } }
            .flatten()
            .chunked(4)
            .map { checkOverlap2(it) }
            .sum()

        println(result)
    }

    private fun checkOverlap1(ids: List<Int>): Int {
        val first = ids[0]..ids[1]
        val second = ids[2]..ids[3]

        return if(first.contains(second.first) && first.contains(second.last) || second.contains
                (first.first) && second.contains(first.last)) 1 else 0
    }

    private fun checkOverlap2(ids: List<Int>): Int {
        val first = ids[0]..ids[1]
        val second = ids[2]..ids[3]

        return if(first.last < second.first || first.first > second.last) 0 else 1
    }
}