import java.util.LinkedList
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

class Day19 {
    fun solve() {
        solve1()
        solve2()
    }

    val cache: MutableMap<Resources, Int> = mutableMapOf()

    private fun solve1() {
        val input = readFile("day_19.txt")!!.split("\n")
        val blueprints = getBlueprints(input)

        var result = 0
        blueprints.forEach {
            cache.clear()
            result += produce(it, Resources(minutes = 24)).also { println(it) } * it.index
        }

        println("Result: $result")
    }

    private fun solve2() {
        val input = readFile("day_19.txt")!!.split("\n").take(3)
        val blueprints = getBlueprints(input)

        var result = 1
        blueprints.forEach {
            cache.clear()
            result *= produce(it, Resources(minutes = 32)).also { println(it) }
        }

        println("Result: $result")
    }

    private fun getBlueprints(input: List<String>): List<Blueprint> {
        return input.mapIndexed { index, s ->
            Blueprint(
                index + 1,
                s.substringAfter("ore robot costs ").substringBefore(" ore. Each clay").toInt(),
                s.substringAfter("clay robot costs ").substringBefore(" ore. Each obsidian")
                    .toInt(),
                Pair(
                    s.substringAfter("obsidian robot costs ").substringBefore(" ore and ").toInt(),
                    s.substringAfter( " ore and ").substringBefore(" clay. Each geode").toInt(),
                ),
                Pair(
                    s.substringBeforeLast(" ore and ").substringAfter("geode robot costs ").toInt(),
                    s.substringAfterLast( " ore and ").substringBefore(" obsidian.").toInt(),
                )
            )
        }
    }

