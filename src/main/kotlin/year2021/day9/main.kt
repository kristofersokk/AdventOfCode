package year2021.day9

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day9.txt")
    val input = file.readLines()
    val width = input[0].length
    val height = input.size

    fun getNeighbours(x: Int, y: Int): List<Pair<Int, Int>> =
        arrayOf(
            -1 to 0,
            1 to 0,
            0 to 1,
            0 to -1
        ).map { (dx, dy) -> x + dx to y + dy }.filter { (x, y) -> x in 0 until width &&
                y in 0 until height }

    // Part 1: Low points
    val part1Count = (0 until height).sumOf { y ->
        (0 until width).sumOf { x ->
            val risk = input[y][x].digitToInt() + 1
            val lowPoint = getNeighbours(x, y).all { (x, y) -> input[y][x].digitToInt() >= risk }
            if (lowPoint) risk else 0
        }
    }
    println(part1Count)
    // Correct: 494

    // Part 2: Basins
    // Find the basins by flooding from lowPoints and requiring '9' to be borders
    val lowPoints = (0 until height).flatMap { y ->
        (0 until width).mapNotNull { x ->
            val risk = input[y][x].digitToInt() + 1
            val lowPoint = getNeighbours(x, y).all { (x, y) -> input[y][x].digitToInt() >= risk }
            if (lowPoint) x to y else null
        }
    }
    val basins = lowPoints.map { lowPoint ->
        val basin = mutableSetOf(lowPoint)
        var frontier = mutableSetOf(lowPoint)
        while (frontier.isNotEmpty()) {
            frontier = frontier.flatMap { (x, y) -> getNeighbours(x, y) }
                .filter { it !in basin }
                .filter { (x, y) -> input[y][x].digitToInt() != 9 }
                .toMutableSet()
            basin.addAll(frontier)
        }
        lowPoint to basin
    }.sortedBy { (_, basin) -> -basin.size }
    val part2Answer = basins.take(3).map { (_, basin) -> basin.size }.reduce { a, b -> a * b }
    println(part2Answer)
    // Correct: 1048128

}
