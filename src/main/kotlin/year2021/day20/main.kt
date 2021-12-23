package year2021.day20

import java.io.File

const val BORDER = 60
const val ENHANCEMENTS = 50

fun main() {
    val file = File("src/main/resources/2021/2021-day20.txt")
    val (decodeTextInput, secondPart) = file.readText().replace("\r", "").split(
        "\n\n"
    )
    val decodeText = decodeTextInput.replace("[\r\n]".toRegex(), "")
    val input = secondPart.trim().split("\n")
    println(decodeText)
    println(input)
    val startWidth = input[0].length
    val emptyCycles = decodeText[0] == '#'

    var grid = MutableList(startWidth + 2 * BORDER) {
        ".".repeat(startWidth + 2 * BORDER)
    }
    input.forEachIndexed { y, line ->
        grid[y + BORDER] = grid[y + BORDER].replaceRange(BORDER until BORDER + startWidth, line)
    }
    grid.forEach(::println)
    println()

    var width = startWidth
    var iteration = 0
    while (iteration < ENHANCEMENTS) {
        val newGrid = grid.toMutableList()
        for (y in 1 until newGrid.size - 1) {
            newGrid[y] = "${newGrid[y][0]}${
                (1 until newGrid.size - 1).joinToString(separator = "") { x ->
                    val newCharIndex = (-1..1).flatMap { dy ->
                        (-1..1).map { dx -> grid[y + dy][x + dx] }
                    }.joinToString(separator = "")
                        .replace('.', '0')
                        .replace('#', '1')
                        .toInt(2)
                    val newChar = decodeText[newCharIndex]
                    newChar.toString()
                }
            }${newGrid[y].last()}"
        }
        if (emptyCycles) {
            val char = if (iteration % 2 == 0) '#' else '.'
            newGrid[0] = "$char".repeat(startWidth + 2 * BORDER)
            newGrid[newGrid.size - 1] = "$char".repeat(startWidth + 2 * BORDER)
            (0 until newGrid.size).forEach { y ->
                newGrid[y] = "$char${newGrid[y].substring(1, newGrid[y].length - 1)}$char"
            }
        }
        grid = newGrid
        grid.forEach(::println)
        println()
        width += 2
        iteration++
    }

    val part1Answer = grid.sumOf { it.count { it == '#' } }
    println(part1Answer)

    // Part 1
    // BORDER = 10, ENHANCEMENTS = 2
    // Correct: 5326

    // Part 2
    // BORDER = 60, ENHANCEMENTS = 50
    // Correct: 17096
}
