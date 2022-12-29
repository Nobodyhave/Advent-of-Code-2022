class Day3 {
    fun solve1() {
        val input = readFile("day_3.txt")!!
        val split = input.split("\n")

        val result = split
            .asSequence()
            .map { items ->
                items.take(items.length / 2)
                    .toSet()
                    .intersect(items.takeLast(items.length / 2).toSet())
                    .first() }
            .map { if(it.isUpperCase()) it - 'A' + 27 else it - 'a' + 1}
            .sum()

        println(result)
    }

    fun solve2() {
        val input = readFile("day_3.txt")!!
        val split = input.split("\n")

        val result = split
            .chunked(3)
            .map { sacks ->
                sacks.map { it.toSet() }
                    .reduce { s1, s2 -> s1.intersect(s2) }
                    .toList()
                    .first()
            }.sumOf { if (it.isUpperCase()) it - 'A' + 27 else it - 'a' + 1 }

        println(result)
    }
}