import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max

class Day15 {
    fun solve() {
        solve1()
        solve2()
    }

    private fun solve1() {
        val input = readFile("day_15.txt")!!.split("\n")
        val sensors = mutableListOf<Sensor>()
        input.forEach {
            val b = it.substringAfter("closest beacon is at ").split(",")
            val bC = b.map { it.split("=") }
            val beacon = Beacon(bC[0][1].toInt(), bC[1][1].toInt())

            val s = it.substringAfter("Sensor at ").substringBefore(":").split(",")
            val sC = s.map { it.split("=") }
            sensors.add(Sensor(sC[0][1].toInt(), sC[1][1].toInt(), beacon))
        }

        val intervals = getIntervals(2000000, sensors)
        println(countRow(2000000, intervals, sensors))
    }

    private fun solve2() {
        val input = readFile("day_15.txt")!!.split("\n")
        val sensors = mutableListOf<Sensor>()
        input.forEach {
            val b = it.substringAfter("closest beacon is at ").split(",")
            val bC = b.map { it.split("=") }
            val beacon = Beacon(bC[0][1].toInt(), bC[1][1].toInt())

            val s = it.substringAfter("Sensor at ").substringBefore(":").split(",")
            val sC = s.map { it.split("=") }
            sensors.add(Sensor(sC[0][1].toInt(), sC[1][1].toInt(), beacon))
        }

        val size = 4000000
        for(row in 0..size) {
            val intervals = getIntervals(row, sensors)
            val busy = getBusyForRow(row, sensors)
            for(i in 1 until intervals.size) {
                val end = intervals[i-1].end + 1
                val start = intervals[i].start - 1
                for(j in end..start) {
                    if(i > size) break
                    if(!busy.contains(j)) {
                        println(j * 4000000L + row)
                        return
                    }
                }
            }
        }
    }

    private fun getIntervals(row: Int, sensors: List<Sensor>): List<Interval> {
        val intervals = mutableListOf<Interval>()

        sensors.forEach { sensor ->
            val distToRow = abs(row - sensor.y)
            val sensorDist = calculateDistance(sensor)
            if (distToRow <= sensorDist) {
                intervals.add(
                    Interval(
                        sensor.x - (sensorDist - distToRow),
                        sensor.x + (sensorDist - distToRow)
                    )
                )
            }
        }

        intervals.sortBy { it.start }
        val stack = LinkedList<Interval>()
        intervals.forEach {
            if (stack.isEmpty()) {
                stack.addLast(it)
            } else {
                if (stack.peek().end >= it.start) {
                    val prev = stack.pollLast()
                    stack.addLast(Interval(prev.start, max(prev.end, it.end)))
                } else {
                    stack.addLast(it)
                }
            }
        }

        return stack
    }

    private fun countRow(row: Int, intervals: List<Interval>, sensors: List<Sensor>): Int {
        val count = intervals.sumOf { it.end - it.start + 1 }
        val busy = getBusyForRow(row, sensors).count()

        return count - busy
    }

    private fun getBusyForRow(row: Int, sensors: List<Sensor>): Set<Int> {
        return sensors
            .filter { it.y == row || it.beacon.y == row }
            .map { if(it.y == row) it.x else it.beacon.x }
            .toSet()
    }

    private fun calculateDistance(sensor: Sensor): Int {
        return abs(sensor.x - sensor.beacon.x) + abs(sensor.y - sensor.beacon.y)
    }

    data class Beacon(val x: Int, val y: Int)

    data class Sensor(val x: Int, val y: Int, val beacon: Beacon)

    private data class Interval(val start: Int, val end: Int)
}