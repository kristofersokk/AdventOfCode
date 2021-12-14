package year2021.day13

import java.io.File
import kotlin.math.max

private fun <T> List<T>.toPair(): Pair<T, T> {
    val (a, b) = this
    return a to b
}

fun generateData(): Pair<Array<CharArray>, List<String>> {
    val file = File("src/main/resources/2021/2021-day13.txt")
    val input = file.readLines().map { it.trim() }
    val emptyLineIndex = input.indexOf("")
    val dotsInput = input.subList(0, emptyLineIndex).map {
        it.split(",").map { it.toInt() }.toPair()
    }
    val instructions = input.drop(emptyLineIndex + 1)
    val width = dotsInput.maxOf { it.first } + 1
    val height = dotsInput.maxOf { it.second } + 1
    val table = Array(height) {
        CharArray(width) { '.' }
    }
    dotsInput.forEach {
        val (x, y) = it
        table[y][x] = '#'
    }
    return table to instructions
}

private fun simulateInstructions(inputTable: Array<CharArray>, instructions: List<String>): Array<CharArray> {
    var table = inputTable
    instructions.forEach { instruction ->
        val width = table[0].size
        val height = table.size
        val (axis, countStr) = instruction.split(" ")[2].split("=")
        val count = countStr.toInt()
        when (axis) {
            "x" -> {
                val leftWidth = count
                val rightWidth = width - leftWidth - 1
                val newTable = Array(height) {
                    CharArray(max(leftWidth, rightWidth)) { '.' }
                }
                for (y in 0 until height) {
                    for (x in 0 until leftWidth) {
                        newTable[y][max(0, rightWidth - leftWidth) + x] = table[y][x]
                    }
                    for (x in 0 until rightWidth) {
                        if (table[y][leftWidth + 1 + x] == '#') {
                            newTable[y][max(leftWidth, rightWidth) - 1 - x] = '#'
                        }
                    }
                }
                table = newTable
            }
            "y" -> {
                val upHeight = count
                val downHeight = height - upHeight - 1
                val newTable = Array(max(upHeight, downHeight)) {
                    CharArray(width) { '.' }
                }
                for (x in 0 until width) {
                    for (y in 0 until upHeight) {
                        newTable[max(0, downHeight - upHeight) + y][x] = table[y][x]
                    }
                    for (y in 0 until downHeight) {
                        if (table[upHeight + 1 + y][x] == '#') {
                            newTable[max(upHeight, downHeight) - 1 - y][x] = '#'
                        }
                    }
                }
                table = newTable
            }
        }
    }
    return table
}

fun main() {

    // Part 1
    val (table1, instructions1) = generateData()
    val part1Instructions = instructions1.take(1)
    val newTable1 = simulateInstructions(table1, part1Instructions)
    newTable1.forEach {
        println(it.joinToString(separator = ""))
    }
    println(newTable1.sumOf { it.count { it == '#' } })
    // Correct: 693

    println()

    // Part 2
    val (table2, instructions2) = generateData()
    val newTable2 = simulateInstructions(table2, instructions2)
    newTable2.forEach {
        println(it.joinToString(separator = ""))
    }
    println(newTable2.sumOf { it.count { it == '#' } })
    // Correct: UCLZRAZU

}
