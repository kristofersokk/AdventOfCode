package year2022.day17

import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource.Monotonic.markNow

const val ROCK_COUNT = 2022

@OptIn(ExperimentalTime::class)
fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day17.txt")
    val lines = file.readLines()

    val jets = lines[0].trim()
    println("Jets count: ${jets.length}")

    var fallenRocks = 0L
    // grid is rotated 90 degrees clockwise
    val grid = Array(7) { Array(10000) { '.' } }

    val ROCKS = arrayOf(
        arrayOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
        arrayOf(1 to 0, 0 to 1, 1 to 1, 2 to 1, 1 to 2),
        arrayOf(0 to 0, 1 to 0, 2 to 0, 2 to 1, 2 to 2),
        arrayOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
        arrayOf(0 to 0, 1 to 0, 0 to 1, 1 to 1),
    )

    val mark = markNow()

    var cutoffAmount = 0L

    var rockIndex = 0
    var pileHeight = 0
    var jetIndex = 0L
    while (fallenRocks < ROCK_COUNT) {

        val launchRockLoc = 2 to (pileHeight + 3)
        // Launch rock
        var rockParts =
            ROCKS[rockIndex].map { it.first + launchRockLoc.first to it.second + launchRockLoc.second }.toMutableList()
        var rockMove = RockMove.SIDEWAYS

        while (true) {
            // Move rock
            when (rockMove) {
                RockMove.SIDEWAYS -> {
                    val moveDirection = jets[(jetIndex % jets.length).toInt()]
                    jetIndex++

                    val dx = if (moveDirection == '<') -1 else 1
                    val newRockParts = rockParts.map { it.first + dx to it.second }.toMutableList()
                    if (newRockParts.all { it.first in 0 until 7 && grid[it.first][it.second] != '#' }) {
                        rockParts = newRockParts
                    }

                    rockMove = RockMove.DOWN
                }

                RockMove.DOWN -> {
                    val newRockParts = rockParts.map { it.first to it.second - 1 }.toMutableList()
                    if (newRockParts.any { it.second < 0 }) {
                        // Rock is on ground
                        break
                    }
                    if (newRockParts.any { grid[it.first][it.second] == '#' }) {
                        // Rock is on pile
                        break
                    }
                    rockParts = newRockParts

                    rockMove = RockMove.SIDEWAYS
                }
            }
        }
        // Calculate new pileHeight
        pileHeight = maxOf(
            pileHeight,
            rockParts.maxOf { it.second + 1 }
        )

        // Save rock to grid
        rockParts.forEach { grid[it.first][it.second] = '#' }

        if (pileHeight > 9000) {
            // Need to shift grid up
            cutoffAmount += 8500
            pileHeight -= 8500
            (0 until 7).forEach { x ->
                grid[x] = grid[x].drop(8500).toTypedArray() + Array(8500) { '.' }
            }
        }

        fallenRocks++
        rockIndex = (rockIndex + 1) % 5

//        if (fallenRocks % 10000000 == 0L) {
//            println()
//            println("Fallen rocks: $fallenRocks")
//        }
        print("\rProgress: ${(fallenRocks * 10000.0 / ROCK_COUNT).roundToInt().toDouble() / 100}%")
    }
    println()

//    (pileHeight downTo 0).forEach { y ->
//        print('|')
//        (0 until 7).forEach { x ->
//            print(grid[x][y])
//        }
//        print('|')
//        println()
//    }
//    println("+-------+")
//    println()

    println("Result: ${cutoffAmount + pileHeight}")
    println("Elapsed time: ${mark.elapsedNow().toIsoString()}")

    println("Part 1 development time: 1h7m")
}

private enum class RockMove {
    DOWN, SIDEWAYS
}
