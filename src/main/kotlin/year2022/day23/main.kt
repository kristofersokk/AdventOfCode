package year2022.day23

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day23.txt")
    val lines = file.readLines()

    val elveLocs = mutableSetOf<Pair<Int, Int>>()
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') {
                elveLocs.add(x to y)
            }
        }
    }

    var directionChecks = listOf(
        (0 to -1) to listOf(-1 to -1, 0 to -1, 1 to -1),
        (0 to 1) to listOf(1 to 1, 0 to 1, -1 to 1),
        (-1 to 0) to listOf(-1 to 1, -1 to 0, -1 to -1),
        (1 to 0) to listOf(1 to -1, 1 to 0, 1 to 1)
    )

    fun printGrid() {
        val minX = elveLocs.minOf { it.first }
        val maxX = elveLocs.maxOf { it.first }
        val minY = elveLocs.minOf { it.second }
        val maxY = elveLocs.maxOf { it.second }
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(if (x to y in elveLocs) '#' else '.')
            }
            println()
        }
    }

    println("Initial grid")
    printGrid()
    println()

    var simulationMovedElf = false
    fun simulateRound(roundIndex: Int, printGrid: Boolean = false) {
        simulationMovedElf = false
        val proposedLocs = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()
        elveLocs.forEach { elfLoc ->
            val isolated = (-1..1).flatMap { x ->
                (-1..1).map { y ->
                    elfLoc.first + x to elfLoc.second + y
                }
            }.filter { it != elfLoc }.all { it !in elveLocs }
            if (!isolated) {
                val directionToGo = directionChecks.firstOrNull { (_, checks) ->
                    checks.all { check -> elfLoc + check !in elveLocs}
                }?.first
                if (directionToGo != null) {
                    val newLoc = elfLoc + directionToGo
                    if (newLoc !in proposedLocs) {
                        proposedLocs[newLoc] = mutableListOf(elfLoc)
                    } else {
                        proposedLocs[newLoc]!!.add(elfLoc)
                    }
                }
            }
        }
        proposedLocs.forEach { (newLoc, oldLocs) ->
            if (oldLocs.size == 1) {
                elveLocs.remove(oldLocs.first())
                elveLocs.add(newLoc)
                simulationMovedElf = true
            }
        }
        directionChecks = directionChecks.drop(1) + directionChecks.first()

        println("Round ${roundIndex + 1}")
        if (printGrid) {
            printGrid()
            println()
        }
    }

    repeat(10) { simulateRound(it) }

    val minX = elveLocs.minOf { it.first }
    val maxX = elveLocs.maxOf { it.first }
    val minY = elveLocs.minOf { it.second }
    val maxY = elveLocs.maxOf { it.second }
    val result1 = (minX..maxX).flatMap { x ->
        (minY..maxY).map { y ->
            if (x to y in elveLocs) 0 else 1
        }
    }.sum()

    println("Result 1: $result1")

    var simulationIndex = 10
    while (simulationMovedElf) {
        simulateRound(simulationIndex)
        simulationIndex++
    }

    println("Result 2: $simulationIndex")
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}
