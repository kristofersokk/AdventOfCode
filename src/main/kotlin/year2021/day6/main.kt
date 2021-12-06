package year2021.day6

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day6.txt")
    val lines = file.readLines()

    var fishes = lines[0].split(",").map { it.toInt() }.groupBy { it }.mapValues { it.value.size.toLong() }

    println(fishes.values.sum())

    repeat(256) {
        println(fishes)
        val newFishes = mutableMapOf<Int, Long>()
        fishes.keys.filter { it > 0 }.forEach { key ->
            newFishes[key - 1] = fishes[key]!!
        }
        if (0 in fishes.keys) {
            newFishes[6] = (newFishes[6] ?: 0) + fishes[0]!!
            newFishes[8] = fishes[0]!!
        }
        fishes = newFishes
    }

    println(fishes.values.sum())

}
