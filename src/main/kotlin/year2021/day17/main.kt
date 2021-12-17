package year2021.day17

import java.io.File
import kotlin.math.ceil
import kotlin.math.sqrt

fun main() {
    val file = File("src/main/resources/2021/2021-day17.txt")
    val line = file.readLines().map { it.trim() }[0]
    val (xRange, yRange) = line.split(": ")[1].trim().split(", ").map {
        val ints = it.split("=")[1].split("..").map { it.toInt() }
        ints[0]..ints[1]
    }
    println(xRange)
    println(yRange)

    val minVx = ceil(-0.5 + sqrt(0.25 + 2 * xRange.first)).toInt()
    val maxVx = xRange.last
    val minVy = yRange.first
    val maxVy = -yRange.first
    val correctVelocities = (minVx..maxVx).flatMap { vx ->
        (minVy..maxVy).map { vx to it }
    }.filter { (startVx, startVy) ->
        var x = 0
        var y = 0
        var vx = startVx
        var vy = startVy
        var correct = true
        while (true) {
            x += vx
            y += vy
            vx = if (vx > 0) vx - 1 else vx
            vy--
            if (x in xRange && y in yRange) {
                break
            }
            if (x > xRange.last || y < yRange.first) {
                correct = false
                break
            }
        }
        correct
    }

    // Part 1
    val bestYVelocity = correctVelocities.maxOf { it.second }
    val highestAltitude = bestYVelocity * (bestYVelocity + 1) / 2
    println(highestAltitude)
    // Correct: 13203

    // Part 2
    println(correctVelocities)
    println(correctVelocities.size)
    // Correct: 5644
}
