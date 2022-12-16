package year2022.day16

import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day16.txt")
    val lines = file.readLines()

    val valves = lines.associate { line ->
        val parts = line.split(";")
        val name = parts[0].split(" has")[0].split(" ")[1]
        val flowRate = parts[0].split("rate=")[1].toInt()
        val leadToValves =
            parts[1].split("${parts[1].split(" ")[4]} ")[1].split(", ")
        name to Valve(name, flowRate, leadToValves)
    }
    println("Parsed data:")
    valves.values.forEach { println(it) }
    println()

    val releasableValvesNames = valves.values.filter { it.flowRate != 0 }.map { it.name }

    val distances = mutableMapOf<String, MutableMap<String, Int>>()
    valves.values.filter { it.name == "AA" || it.name in releasableValvesNames }.forEach { valve ->
        val (name) = valve
        distances[name] = mutableMapOf()

        val toProcess = mutableListOf(name to 0)
        val visited = mutableSetOf(name)
        while (toProcess.isNotEmpty()) {
            val (currentValveName, currentDistance) = toProcess.removeFirst()
            distances[name]!![currentValveName] = currentDistance
            visited.add(currentValveName)
            val currentValve = valves[currentValveName]
            currentValve!!.leadToValves.forEach { leadToValve ->
                if (leadToValve !in visited) {
                    toProcess.add(leadToValve to currentDistance + 1)
                }
            }
        }
        distances[name]!!.remove(name)
        distances[name] = distances[name]!!.filter { it.key in releasableValvesNames }.toMutableMap()
    }

    println("Distances to other releasable valves:")
    distances.asSequence().sortedBy { it.key }.forEach { (valveName, ds) ->
        println("$valveName: $ds")
    }
    println()

    var maxPressure = 0
    fun simulate(currentValve: String, currentPressure: Int, timeRemaining: Int, openedValves: Set<String>) {
        val distancesTo = distances[currentValve]!!
        val canGoTo = distancesTo.filter { it.key !in openedValves }.filter { it.value + 1 <= timeRemaining }
        if (canGoTo.isEmpty()) {
            val finalPressure = currentPressure + openedValves.sumOf { valves[it]!!.flowRate * timeRemaining }
            if (finalPressure > maxPressure) {
                maxPressure = finalPressure
            }
        } else {
            canGoTo.forEach { (valveName, distance) ->
                simulate(
                    valveName,
                    currentPressure + openedValves.sumOf { valves[it]!!.flowRate * (distance + 1) },
                    timeRemaining - distance - 1,
                    openedValves + valveName
                )
            }
        }
    }
    simulate("AA", 0, 30, setOf())

    println("Result1: $maxPressure")

    println()
    println("Calculating part 2...")

    var maxPressureWithElephant = 0
    fun simulateWithElephant(
        movementsToValves: List<Pair<String, Int>>,
        currentPressure: Int,
        timeRemaining: Int,
        openedValves: Set<String>,
        level: Int
    ) {
        val needDestination = movementsToValves.withIndex().filter { it.value.second == 0 }
        val canTakeAnotherValve = needDestination.map {
            val distancesTo = distances[it.value.first]!!
            val canGoTo = distancesTo.mapValues { it.value + 1 }
                .filter { it.key !in openedValves && it.key !in movementsToValves.map { it.first } }
                .filter { it.value <= timeRemaining }
            it to canGoTo
        }.filter { it.second.isNotEmpty() }
        val isAnyBodyInMoving = movementsToValves.any { it.second > 0 }

        if (canTakeAnotherValve.isNotEmpty()) {
            val totalA = canTakeAnotherValve.sumOf { it.second.size }
            var AIndex = 0

            canTakeAnotherValve.forEach { person ->
                val (needsDestination, canGoToList) = person
                val (personIndex) = needsDestination
                canGoToList.forEach { (goNextLoc, goNextDist) ->
                    val nextDestinations = movementsToValves.toMutableList()
                    nextDestinations[personIndex] = goNextLoc to goNextDist

                    if (level == 0) {
                        print("\rProgress: ${(AIndex * 10000.0 / totalA).roundToInt().toDouble() / 100}%")
                    }
                    AIndex++

                    simulateWithElephant(
                        nextDestinations,
                        currentPressure,
                        timeRemaining,
                        openedValves,
                        level + 1
                    )
                }
            }
        } else if (isAnyBodyInMoving) {
            val simulateNMinutes = movementsToValves.map { it.second }.filter { it > 0 }.min()
            val newPressure = currentPressure + openedValves.sumOf { valves[it]!!.flowRate * simulateNMinutes }
            val newMovementsToValves = movementsToValves.map { it.first to maxOf(it.second - simulateNMinutes, 0) }
            val newOpenedValves = openedValves + newMovementsToValves.filter { it.second == 0 }.map { it.first }
            simulateWithElephant(
                newMovementsToValves,
                newPressure,
                timeRemaining - simulateNMinutes,
                newOpenedValves,
                level + 1
            )
        } else {
            val finalPressure = currentPressure + openedValves.sumOf { valves[it]!!.flowRate * timeRemaining }
            if (finalPressure > maxPressureWithElephant) {
                maxPressureWithElephant = finalPressure
            }
        }
    }

    val part2Duration = measureTime {
        simulateWithElephant(List(2) { "AA" to 0 }, 0, 26, setOf(), 0)
    }
    println()

    println("Part 2 duration: ${part2Duration.toIsoString()}")
    println("Result2: $maxPressureWithElephant")

    println()
    println("Development time: 3h48m25s")
    println("Should have done dynamic programming not naive recursion")
}

private data class Valve(
    val name: String,
    val flowRate: Int,
    val leadToValves: List<String>,
)
