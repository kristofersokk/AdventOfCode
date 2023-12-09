package year2023.day9

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day9.txt")
    val lines = file.readLines()

    val result1 = lines.sumOf { line ->
        val numbers = line.split(" ").map { it.toLong() }
        val pyramid = (numbers.size downTo 1).map { LongArray(it + 1) }.toTypedArray()
        var lastLine = 0

        numbers.forEachIndexed { index, l -> pyramid[0][index] = l }

        for (y in 1 until numbers.size) {
            for (x in 0 until pyramid[y].size - 1) {
                pyramid[y][x] = pyramid[y - 1][x + 1] - pyramid[y - 1][x]
            }
            if (pyramid[y].all { it == 0L }) {
                lastLine = y
                break
            }
        }

        for (y in lastLine - 1 downTo 0) {
            pyramid[y][numbers.size - y] = pyramid[y + 1][numbers.size - y - 1] + pyramid[y][numbers.size - y - 1]
        }

        pyramid[0].last()
    }

    println("Part 1: $result1")

    val result2 = lines.sumOf { line ->
        val numbers = line.split(" ").map { it.toLong() }
        val pyramid = (numbers.size downTo 1).map { LongArray(it + 1) }.toTypedArray()
        var lastLine = 0

        numbers.forEachIndexed { index, l -> pyramid[0][index + 1] = l }

        for (y in 1 until numbers.size) {
            for (x in 1 until pyramid[y].size) {
                pyramid[y][x] = pyramid[y - 1][x + 1] - pyramid[y - 1][x]
            }
            if (pyramid[y].all { it == 0L }) {
                lastLine = y
                break
            }
        }

        for (y in lastLine - 1 downTo 0) {
            pyramid[y][0] = pyramid[y][1] - pyramid[y + 1][0]
        }

        pyramid[0][0]
    }

    println("Part 2: $result2")
}
