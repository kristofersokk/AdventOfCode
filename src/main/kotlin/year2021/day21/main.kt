package year2021.day21

import java.io.File
import kotlin.math.min

const val MAX_DIRAC_SCORE = 21

val diracCounts = arrayOf(
    3 to 1,
    4 to 3,
    5 to 6,
    6 to 7,
    7 to 6,
    8 to 3,
    9 to 1,
)

fun dirac(p1Loc: Int, p2Loc: Int, p1Score: Int, p2Score: Int, lastDiceValue: Int, playerIndex: Int): LongArray {
    if (p1Score >= MAX_DIRAC_SCORE) {
        return longArrayOf(1, 0)
    }
    if (p2Score >= MAX_DIRAC_SCORE) {
        return longArrayOf(0, 1)
    }
//    println(listOf(p1Loc, p2Loc, p1Score, p2Score, lastDiceValue, playerIndex))
    val results = diracCounts.map { (diceRoll, rollCount) ->
        when (playerIndex) {
            0 -> {
                val newLoc = ((p1Loc + diceRoll) % 10).let {
                    when (it) {
                        0 -> 10
                        else -> it
                    }
                }
                dirac(newLoc, p2Loc, p1Score + newLoc, p2Score, diceRoll, 1) * rollCount
            }
            1 -> {
                val newLoc = ((p2Loc + diceRoll) % 10).let {
                    when (it) {
                        0 -> 10
                        else -> it
                    }
                }
                dirac(p1Loc, newLoc, p1Score, p2Score + newLoc, diceRoll, 0) * rollCount
            }
            else -> longArrayOf(0, 0)
        }
    }
//    println(listOf(p1Loc, p2Loc, p1Score, p2Score, lastDiceValue, playerIndex))
//    println(results.map { it.toList() })
    return longArrayOf(
        results.sumOf { it[0] },
        results.sumOf { it[1] },
    )
}

private operator fun LongArray.times(multiplier: Int): LongArray {
    val newArray = LongArray(this.size)
    for (i in indices) {
        newArray[i] = multiplier * this[i]
    }
    return newArray
}

fun main() {
    val file = File("src/main/resources/2021/2021-day21.txt")
    val lines = file.readLines()

    val startingP1Loc = lines[0].split(": ")[1].toInt()
    val startingP2Loc = lines[1].split(": ")[1].toInt()

    var dieRolls = 0
    var dieValue = 0
    var p1Loc = startingP1Loc
    var p2Loc = startingP2Loc
    var p1Score = 0
    var p2Score = 0
    while (true) {
        var dieSum = 0
        repeat(3) {
            dieRolls++
            dieValue++
            if (dieValue > 100)
                dieValue -= 100
            dieSum += dieValue
        }
        p1Loc = ((p1Loc + dieSum) % 10).let {
            when (it) {
                0 -> 10
                else -> it
            }
        }
        p1Score += p1Loc
        if (p1Score >= 1000)
            break

        dieSum = 0
        repeat(3) {
            dieRolls++
            dieValue++
            if (dieValue > 100)
                dieValue -= 100
            dieSum += dieValue
        }
        p2Loc = ((p2Loc + dieSum) % 10).let {
            when (it) {
                0 -> 10
                else -> it
            }
        }
        p2Score += p2Loc
        if (p2Score >= 1000)
            break
    }
    val part1Answer = min(p1Score, p2Score) * dieRolls
    println(part1Answer)

    val winningUniverses = dirac(startingP1Loc, startingP2Loc, 0, 0, 0, 0)
    println(winningUniverses.toList())

    // Part 1
    // Correct: 853776

    // Part 2
    // Correct: 301304993766094
}
