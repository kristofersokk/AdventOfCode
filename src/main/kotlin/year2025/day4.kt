package year2025

import java.io.File

fun main() {
    val file = File("src/main/resources/2025/2025-day4.txt")
    val lines = file.readLines()

    val result1 = lines.indices
        .flatMap { y -> lines.first().indices.map { x -> x to y } }
        .count { (x, y) ->
            val isRoll = lines[y][x] == '@'
            val hasLessThanFourNeighbors = (-1..1).asSequence()
                .flatMap { dx -> (-1..1).map { dy -> dx to dy } }
                .filter { (x, y) -> x != 0 || y != 0 }
                .map { (dx, dy) -> x + dx to y + dy }
                .filter { (x, y) -> x in 0 until lines.first().length && y in 0 until lines.size }
                .map { (x, y) -> lines[y][x] }
                .count { it == '@' } < 4
            isRoll && hasLessThanFourNeighbors
        }

    println("result1: $result1")

    val result2 = run {
        val changeableLines = lines.map { it.toMutableList() }
        fun canGetRemoved() = changeableLines.indices
            .flatMap { y -> changeableLines.first().indices.map { x -> x to y } }
            .filter { (x, y) ->
                val isRoll = changeableLines[y][x] == '@'
                val hasLessThanFourNeighbors = (-1..1).asSequence()
                    .flatMap { dx -> (-1..1).map { dy -> dx to dy } }
                    .filter { (x, y) -> x != 0 || y != 0 }
                    .map { (dx, dy) -> x + dx to y + dy }
                    .filter { (x, y) -> x in 0 until changeableLines.first().size && y in 0 until changeableLines.size }
                    .map { (x, y) -> changeableLines[y][x] }
                    .count { it == '@' } < 4
                isRoll && hasLessThanFourNeighbors
            }

        var removedCount = 0
        while (canGetRemoved().isNotEmpty()) {
            canGetRemoved()
                .forEach { (x, y) ->
                    changeableLines[y][x] = '.'
                    removedCount++
                }
        }
        removedCount
    }

    println("result2: $result2")
}