package year2021.day15

import java.io.File
import java.util.*

private typealias Location = Pair<Int, Int>

fun main() {
    val file = File("src/main/resources/2021/2021-day15.txt")
    val inputGrid = file.readLines().filterNot { it.isBlank() }.map { it.trim().map { it.digitToInt() } }
//    val grid = inputGrid // part 1 grid
    val grid = (0 until 5).flatMap { gridY -> // part 2 grid
        (inputGrid.indices).map { y ->
            (0 until 5).flatMap { gridX ->
                inputGrid[y].map { (it + gridX + gridY) % 9 }.map {
                    when (it) {
                        0 -> 9
                        else -> it
                    }
                }
            }
        }
    }
    grid.forEach { println(it.joinToString(separator = "")) }
    val width = grid[0].size
    val height = grid.size
    val endLocation = width - 1 to height - 1

    val bestPaths = mutableMapOf<Location, Pair<Int, List<Location>>>()
    val frontier = PriorityQueue(20, Comparator.comparingInt { (x, y): Location ->
        width - x + height - y + (bestPaths[x to y]?.first ?: 0)
    })
    frontier.add(0 to 0)
    bestPaths[0 to 0] = 0 to listOf(0 to 0)

    while (true) {
        val (x, y) = frontier.poll()
        if (x to y == endLocation) {
            break
        }
        val (currentCost, currentPath) = bestPaths[x to y]!!
        val neighbours: List<Location> = listOf(
            -1 to 0,
            1 to 0,
            0 to 1,
            0 to -1,
        ).map { (dx, dy) -> x + dx to y + dy }
            .filter { (x, y) -> x in 0 until width && y in 0 until height }
//        println()
//        println(x to y)
//        println(currentCost)
//        println(currentPath)
        neighbours.forEach { neighbour ->
            val neighbourCost = bestPaths[neighbour]?.first ?: Int.MAX_VALUE
            val newCost = currentCost + grid[neighbour.second][neighbour.first]
//            println(neighbour)
//            println(newCost)
            if (neighbour !in bestPaths || newCost < neighbourCost) {
                bestPaths[neighbour] = newCost to currentPath + neighbour
                frontier.remove(neighbour)
                frontier.add(neighbour)
            }
        }
    }
    val (cost, path) = bestPaths[endLocation]!!
    println(cost)
    println(path)
//    bestPaths.entries.forEach(::println)

    // Part 1
    // Correct: 696

    // Part 2
    // Correct: 2952
}
