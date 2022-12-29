class Day1 {
    fun solve1() {
        val input = readFile("day_1.txt")!!
        val split = input.split("\n\n")

        val result = split
            .map { it.split("\n").map { it.toInt() } }
            .maxOfOrNull { it.sum() }

        println(result)
    }

    fun solve2() {
        val input = readFile("day_1.txt")!!
        val split = input.split("\n\n")

        val result = split
            .asSequence()
            .map { it.split("\n").map { it.toInt() } }
            .map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()

        println(result)
    }
}