package year2022.day15

import kotlin.math.abs

const val WIDTH_HEIGHT = 4_000_000
const val PRINT_GRID = false

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day15.txt")
    val lines = file.readLines()

    // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    val sensors = lines.map { line ->
        val (x, y) = line.split(": ")[0].split("at ")[1].split(", ").map { it.split("=")[1].toInt() }
        val (bx, by) = line.split(": ")[1].split("at ")[1].split(", ").map { it.split("=")[1].toInt() }
        (x to y) to (bx to by)
    }

    fun sensorRangeInRow(y: Int, sensorAndBeacon: Pair<Pair<Int, Int>, Pair<Int, Int>>): IntRange? {
        val (sensor, beacon) = sensorAndBeacon
        val (sx, sy) = sensor
        val (bx, by) = beacon
        val manhattanDistance = abs(sx - bx) + abs(sy - by)
        if (y < sy - manhattanDistance || y > sy + manhattanDistance) return null
        val dx = manhattanDistance - abs(sy - y)
        return (sx - dx)..(sx + dx)
    }

    fun getGridChar(x: Int, y: Int): Char {
        sensors.forEach { (_, beacon) ->
            if (beacon.first == x && beacon.second == y) return 'B'
        }
        sensors.forEach { (sensor) ->
            if (sensor.first == x && sensor.second == y) return 'S'
        }
        sensors.forEach { sensorAndBeacon ->
            val sensorRange = sensorRangeInRow(y, sensorAndBeacon)
            if (sensorRange != null && x in sensorRange) return '#'
        }
        return '.'
    }

    fun rangeOfRow(y: Int): IntRange {
        val minX = sensors.minOf { (sensor, beacon) ->
            val sensorManhattanDistance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
            if (y in sensor.second - sensorManhattanDistance..sensor.second + sensorManhattanDistance) {
                val dx = sensorManhattanDistance - abs(y - sensor.second)
                return@minOf sensor.first - dx
            }
            Int.MAX_VALUE
        }
        val maxX = sensors.maxOf { (sensor, beacon) ->
            val sensorManhattanDistance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
            if (y in sensor.second - sensorManhattanDistance..sensor.second + sensorManhattanDistance) {
                val dx = sensorManhattanDistance - abs(y - sensor.second)
                return@maxOf sensor.first + dx
            }
            Int.MIN_VALUE
        }
        return minX..maxX
    }

    fun printGrid() {
        for (y in 0..WIDTH_HEIGHT / 2) {
            for (x in 0..WIDTH_HEIGHT / 2) {
                print(getGridChar(x, y))
            }
            println()
        }
    }

    if (PRINT_GRID) printGrid()

    val part1Row = WIDTH_HEIGHT / 2
    val rowRange = rangeOfRow(part1Row)
    val result1 = rowRange.count { getGridChar(it, part1Row) in listOf('S', '#') }

    println("Result1: $result1")

    var emptySpot: Pair<Int, Int>? = null
    (0..WIDTH_HEIGHT).forEach { y ->
        var x = 0
        while (x <= WIDTH_HEIGHT) {
            val sensorRange = sensors.asSequence().map { sensorRangeInRow(y, it) }.filterNotNull()
                .filter { x in it }
                .firstOrNull()
            if (sensorRange != null) {
                x = sensorRange.last + 1
            } else {
                emptySpot = x to y
                return@forEach
            }
        }
    }

    println("Empty spot: $emptySpot")
    val result2 = emptySpot!!.first.toLong() * WIDTH_HEIGHT + emptySpot!!.second
    println("Result2: $result2")

    println("Development time: 1h22m14s")
}
