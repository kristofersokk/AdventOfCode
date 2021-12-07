package year2021.day7

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File("src/main/resources/2021/2021-day7.txt")
    val crabs = file.readText().trim().split(",").map { it.toInt() }.sorted()

    val bestPosition1 = crabs[crabs.size / 2]
    println("mode: $bestPosition1")
    val totalFuel1 = crabs.sumOf { abs(it - bestPosition1) }
    println(totalFuel1)

    val a = (crabs.first()..crabs.last()).toList().map { iterable ->
        iterable to crabs.sumOf { (1..abs(it - iterable)).sum() }
    }.zipWithNext().map { Triple(it.second.first, it.second.second, it.second.second - it.first.second) }
    a.forEach(::println)

}
