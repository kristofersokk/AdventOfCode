package year2023.day5

import java.io.File
import kotlin.math.roundToInt

private typealias Mapping = Triple<Long, Long, Long>

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

    val seedToSoilMap = maps["seed" to "soil"]!!
    val soilToFertilizerMap = maps["soil" to "fertilizer"]!!
    val fertilizerToWaterMap = maps["fertilizer" to "water"]!!
    val waterToLightMap = maps["water" to "light"]!!
    val lightToTemperatureMap = maps["light" to "temperature"]!!
    val temperatureToHumidityMap = maps["temperature" to "humidity"]!!
    val humidityToLocationMap = maps["humidity" to "location"]!!

    val locations = seeds
        .asSequence()
        .map { seed -> seedToSoilMap.resolveRange(seed) }
        .map { seed -> soilToFertilizerMap.resolveRange(seed) }
        .map { seed -> fertilizerToWaterMap.resolveRange(seed) }
        .map { seed -> waterToLightMap.resolveRange(seed) }
        .map { seed -> lightToTemperatureMap.resolveRange(seed) }
        .map { seed -> temperatureToHumidityMap.resolveRange(seed) }
        .map { seed -> humidityToLocationMap.resolveRange(seed) }
        .toList()

    val result1 = locations.min()
    println("Result1: $result1")

    val result2Size = seeds.chunked(2).sumOf { it[1] }
    var currentResult2Count = 0L

    println()
    println(result2Size.simpleFormat() + " possible seeds")

    val result2 = seeds.chunked(2)
        .asSequence()
        .flatMap { it[0] until it[0] + it[1] }
        .map { seed -> seedToSoilMap.resolveRange(seed) }
        .map { seed -> soilToFertilizerMap.resolveRange(seed) }
        .map { seed -> fertilizerToWaterMap.resolveRange(seed) }
        .map { seed -> waterToLightMap.resolveRange(seed) }
        .map { seed -> lightToTemperatureMap.resolveRange(seed) }
        .map { seed -> temperatureToHumidityMap.resolveRange(seed) }
        .map { seed -> humidityToLocationMap.resolveRange(seed) }
        .map {
            currentResult2Count++
            if (currentResult2Count % 100000L == 0L) {
                print("Result2: ${(currentResult2Count.toDouble() / result2Size * 1000.0).roundToInt() / 10.0}%\r")
            }
            it
        }
        .min()

    println("Result2: $result2")
}

private fun Number.simpleFormat() =
    this.toString().reversed().chunked(3).joinToString(separator = "_").reversed()

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
