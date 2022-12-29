class Day10 {
    fun solve(){
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_10.txt")!!.split("\n")
        val cycles = setOf(20, 60, 100, 140, 180, 220)
        var cycle = 1
        var register = 1
        var result = 0
        input.forEach {
            val split = it.split(" ")
            if(cycles.contains(cycle)) {
                result += cycle * register
            }
            if(split[0] == "noop") {
                cycle++
            } else {
                cycle++
                if(cycles.contains(cycle)) {
                    result += cycle * register
                }
                register += split[1].toInt()
                cycle++
            }
        }

        println(result)
    }

    private fun solve2() {
        val input = readFile("day_10.txt")!!.split("\n")
        var cycle = 1
        var register = 1
        val output = StringBuilder()
        input.forEach {
            val split = it.split(" ")
            if(cycle % 40 in (register .. register + 2)) {
                output.append('#')
            } else {
                output.append('.')
            }
            if(split[0] == "noop") {
                cycle++
            } else {
                cycle++
                if(cycle % 40 in (register .. register + 2)) {
                    output.append('#')
                } else {
                    output.append('.')
                }
                register += split[1].toInt()
                cycle++
            }
        }

        output.chunked(40).forEach { println(it) }
    }
}