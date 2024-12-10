package year2024

import java.io.File

fun main() {
    val file = File("src/main/resources/2024/2024-day10.txt")
    val lines = file.readLines()

    val width = lines[0].length
    val height = lines.size

    data class Location(val x: Int, val y: Int)

    data class Trail(
        val source: Location,
        val destination: Location,
    )

    val trailsByLatitude = mutableListOf<List<Trail>>()

    val firstLocations = mutableListOf<Trail>()
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '0') {
                firstLocations.add(Trail(Location(x, y), Location(x, y)))
            }
        }
    }
    trailsByLatitude.add(firstLocations)

    while (trailsByLatitude.last().isNotEmpty()) {
        val lastTrails = trailsByLatitude.last()
        val newTrails = mutableListOf<Trail>()
        val newLevel = trailsByLatitude.size
        if (newLevel > 9) {
            break
        }
        lastTrails.forEach { trail ->
            val (x, y) = trail.destination
            if (x > 0 && lines[y][x - 1] == newLevel.digitToChar()) {
                newTrails.add(Trail(trail.source, Location(x - 1, y)))
            }
            if (x < width - 1 && lines[y][x + 1] == newLevel.digitToChar()) {
                newTrails.add(Trail(trail.source, Location(x + 1, y)))
            }
            if (y > 0 && lines[y - 1][x] == newLevel.digitToChar()) {
                newTrails.add(Trail(trail.source, Location(x, y - 1)))
            }
            if (y < height - 1 && lines[y + 1][x] == newLevel.digitToChar()) {
                newTrails.add(Trail(trail.source, Location(x, y + 1)))
            }
        }
        trailsByLatitude.add(newTrails)
    }

    val countedTrails = trailsByLatitude.mapIndexed { latitude, trails ->
        latitude to trails.groupBy { it.source }.mapValues { it.value.groupBy { it.destination }.size }
    }.toMap()

    val result1 = countedTrails[9]!!.values.sum()

    println("Result 1: $result1")

    val ratedTrails = trailsByLatitude.mapIndexed { latitude, trails ->
        latitude to trails.groupBy { it.source }.mapValues { it.value.size }
    }.toMap()

    val result2 = ratedTrails[9]!!.values.sum()

    println("Result 2: $result2")

}