package year2022.day24

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File("src/main/resources/2022/2022-day24.txt")
    val lines = file.readLines()

    val width = lines[0].length - 2
    val height = lines.size - 2
    val startLoc = lines.first().indexOf('.') - 1
    val endLoc = lines.last().indexOf('.') - 1
    val blizzardsByRow = List(height) { mutableListOf<Blizzard>() }
    val blizzardsByCol = List(width) { mutableListOf<Blizzard>() }

    lines.drop(1).dropLast(1).forEachIndexed { rowIndex, row ->
        row.drop(1).dropLast(1).forEachIndexed { colIndex, ch ->
            when (ch) {
                '<' -> blizzardsByRow[rowIndex].add(Blizzard(colIndex, null, -1))
                '>' -> blizzardsByRow[rowIndex].add(Blizzard(colIndex, null, 1))
                '^' -> blizzardsByCol[colIndex].add(Blizzard(null, rowIndex, -1))
                'v' -> blizzardsByCol[colIndex].add(Blizzard(null, rowIndex, 1))
            }
        }
    }

    var currentTime = 0
    var alternatives = setOf(startLoc to -1)
    var goalLocs = listOf(
        endLoc to height, // part 1
        startLoc to -1,
        endLoc to height, // part 2
    )
    val goalMinutes = mutableListOf<Int>()

    fun printGrid() {
        println((-1..width).joinToString("") { if (it == startLoc) "." else "#" })
        (0 until height).forEach { rowIndex ->
            print('#')
            (0 until width).forEach { colIndex ->
                val blizzards =
                    blizzardsByRow[rowIndex].filter { (it.startIndexInRow!! + it.dxy * currentTime) properMod width == colIndex } +
                    blizzardsByCol[colIndex].filter { (it.startIndexInCol!! + it.dxy * currentTime) properMod height == rowIndex }
                print(
                    when {
                        colIndex to rowIndex in alternatives -> "E"
                        blizzards.size > 1 -> blizzards.size.digitToChar()
                        blizzards.isNotEmpty() -> when (true) {
                            (blizzards[0].startIndexInRow != null) -> if (blizzards[0].dxy < 0) '<' else '>'
                            (blizzards[0].startIndexInCol != null) -> if (blizzards[0].dxy < 0) '^' else 'v'
                            else -> throw IllegalStateException()
                        }
                        else -> "."
                    }
                )
            }
            println('#')
        }
        println((-1..width).joinToString("") { if (it == endLoc) "." else "#" })
    }

    while (goalLocs.isNotEmpty()) {
        if (alternatives.isEmpty()) {
            throw IllegalStateException("Alternatives are empty")
        }
        currentTime++
        val newAlternatives = mutableSetOf<Pair<Int, Int>>()
        for ((x, y) in alternatives) {
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    if (abs(dx) + abs(dy) < 2) {
                        val newX = x + dx
                        val newY = y + dy
                        if (newX == endLoc && newY == height) {
                            newAlternatives.add(newX to newY)
                        } else if (newX == startLoc && newY == -1) {
                            newAlternatives.add(newX to newY)
                        } else if (newX in 0 until width && newY in 0 until height &&
                            blizzardsByRow[newY].none {
                                (it.startIndexInRow!! + it.dxy * currentTime) properMod width == newX
                            } && blizzardsByCol[newX].none {
                                (it.startIndexInCol!! + it.dxy * currentTime) properMod height == newY
                            }
                        ) {
                            newAlternatives.add(newX to newY)
                        }
                    }
                }
            }
        }
        alternatives = newAlternatives
        val currentGoal = goalLocs.first()
        if (currentGoal in alternatives) {
            println("Found goal $currentGoal at $currentTime")
            goalLocs = goalLocs.drop(1)
            alternatives = setOf(currentGoal)
            goalMinutes.add(currentTime)
        }

//        println("Time: $currentTime")
//        println("Alternatives: ${alternatives.size}, $alternatives")
//        printGrid()
//        println()
    }

    println("Part 1: ${goalMinutes[0]}")
    println("Part 2: ${goalMinutes[2]}")

}

private infix fun Int.properMod(other: Int): Int {
    return ((this % other) + other) % other
}

private data class Blizzard(
    val startIndexInRow: Int?,
    val startIndexInCol: Int?,
    val dxy: Int,
)
