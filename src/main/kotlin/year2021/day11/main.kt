package year2021.day11

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day11.txt")
    val input = file.readLines()

    // Part 1
    val table1 = input.map { it.trim().map { it.digitToInt() }.toIntArray() }
    var flashes1 = 0L
    repeat(100) {
        flashes1 = stepOfTable(it, table1, flashes1)
    }
    println(100)
    println("Part1 answer: $flashes1")
    println()
    // Correct: 1700

    // Part 2
    val table2 = input.map { it.trim().map { it.digitToInt() }.toIntArray() }
    var flashes2 = 0L
    var index2 = 0
    while (true) {
        val newFlashes = stepOfTable(index2, table2, flashes2)
        if (newFlashes - flashes2 == 100L) {
            println(index2 + 1)
            println("Part2 answer: ${index2 + 1}")
            break
        }
        flashes2 = newFlashes
        index2++
    }
    // Correct: 273

}

private fun stepOfTable(it: Int, table: List<IntArray>, flashes: Long): Long {
    var flashes1 = flashes
    println(it)
    table.forEach {
        println(it.joinToString(separator = ""))
    }
    println()
    for (y in table.indices) {
        for (x in 0 until table[0].size) {
            table[y][x]++
        }
    }
    val frontier = mutableSetOf<Pair<Int, Int>>()
    val been = mutableSetOf<Pair<Int, Int>>()
    for (y in table.indices) {
        for (x in 0 until table[0].size) {
            if (table[y][x] > 9) {
                frontier.add(x to y)
            }
        }
    }
    while (frontier.isNotEmpty()) {
        val (x, y) = frontier.first()
        frontier.remove(x to y)
//            println(x to y)
        flashes1++
        been.add(x to y)
        for (dx in -1..1) {
            for (dy in -1..1) {
                val newX = x + dx
                val newY = y + dy
                if ((dx != 0 || dy != 0) && newX in 0 until table[0].size && newY in table.indices) {
                    table[newY][newX]++
                    if (table[newY][newX] > 9 && newX to newY !in been) {
//                            println("Added: ${newX to newY}")
                        frontier.add(newX to newY)
                    }
                }
            }
        }
    }
    for (y in table.indices) {
        for (x in 0 until table[0].size) {
            if (table[y][x] > 9) {
                table[y][x] = 0
            }
        }
    }
    return flashes1
}
