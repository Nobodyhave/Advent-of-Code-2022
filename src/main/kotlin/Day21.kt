class Day21 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_21.txt")!!.split("\n")
        val monkeys = getMonkeys(input)
        val root = connectMonkeys(monkeys)

        val result = root.yell()
        println(result)
    }

    private fun solve2() {
        val input = readFile("day_21.txt")!!.split("\n")
        val monkeys = getMonkeys(input)
        val root = connectMonkeys(monkeys)

        val pathToHuman = mutableSetOf<Monkey>()
        pathToHuman(root, pathToHuman)

        val result = calculateHuman(root, pathToHuman, null)
        println(result)
    }

    private fun getMonkeys(input: List<String>): Map<String, Monkey> {
        return input.map {
            val name = it.substringBefore(":")
            val rest = it.substringAfter(": ")

            var operationType: String? = null
            var operation: ((Long, Long) -> Long)? = null
            var reverseOperation: ((Long, Long) -> Long)? = null
            var number: Long? = null
            var left: Monkey? = null
            var right: Monkey? = null
            if (rest.contains("-")) {
                operationType = "-"
                operation = { a, b -> a - b }
                reverseOperation = { a, b -> a + b }
                left = Monkey(rest.substringBefore(" -"))
                right = Monkey(rest.substringAfter("- "))
            } else if (rest.contains("+")) {
                operationType = "+"
                operation = { a, b -> a + b }
                reverseOperation = { a, b -> a - b }
                left = Monkey(rest.substringBefore(" +"))
                right = Monkey(rest.substringAfter("+ "))
            } else if (rest.contains("/")) {
                operationType = "/"
                operation = { a, b -> a / b }
                reverseOperation = { a, b -> a * b }
                left = Monkey(rest.substringBefore(" /"))
                right = Monkey(rest.substringAfter("/ "))
            } else if (rest.contains("*")) {
                operationType = "*"
                operation = { a, b -> a * b }
                reverseOperation = { a, b -> a / b }
                left = Monkey(rest.substringBefore(" *"))
                right = Monkey(rest.substringAfter("* "))
            } else {
                number = rest.toLong()
            }

            Monkey(name, number, operationType, operation, reverseOperation, left, right)
        }.associateBy {
            it.name
        }
    }

    private fun connectMonkeys(monkeys: Map<String, Monkey>): Monkey {
        monkeys.forEach { (_, monkey) ->
            val left = monkeys[monkey.left?.name]
            val right = monkeys[monkey.right?.name]
            monkey.left = left
            monkey.right = right
        }

        return monkeys["root"]!!
    }

    private fun pathToHuman(monkey: Monkey?, path: MutableSet<Monkey>) {
        if (monkey == null) return
        if (monkey.name == "humn") {
            path.add(monkey)
            return
        }

        pathToHuman(monkey.left, path)
        pathToHuman(monkey.right, path)

        if (path.isNotEmpty()) path.add(monkey)
    }

    private fun calculateHuman(monkey: Monkey?, path: MutableSet<Monkey>, num: Long?): Long? {
        if (monkey == null) return null
        if (monkey.name == "humn") {
            return num
        }
        if (monkey.number != null) return monkey.number

        return if (path.contains(monkey.left)) {
            calculateHuman(
                monkey.left,
                path,
                if (num != null)
                    monkey.reverseOperation?.invoke(num, monkey.right!!.yell())
                else
                    monkey.right!!.yell()
            )
        } else {
            when (monkey.operationType) {
                "-" -> calculateHuman(
                    monkey.right,
                    path,
                    if (num != null)
                        monkey.operation?.invoke(monkey.left!!.yell(), num)
                    else
                        monkey.left!!.yell()
                )
                "+" -> calculateHuman(
                    monkey.right,
                    path,
                    if (num != null)
                        monkey.reverseOperation?.invoke(num, monkey.left!!.yell())
                    else
                        monkey.left!!.yell()
                )
                "/" -> calculateHuman(
                    monkey.right,
                    path,
                    if (num != null)
                        monkey.operation?.invoke(monkey.left!!.yell(), num)
                    else
                        monkey.left!!.yell()
                )
                "*" -> calculateHuman(
                    monkey.right,
                    path,
                    if (num != null)
                        monkey.reverseOperation?.invoke(num, monkey.left!!.yell())
                    else
                        monkey.left!!.yell()
                )
                else -> throw java.lang.IllegalArgumentException("Unsupported operation type")
            }
        }
    }

    private class Monkey(
        val name: String,
        val number: Long? = null,
        val operationType: String? = null,
        val operation: ((Long, Long) -> Long)? = null,
        val reverseOperation: ((Long, Long) -> Long)? = null,
        var left: Monkey? = null,
        var right: Monkey? = null,
    ) {
        fun yell(): Long {
            return number ?: return operation!!.invoke(left!!.yell(), right!!.yell())
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Monkey

            if (name != other.name) return false

            return true
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }
    }
}