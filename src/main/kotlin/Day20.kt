import kotlin.math.abs

class Day20 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_20.txt")!!.split("\n")
        val list = CircularList(input.map { it.toInt() })
        var array = input.mapIndexed { index, s -> (Pair(index, s.toLong())) }.toTypedArray()

        for (i in 0 until list.pointers.size) {
            array = move(array, i)
        }

        val result = getCoordinates(array)
        println(result)
    }

    private fun solve2() {
        val input = readFile("day_20.txt")!!.split("\n")
        val list = CircularList(input.map { it.toInt() })
        var array = input.mapIndexed { index, s -> (Pair(index, s.toLong() * 811589153L)) }
            .toTypedArray()

        repeat(10) {
            for (i in 0 until list.pointers.size) {
                array = move(array, i)
            }
        }

        val result = getCoordinates(array)
        println(result)
    }

    private fun move(array: Array<Pair<Int, Long>>, index: Int): Array<Pair<Int, Long>> {
        var curIndex = 0
        for (i in array.indices) {
            if (array[i].first == index) {
                curIndex = i
                break
            }
        }

        val newArray = array.copyOf()
        val size = array.size - 1
        val dist = if(array[curIndex].second >= 0) {
            (array[curIndex].second % size).toInt()
        } else {
            ((size - abs(array[curIndex].second) % size) % size).toInt()
        }

        if(curIndex + dist <= array.lastIndex) {
            val elementToMove = array[curIndex]
            try {
                array.copyInto(newArray, curIndex, curIndex + 1, curIndex + dist + 1)
            } catch (e: ArrayIndexOutOfBoundsException) {
                println("curIndex: $curIndex curIndex+1: ${curIndex+1} curIndex + dist + 1: ${curIndex + dist + 1}")
            }

            newArray[curIndex + dist] = elementToMove
        } else {
            val elementToMove = array[curIndex]
            val targetIndex = dist - (array.lastIndex - curIndex)
            array.copyInto(newArray, targetIndex + 1, targetIndex, curIndex)
            newArray[targetIndex] = elementToMove
        }

        return newArray
    }

    private fun getCoordinates(array: Array<Pair<Int, Long>>): Long {
        var zeroIndex = 0
        for (i in array.indices) {
            if (array[i].second == 0L) {
                zeroIndex = i
                break
            }
        }

        var coordinates = 0L
        repeat(3) {
            zeroIndex = (zeroIndex + 1000) % array.size
            coordinates += array[zeroIndex].second
        }

        return coordinates
    }

    // Looks like a nice idea to use, without many array copies
    // But it contains a bug somewhere, didn't have time to debug
    private class CircularList(input: List<Int>) {
        val pointers = Array(input.size) { Node() }

        init {
            input.forEachIndexed { index, i ->
                val node = Node(value = i)
                if (index != 0) {
                    node.prev = pointers[index - 1].next
                    pointers[index - 1].next?.next = node
                }
                pointers[index] = Node(next = node)
                if (index == input.lastIndex) {
                    pointers[index].next?.next = pointers[0].next
                    pointers[0].next?.prev = pointers[index].next
                }
            }
        }

        fun move(index: Int) {
            val nodeToMove = pointers[index].next
            val prevNode = nodeToMove?.prev
            val nextNode = nodeToMove?.next

            nodeToMove?.value as Int
            var leftNode: Node? = nodeToMove
            if (nodeToMove.value > 0) {
                for (i in 0 until nodeToMove.value) {
                    leftNode = leftNode?.next
                }
            } else if (nodeToMove.value < 0) {
                for (i in 0 until (-nodeToMove.value + 1)) {
                    leftNode = leftNode?.prev
                }
            } else {
                return
            }
            val rightNode = leftNode?.next

            prevNode?.next = nextNode
            nextNode?.prev = prevNode
            leftNode?.next = nodeToMove
            nodeToMove.next = rightNode
            rightNode?.prev = nodeToMove
            nodeToMove.prev = leftNode
        }

        fun getCoordinates(): Int {
            var zeroNode = pointers[0].next
            while (zeroNode?.value != 0) {
                zeroNode = zeroNode?.next
            }

            var coordinates = 0
            repeat(3) {
                repeat(1000) {
                    zeroNode = zeroNode?.next
                }
                coordinates += zeroNode?.value as Int
            }

            return coordinates
        }

        fun print() {
            val sb = StringBuilder()
            var p = pointers[0].next
            for (i in pointers.indices + 1) {
                sb.append("${p?.value} -> ")
                p = p?.next
            }
            println(sb)

            sb.clear()
            p = pointers[0].next
            for (i in pointers.indices + 1) {
                sb.insert(0, " <- ${p?.value}")
                p = p?.prev
            }
            println(sb)
            println("--------------")
        }
    }

    private data class Node(
        var prev: Node? = null,
        var next: Node? = null,
        val value: Int = Int.MAX_VALUE
    )
}