package year2023.day14

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day14.txt")
    val lines = file.readLines()

    val columns = lines.first().indices.map { x ->
        val columnChars = lines.map { it[x] }.toMutableList()
        var checkIndex = 0
        while (checkIndex < columnChars.size - 1) {
            if (checkIndex < 0) {
                checkIndex = 0
            } else if (columnChars[checkIndex] == '.' && columnChars[checkIndex + 1] == 'O') {
                columnChars[checkIndex] = 'O'
                columnChars[checkIndex + 1] = '.'
                checkIndex--
            } else {
                checkIndex++
            }
        }
        columnChars.joinToString("")
    }

    val result1 = columns.sumOf { column ->
        column.mapIndexed { index, c -> if (c == 'O') (column.length - index) else 0 }.sum()
    }

    println("Part 1: $result1")
}
