package year2023.day3

import java.io.File

private data class Location(val x: Int, val y: Int)

private infix fun Int.loc(y: Int) = Location(this, y)

private typealias Part = Pair<Int, Location>

fun main() {

    val file = File("src/main/resources/2023/2023-day3.txt")
    val lines = file.readLines()

    val parts = mutableListOf<Part>()
    lines.forEachIndexed { y, line ->
        var partBuffer = ""
        var partRunningIndex = 0
        line.forEachIndexed { x, c ->
            if (c.isDigit()) {
                if (partBuffer == "") {
                    partRunningIndex = x
                }
                partBuffer += c
            } else if (partBuffer != "") {
                parts += partBuffer.toInt() to (partRunningIndex loc y)
                partBuffer = ""
            }
        }
        if (partBuffer != "") {
            parts += partBuffer.toInt() to (partRunningIndex loc y)
        }
    }

    val result1 = parts.filter { part ->
        val checkLocations = getPartAdjacentLocations(part, lines[0].length, lines.size)
        !checkLocations.all { lines[it.y][it.x].let { it.isDigit() || it == '.' } }
    }.sumOf { it.first }

    println("Result1: $result1")

    val wrongGears = mutableMapOf<Location, List<Part>>()
    parts.forEach { part ->
        val checkLocations = getPartAdjacentLocations(part, lines[0].length, lines.size)
        checkLocations.forEach {
            if (lines[it.y][it.x] == '*') {
                wrongGears[it] = wrongGears.getOrDefault(it, listOf()) + part
            }
        }
    }

    val result2 = wrongGears.values.filter { it.size == 2 }
        .sumOf { it[0].first * it[1].first }

    println("Result2: $result2")
}

private fun getPartAdjacentLocations(
    part: Part,
    maxWidth: Int,
    maxHeight: Int
): List<Location> {
    val (value, location) = part
    val partLength = value.toString().length
    return ((location.x - 1..location.x + partLength).map { it loc location.y - 1 } +
        (location.x - 1 loc location.y) +
        (location.x + partLength loc location.y) +
        (location.x - 1..location.x + partLength).map { it loc location.y + 1 })
        .filter { it.x in 0 until maxWidth && it.y in 0 until maxHeight }
}
