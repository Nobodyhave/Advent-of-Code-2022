class Day13 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_13.txt")!!.split("\n\n")
        val result = input.mapIndexed { index, s ->
            val (p1, p2) = s.split("\n")
            (if(compare(p1, p2) <= 0) index + 1 else 0).also { println("Pair $index: $it") }
        }.sum()

        println(result)
    }

    private fun solve2() {
        val input = readFile("day_13.txt")!!.split("\n\n").map { it.split("\n") }.flatten()
        val packets = (input + listOf("[[2]]", "[[6]]")).sortedWith { o1, o2 ->
            compare(o1, o2)
        }
        val i1 = packets.indexOf("[[2]]")
        val i2 = packets.indexOf("[[6]]")

        println( (i1 + 1) * (i2 + 1) )
    }

    private fun compare(s1: String, s2: String): Int {
        println("Compare $s1 and $s2")
        val p1: List<String>
        val p2: List<String>

        if(s1.startsWith("[") && s2.startsWith("[")) {
            p1 = tokenize(s1)
            p2 = tokenize(s2)
        } else if(s1.startsWith("[")) {
            p1 = tokenize(s1)
            p2 = tokenize("[$s2]")
        } else if(s2.startsWith("[")) {
            p1 = tokenize("[$s1]")
            p2 = tokenize(s2)
        } else {
            return s1.toInt().compareTo(s2.toInt())
        }

        var i = 0
        var j = 0
        while (i < p1.size && j < p2.size) {
            val r = compare(p1[i], p2[j])
            if(r == 0) {
                i++
                j++
                continue
            } else if(r > 0) {
                return 1
            } else {
                return -1
            }
        }

        return if(i == p1.size && j == p2.size) {
            0
        } else if(i == p1.size) {
            -1
        } else {
            1
        }
    }

    private fun tokenize(s: String): List<String> {
        val result = mutableListOf<String>()
        var i = 1
        var openCount = 0
        var start = 1
        while (i < s.length - 1) {
            if(s[i] == ',') {
                if(openCount == 0) {
                    if(i > start) result.add(s.substring(start, i))
                    start = i + 1
                }
            } else if(s[i] == '[') {
                openCount++
            } else if(s[i] == ']') {
                openCount--
            }
            i++
        }
        if(s.length - 1 > start) result.add(s.substring(start, s.length - 1))

        return result
    }
}