package year2024

import java.io.File
import kotlin.math.abs

private data class Antenna(
    val type: Char,
    val location: Location
)

private data class Location(
    val x: Int,
    val y: Int,
)

private data class AntiNode(
    val type: Char,
    val location: Location,
    val resonant: Boolean = false,
)

fun main() {
    val file = File("src/main/resources/2024/2024-day8.txt")
    val lines = file.readLines()

    val allAntennas = lines.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c != '.') Antenna(c, Location(x, y)) else null
        }
    }
    val antennasByType = allAntennas.groupBy { it.type }
    val antennaTypes = antennasByType.keys

    val width = lines[0].length
    val height = lines.size

    val antiNodesPerAntenna = antennaTypes.associateWith { type ->
        val antennas = antennasByType[type]!!
        val antiNodes = mutableSetOf<AntiNode>()
        for (i1 in 0..<antennas.size - 1) {
            for (i2 in i1 + 1..<antennas.size) {
                val antenna1 = antennas[i1]
                val antenna2 = antennas[i2]
                val dx = antenna2.location.x - antenna1.location.x
                val dy = antenna2.location.y - antenna1.location.y
                val newAntiNodes = mutableListOf<AntiNode>()

                var multiplier = 0
                while (antenna1.location.x + dx * multiplier in 0 until width && antenna1.location.y + dy * multiplier in 0 until height) {
                    val x = antenna1.location.x + dx * multiplier
                    val y = antenna1.location.y + dy * multiplier
                    newAntiNodes.add(AntiNode(type, Location(x, y), abs(multiplier) == 1))
                    multiplier--
                }
                multiplier = 0
                while (antenna2.location.x + dx * multiplier in 0 until width && antenna2.location.y + dy * multiplier in 0 until height) {
                    val x = antenna2.location.x + dx * multiplier
                    val y = antenna2.location.y + dy * multiplier
                    newAntiNodes.add(AntiNode(type, Location(x, y), abs(multiplier) == 1))
                    multiplier++
                }

                antiNodes.addAll(newAntiNodes.toList())
            }
        }
        antiNodes
    }

    val result1 = antiNodesPerAntenna.values.flatten()
        .filter { it.resonant }
        .map { it.location }
        .toSet()
        .size

    println("Result 1: $result1")

    val result2 = antiNodesPerAntenna.values.flatten()
        .map { it.location }
        .toSet()
        .size

    println("Result 2: $result2")
}