    private fun produce(blueprint: Blueprint, resources: Resources): Int {
        if(resources.minutes < 0) throw IllegalStateException("Volcano has already erupted")
        if(resources.minutes <= 0) return resources.geode
        if(cache.containsKey(resources)) return cache[resources]!!

        var result = 0
        var attempt: Int

        if(resources.ore >= blueprint.geodeRobotCost.first
            && resources.obsidian >= blueprint.geodeRobotCost.second) {
            attempt = produce(blueprint, resources.copy(
                ore = resources.ore + resources.oreRobot
                        - blueprint.geodeRobotCost.first,
                clay = resources.clay + resources.clayRobot,
                obsidian = resources.obsidian + resources.obsidianRobot
                        - blueprint.geodeRobotCost.second,
                geode = resources.geode + resources.geodeRobot,
                geodeRobot = resources.geodeRobot + 1,
                minutes = resources.minutes - 1,
            ))
            result = max(attempt, result)
        } else {
            var minutesToSkip = max(
                calculateMinutesToSkip(
                    blueprint.geodeRobotCost.first, resources.ore, resources.oreRobot),
                calculateMinutesToSkip(
                    blueprint.geodeRobotCost.second, resources.obsidian, resources.obsidianRobot),
            )

            if(minutesToSkip != Int.MAX_VALUE) {
                minutesToSkip = min(resources.minutes, minutesToSkip)
                attempt = produce(
                    blueprint, resources.copy(
                        ore = resources.ore + resources.oreRobot * minutesToSkip,
                        clay = resources.clay + resources.clayRobot * minutesToSkip,
                        obsidian = resources.obsidian + resources.obsidianRobot * minutesToSkip,
                        geode = resources.geode + resources.geodeRobot * minutesToSkip,
                        minutes = resources.minutes - minutesToSkip,
                    )
                )
                result = max(attempt, result)
            }
        }

        if(resources.obsidianRobot < blueprint.maxObsidian) {
            if (resources.ore >= blueprint.obsidianRobotCost.first
                && resources.clay >= blueprint.obsidianRobotCost.second
            ) {
                attempt = produce(
                    blueprint, resources.copy(
                        ore = resources.ore + resources.oreRobot
                                - blueprint.obsidianRobotCost.first,
                        clay = resources.clay + resources.clayRobot
                                - blueprint.obsidianRobotCost.second,
                        obsidian = resources.obsidian + resources.obsidianRobot,
                        geode = resources.geode + resources.geodeRobot,
                        obsidianRobot = resources.obsidianRobot + 1,
                        minutes = resources.minutes - 1,
                    )
                )
                result = max(attempt, result)
            } else {
                var minutesToSkip = max(
                    calculateMinutesToSkip(
                        blueprint.obsidianRobotCost.first, resources.ore, resources.oreRobot
                    ),
                    calculateMinutesToSkip(
                        blueprint.obsidianRobotCost.second, resources.clay, resources.clayRobot
                    ),
                )

                if(minutesToSkip != Int.MAX_VALUE) {
                    minutesToSkip = min(resources.minutes, minutesToSkip)
                    attempt = produce(
                        blueprint, resources.copy(
                            ore = resources.ore + resources.oreRobot * minutesToSkip,
                            clay = resources.clay + resources.clayRobot * minutesToSkip,
                            obsidian = resources.obsidian + resources.obsidianRobot * minutesToSkip,
                            geode = resources.geode + resources.geodeRobot * minutesToSkip,
                            minutes = resources.minutes - minutesToSkip,
                        )
                    )
                    result = max(attempt, result)
                }
            }
        }

        if(resources.clayRobot < blueprint.maxClay) {
            if (resources.ore >= blueprint.clayRobotCost) {
                attempt = produce(
                    blueprint, resources.copy(
                        ore = resources.ore + resources.oreRobot
                                - blueprint.clayRobotCost,
                        clay = resources.clay + resources.clayRobot,
                        obsidian = resources.obsidian + resources.obsidianRobot,
                        geode = resources.geode + resources.geodeRobot,
                        clayRobot = resources.clayRobot + 1,
                        minutes = resources.minutes - 1,
                    )
                )
                result = max(attempt, result)
            } else {
                var minutesToSkip = calculateMinutesToSkip(
                    blueprint.clayRobotCost, resources.ore, resources.oreRobot
                )

                if(minutesToSkip != Int.MAX_VALUE) {
                    minutesToSkip = min(resources.minutes, minutesToSkip)
                    attempt = produce(
                        blueprint, resources.copy(
                            ore = resources.ore + resources.oreRobot * minutesToSkip,
                            clay = resources.clay + resources.clayRobot * minutesToSkip,
                            obsidian = resources.obsidian + resources.obsidianRobot * minutesToSkip,
                            geode = resources.geode + resources.geodeRobot * minutesToSkip,
                            minutes = resources.minutes - minutesToSkip,
                        )
                    )
                    result = max(attempt, result)
                }
            }
        }

        if(resources.oreRobot < blueprint.maxOre) {
            if (resources.ore >= blueprint.oreRobotCost) {
                attempt = produce(
                    blueprint, resources.copy(
                        ore = resources.ore + resources.oreRobot
                                - blueprint.oreRobotCost,
                        clay = resources.clay + resources.clayRobot,
                        obsidian = resources.obsidian + resources.obsidianRobot,
                        geode = resources.geode + resources.geodeRobot,
                        oreRobot = resources.oreRobot + 1,
                        minutes = resources.minutes - 1,
                    )
                )
                result = max(attempt, result)
            } else {
                var minutesToSkip = calculateMinutesToSkip(
                    blueprint.oreRobotCost, resources.ore, resources.oreRobot
                )

                if(minutesToSkip != Int.MAX_VALUE) {
                    minutesToSkip = min(resources.minutes, minutesToSkip)
                    attempt = produce(
                        blueprint, resources.copy(
                            ore = resources.ore + resources.oreRobot * minutesToSkip,
                            clay = resources.clay + resources.clayRobot * minutesToSkip,
                            obsidian = resources.obsidian + resources.obsidianRobot * minutesToSkip,
                            geode = resources.geode + resources.geodeRobot * minutesToSkip,
                            minutes = resources.minutes - minutesToSkip,
                        )
                    )
                    result = max(attempt, result)
                }
            }

                attempt = produce(
                blueprint, resources.copy(
                    ore = resources.ore + resources.oreRobot * resources.minutes,
                    clay = resources.clay + resources.clayRobot * resources.minutes,
                    obsidian = resources.obsidian + resources.obsidianRobot * resources.minutes,
                    geode = resources.geode + resources.geodeRobot * resources.minutes,
                    minutes = 0,
                )
            )
            result = max(attempt, result)
        }

        cache[resources] = result

        return result
    }

    private fun calculateMinutesToSkip(target: Int, resource: Int, robots: Int): Int {
        if(robots == 0) return Int.MAX_VALUE
        val minutes = (target - resource) / robots
       return (target - resource) / robots + if(minutes * robots == target - resource) 0 else 1
    }

    data class Blueprint(
        val index: Int,
        val oreRobotCost: Int,
        val clayRobotCost: Int,
        val obsidianRobotCost: Pair<Int, Int>,
        val geodeRobotCost: Pair<Int, Int>,
        val maxOre: Int = max(max(max(oreRobotCost, clayRobotCost), obsidianRobotCost.first),
            geodeRobotCost.first),
        val maxClay: Int = obsidianRobotCost.second,
        val maxObsidian: Int = geodeRobotCost.second
    )

    data class Resources(
        var ore: Int = 0,
        var clay: Int = 0,
        var obsidian: Int = 0,
        var geode: Int = 0,
        var oreRobot: Int = 1,
        var clayRobot: Int = 0,
        var obsidianRobot: Int = 0,
        var geodeRobot: Int = 0,
        val minutes: Int,
    )
}