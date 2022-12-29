class Day6 {
    fun solve(){
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_6.txt")!!

        var start = 0
        var end = 0
        val counts = mutableMapOf<Char, Int?>()

        while (end < 4) {
            val count = counts[input[end]]
            if(count == null) counts[input[end]] = 1 else counts[input[end]] = count + 1
            end++
        }

        while (end < input.length) {
            if(counts.size == 4 && counts.values.all { it == 1 }) {
                println(end)
                break
            }

            var count = counts[input[end]]
            if(count == null) counts[input[end]] = 1 else counts[input[end]] = count + 1
            end++

            count = counts[input[start]]
            if(count == 1) counts.remove(input[start])
            else counts[input[start]] = count?.minus(1)
            start++
        }
    }

    private fun solve2() {
        val input = readFile("day_6.txt")!!

        var start = 0
        var end = 0
        val counts = mutableMapOf<Char, Int?>()

        while (end < 14) {
            val count = counts[input[end]]
            if(count == null) counts[input[end]] = 1 else counts[input[end]] = count + 1
            end++
        }

        while (end < input.length) {
            if(counts.size == 14 && counts.values.all { it == 1 }) {
                println(end)
                break
            }

            var count = counts[input[end]]
            if(count == null) counts[input[end]] = 1 else counts[input[end]] = count + 1
            end++

            count = counts[input[start]]
            if(count == 1) counts.remove(input[start])
            else counts[input[start]] = count?.minus(1)
            start++
        }
    }
}