package year2023.day5

import java.io.File
import kotlin.math.roundToInt

private typealias Mapping = Triple<Long, Long, Long>

private fun Number.simpleFormat() =
    this.toString().reversed().chunked(3).joinToString(separator = "_").reversed()

fun main() {
    val file = File("src/main/resources/2023/2023-day5.txt")
    val lines = file.readLines()

    val seeds = lines.first().split(": ")[1].split(" ").map { it.toLong() }

    val maps = lines.drop(2).split { it.isEmpty() }.associate { partition ->
        val ranges = mutableListOf<Mapping>()
        partition.drop(1).forEach { line ->
            val numbers = line.split(" ").map { it.toLong() }
            ranges.add(Mapping(numbers[0], numbers[1], numbers[2]))
        }
        val parts = partition.first().split(" ", "-to-").take(2)
        parts[0] to parts[1] to ranges
    }

    val locations = seeds
        .asSequence()
        .map { seed -> maps["seed" to "soil"]!!.resolveRange(seed) }
        .map { seed -> maps["soil" to "fertilizer"]!!.resolveRange(seed) }
        .map { seed -> maps["fertilizer" to "water"]!!.resolveRange(seed) }
        .map { seed -> maps["water" to "light"]!!.resolveRange(seed) }
        .map { seed -> maps["light" to "temperature"]!!.resolveRange(seed) }
        .map { seed -> maps["temperature" to "humidity"]!!.resolveRange(seed) }
        .map { seed -> maps["humidity" to "location"]!!.resolveRange(seed) }
        .toList()

    val result1 = locations.min()
    println("Result1: $result1")

    val result2Size = seeds.chunked(2).sumOf { it[1] }
    var currentResult2Count = 0L

    println(result2Size.simpleFormat())

    val result2 = seeds.chunked(2)
        .asSequence()
        .flatMap { it[0] until it[0] + it[1] }
        .map { seed -> maps["seed" to "soil"]!!.resolveRange(seed) }
        .map { seed -> maps["soil" to "fertilizer"]!!.resolveRange(seed) }
        .map { seed -> maps["fertilizer" to "water"]!!.resolveRange(seed) }
        .map { seed -> maps["water" to "light"]!!.resolveRange(seed) }
        .map { seed -> maps["light" to "temperature"]!!.resolveRange(seed) }
        .map { seed -> maps["temperature" to "humidity"]!!.resolveRange(seed) }
        .map { seed -> maps["humidity" to "location"]!!.resolveRange(seed) }
        .map {
            currentResult2Count++
            if (currentResult2Count % 10000000L == 0L) {
                println("Result2: ${(currentResult2Count.toDouble() / result2Size * 1000.0).roundToInt() / 10}%")
            }
            it
        }
        .min()

    println("Result2: $result2")
}

private fun List<Mapping>.resolveRange(
    seed: Long
) = firstOrNull { (_, sourceStart, range) -> seed in sourceStart until sourceStart + range }
    ?.let { (destStart, sourceStart) -> destStart + seed - sourceStart }
    ?: seed

fun <T> List<T>.split(predicate: (T) -> Boolean) = this.flatMapIndexed { index, x ->
    when {
        index == 0 || index == this.lastIndex -> listOf(index)
        predicate(x) -> listOf(index - 1, index + 1)
        else -> emptyList()
    }
}.windowed(size = 2, step = 2) { (from, to) -> this.slice(from..to) }
