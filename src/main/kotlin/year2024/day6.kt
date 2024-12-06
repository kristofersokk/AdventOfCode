package year2024

import java.io.File

private enum class Direction(
    val dx: Int,
    val dy: Int
) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);
}

sealed class SimulationResult
data object InfiniteLoop : SimulationResult()
data class ExitedArena(
    val visited: Set<Pair<Int, Int>>
) : SimulationResult()

fun simulate(lines: List<String>): SimulationResult {
    var position = lines.indexOfFirst { '^' in it }.let {
        it to lines[it].indexOf('^')
    }
    var direction = Direction.UP
    val visited = mutableSetOf(position)

    var moves = 0
    val maxMoves = lines.size * lines.first().length

    while (position.second in lines.first().indices && position.first in lines.indices) {
        visited.add(position)

        if (moves > maxMoves) {
            return InfiniteLoop
        }

        val next = position.first + direction.dy to position.second + direction.dx
        if (next.first in lines.indices && next.second in lines.first().indices && lines[next.first][next.second] == '#') {
            direction = when (direction) {
                Direction.UP -> Direction.RIGHT
                Direction.DOWN -> Direction.LEFT
                Direction.LEFT -> Direction.UP
                Direction.RIGHT -> Direction.DOWN
            }
            visited.add(position)
        } else {
            position = next
            moves++
        }
    }

    return ExitedArena(visited)
}

fun main() {
    val file = File("src/main/resources/2024/2024-day6.txt")
    val lines = file.readLines()

    val result1Simulation = simulate(lines)
    val result1 = (result1Simulation as? ExitedArena)?.visited?.size

    println("Part 1: $result1")

    val result2 = lines.indices.sumOf { y ->
        lines.first().indices.count { x ->
            if (lines[y][x] in arrayOf('#', '^')) {
                return@count false
            }
            simulate(lines.mapIndexed { i, line ->
                if (i == y) {
                    line.substring(0, x) + "#" + line.substring(x + 1)
                } else {
                    line
                }
            }) is InfiniteLoop
        }
    }

    println("Part 2: $result2")
}