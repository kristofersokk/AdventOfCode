package year2021.day4

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day4.txt")
    val text = file.readText()

    val components = text.replace("\r", "")
        .split("\n\n")
    val numbers = components[0].split(",")
        .map { it.trim().toInt() }

    val boardsNumbers = components.drop(1)
        .map { component ->
            component.split("\n")
                .map { line -> line.trim().split(" +".toRegex()).map { it.toInt() } }
        }

    val boardsMarkedNumbers = Array(boardsNumbers.size) {
        mutableSetOf<Int>()
    }

    // I might have broken part 1 when rewriting to support part 2
    var winningBoard = -1 to -1
    var losingBoard = -1 to -1
    val boardWins = mutableMapOf<Int, Int>()

    for (numberIndex in numbers.indices) {
        val number = numbers[numberIndex]
        if (boardWins.size >= boardsNumbers.size) {
            break
        }
        for (boardIndex in boardsNumbers.indices) {
            boardsMarkedNumbers[boardIndex].add(number)
            if (
                boardsNumbers[boardIndex].any { it.all { it in boardsMarkedNumbers[boardIndex] } } ||
                (0 until 5).any { xIndex -> boardsNumbers[boardIndex].all { it[xIndex] in boardsMarkedNumbers[boardIndex] } }
            ) {
                if (boardWins.isEmpty()) {
                    winningBoard = boardIndex to number
                }
                if (boardWins.size == boardsNumbers.size - 1) {
                    losingBoard = boardIndex to number
                }
                boardWins[boardIndex] = number
            }
        }
    }

    val winningBoardUnmarkedSum = boardsNumbers[winningBoard.first].flatten()
        .filter { it !in boardsMarkedNumbers[winningBoard.first] }
        .sum()

    println(winningBoard.second * winningBoardUnmarkedSum)

    val losingBoardUnmarkedSum = boardsNumbers[losingBoard.first].flatten()
        .filter { it !in boardsMarkedNumbers[losingBoard.first] }
        .sum()

    println(losingBoard.second * losingBoardUnmarkedSum)
}
