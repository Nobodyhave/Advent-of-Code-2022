import kotlin.math.min

class Day7 {
    fun solve(){
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_7.txt")!!

        val tree = createTree(input)
        tree.updateSize()
        val pruneSize = tree.calculatePruneSize()

        println(pruneSize)
    }

    private fun solve2() {
        val input = readFile("day_7.txt")!!

        val tree = createTree(input)
        tree.updateSize()

        println(tree.getPruneSize())
    }

    private fun createTree(input: String): Tree {
        val tree = Tree()
        input.split("\n").forEach { line ->
            val tokens = line.split(" ")
            when {
                "$" == tokens[0] && "cd" == tokens[1]  -> {
                    if(tokens[2] == "..") tree.changeLevelUp()
                    else if(tokens[2] == "/") tree.changeToRoot()
                    else tree.changeLevelDown(tokens[2])
                }
                "$" == tokens[0] && "ls" == tokens[1]  -> {

                }
                "dir" == tokens[0] -> {
                    tree.addChildDir(tokens[1])
                }
                else -> {
                    tree.addChildFile(tokens[0].toInt(), tokens[1])
                }
            }
        }

        return tree
    }

    private class Tree {
        private val root = Dir("/")
        private var curDir = root

        fun addChildDir(c: String) {
            val child = curDir.childrenDirs[c] ?: Dir(c)

            child.parent = curDir
            curDir.childrenDirs[c] = child

        }

        fun addChildFile(size: Int, c: String) {
            val child = curDir.childrenFiles[c] ?: File(size, c)

            child.parent = curDir
            curDir.childrenFiles[c] = child
        }

        fun changeToRoot() {
            curDir = root
        }

        fun changeLevelUp() {
            curDir.parent?.let {
                curDir = it
            }
        }

        fun changeLevelDown(name: String) {
            curDir = curDir.childrenDirs[name]
                ?: throw IllegalArgumentException("Non existing dir $name")
        }

        fun updateSize() {
            updateSize(root)
        }

        private fun updateSize(dir: Dir): Int {
            val filesSize = dir.childrenFiles.values.sumOf { it.size }
            val dirsSize = dir.childrenDirs.values.sumOf { updateSize(it) }

            dir.size = filesSize + dirsSize

            return dir.size
        }

        fun calculatePruneSize(): Int {
            return calculatePruneSize(root)
        }

        fun getPruneSize(): Int {
            return getPruneSize(root, 30000000 - (70000000 - root.size))
        }

        private fun getPruneSize(dir: Dir, target: Int): Int {
            val minChild = if(dir.childrenDirs.isNotEmpty())
                dir.childrenDirs.values.minOf {getPruneSize(it,target) } else Int.MAX_VALUE

            return if(dir.size >= target && minChild >= target) {
                min(dir.size,minChild)
            } else if(dir.size >= target) {
                dir.size
            } else if(minChild >= target) {
                minChild
            } else {
                Int.MAX_VALUE
            }
        }

        private fun calculatePruneSize(dir: Dir): Int {
            val size = if (dir.size <= 100000) dir.size else 0

            return size + dir.childrenDirs.values.sumOf { calculatePruneSize(it) }
        }

        private open class Node(val name: String) {
            var parent: Dir? = null
        }

        private class File(val size: Int, name: String): Node(name)
        private class Dir(name: String): Node(name) {
            var size = 0
            val childrenDirs = mutableMapOf<String, Dir>()
            val childrenFiles = mutableMapOf<String, File>()
        }
    }
}