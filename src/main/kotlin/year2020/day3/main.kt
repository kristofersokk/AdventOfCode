package year2020.day3

import java.io.File

fun main() {
    val file = File("src/main/resources/2020/2020-day3.txt")
    val lines = file.readLines()

    val width = lines[0].length
    val height = lines.size

    val result1 = (0 until height).count { y ->
        lines[y][(3 * y) % width] == '#'
    }
    println(result1)

    val result2 = listOf(
        1 to 1,
        3 to 1,
        5 to 1,
        7 to 1,
        1 to 2
    ).map { (dx, dy) ->
        (0 until height step dy).filterIndexed { index, y ->
            lines[y][(dx * index) % width] == '#'
        }.size
    }
        .onEach { println(it) }
        .fold(1L) { i, j -> i * j }
    println(result2)
}