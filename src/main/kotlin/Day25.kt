import kotlin.math.pow

class Day25 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_25.txt")!!.split("\n")

        var sum = 0L
        input.forEach {
            sum += snafuToDecimal(it)
        }

        println(pentalToSnafu(decimalToPental(sum)))
    }

    private fun solve2() {
        // No second part, star is given for solving other 49 problems
    }

    private fun snafuToDecimal(snafu: String): Long {
        var decimal = 0L
        snafu.forEachIndexed { i, c ->
            decimal += when(c) {
                '2' -> 2 * 5.0.pow((snafu.lastIndex - i).toDouble()).toLong()
                '1' -> 1 * 5.0.pow((snafu.lastIndex - i).toDouble()).toLong()
                '0' -> 0
                '-' -> -1 * 5.0.pow((snafu.lastIndex - i).toDouble()).toLong()
                '=' -> -2 * 5.0.pow((snafu.lastIndex - i).toDouble()).toLong()
                else -> throw java.lang.IllegalArgumentException("Unsupported SNAFU number")
            }
        }

        return decimal
    }

    private fun decimalToPental(decimal: Long): String {
        var dec = decimal
        var power = 0
        while (dec / 5.0.pow(power).toLong() > 0) power++
        power--

        val pental = StringBuilder()
        while (power >= 0) {
            val div = (dec / 5.0.pow(power)).toLong()
            pental.append(div)
            dec -= div * 5.0.pow(power).toLong()
            power--
        }

        return pental.toString()
    }

    private fun pentalToSnafu(pental: String): String {
        val snafu = StringBuilder()

        var carry = false
        for (i in pental.lastIndex downTo 0) {
            when(pental[i]) {
                '4'-> {
                    if(carry) {
                        snafu.append('0')
                    } else {
                        snafu.append('-')
                    }
                    carry = true
                }
                '3'-> {
                    if(carry) {
                        snafu.append('-')
                    } else {
                        snafu.append('=')
                    }
                    carry = true
                }
                '2'-> {
                    if(carry) {
                        snafu.append('=')
                        carry = true
                    } else {
                        snafu.append('2')
                        carry = false
                    }
                }
                '1'-> {
                    if(carry) {
                        snafu.append('2')
                    } else {
                        snafu.append('1')
                    }
                    carry = false
                }
                '0'-> {
                    if(carry) {
                        snafu.append('1')
                    } else {
                        snafu.append('0')
                    }
                    carry = false
                }
            }
        }

        if(carry) {
            snafu.append('1')
        }

        return snafu.reverse().toString()
    }
}