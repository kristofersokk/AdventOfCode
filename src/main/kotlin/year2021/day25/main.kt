package year2021.day25

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day25.txt")
    val lines = file.readLines().map { it.trim() }
    val width = lines[0].length
    val height = lines.size

    var step = 0
    var grid = lines.map { it.toCharArray() }
    while (true) {
        step++
        val newGrid1 = grid.map { it.copyOf() }
        var somethingDone = false
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                if (grid[y][x] == '>' &&
                    grid[y][(x + 1) % width] == '.'
                ) {
                    newGrid1[y][x] = '.'
                    newGrid1[y][(x + 1) % width] = '>'
                    somethingDone = true
                }
            }
        }
        val newGrid2 = newGrid1.map { it.copyOf() }
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                if (newGrid1[y][x] == 'v' &&
                    newGrid1[(y + 1) % height][x] == '.'
                ) {
                    newGrid2[y][x] = '.'
                    newGrid2[(y + 1) % height][x] = 'v'
                    somethingDone = true
                }
            }
        }
        grid = newGrid2
        println(step)
        newGrid2.forEach {
            println(it.joinToString(""))
        }
        println()
        if (!somethingDone) {
            break
        }
    }

    // Part 1
    // Correct: 429

    // Part 2
    // Correct:
}
