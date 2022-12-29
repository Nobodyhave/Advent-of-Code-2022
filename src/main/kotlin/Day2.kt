class Day2 {
    fun solve1() {
        val input = readFile("day_2.txt")!!
        val split = input.split("\n")

        val result = split
            .asSequence()
            .map { getScores1(it) }
            .sum()

        println(result)
    }

    fun solve2() {
        val input = readFile("day_2.txt")!!
        val split = input.split("\n")

        val result = split
            .asSequence()
            .map { getScores2(it) }
            .sum()

        println(result)
    }

    private fun getScores1(round: String): Int {
        return when(round) {
            "A X" -> 1 + 3
            "A Y" -> 2 + 6
            "A Z" -> 3 + 0
            "B X" -> 1 + 0
            "B Y" -> 2 + 3
            "B Z" -> 3 + 6
            "C X" -> 1 + 6
            "C Y" -> 2 + 0
            "C Z" -> 3 + 3
            else -> throw IllegalArgumentException("Wrong input: $round")
        }
    }

    private fun getScores2(round: String): Int {
        return when(round) {
            "A X" -> 3 + 0
            "A Y" -> 1 + 3
            "A Z" -> 2 + 6
            "B X" -> 1 + 0
            "B Y" -> 2 + 3
            "B Z" -> 3 + 6
            "C X" -> 2 + 0
            "C Y" -> 3 + 3
            "C Z" -> 1 + 6
            else -> throw IllegalArgumentException("Wrong input: $round")
        }
    }
}