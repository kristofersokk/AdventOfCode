package year2024

import java.io.File
import java.math.BigInteger

fun main() {
    val file = File("src/main/resources/2024/2024-day11.txt")
    val lines = file.readLines()

    val stones = lines.first().trim().split(" ").map { it.toBigInteger() }.toMutableList()

    val cachedValues = mutableMapOf<Pair<BigInteger, Int>, Long>()

    fun blinkCount(stone: BigInteger, levelsLeft: Int): Long {
        if (levelsLeft == 0) {
            return 1
        }

        if (cachedValues.containsKey(stone to levelsLeft)) {
            return cachedValues[stone to levelsLeft]!!
        }

        val stoneStr = stone.toString()
        val value = when {
            stone == BigInteger.ZERO -> blinkCount(BigInteger.ONE, levelsLeft - 1)
            stoneStr.length % 2 == 0 ->
                blinkCount(stoneStr.substring(0..<stoneStr.length / 2).toBigInteger(), levelsLeft - 1) +
                    blinkCount(
                        stoneStr.substring(stoneStr.length / 2 until stoneStr.length).toBigInteger(),
                        levelsLeft - 1
                    )

            else -> blinkCount(stone.multiply(2024.toBigInteger()), levelsLeft - 1)
        }

        cachedValues[stone to levelsLeft] = value
        return value
    }

    val result1 = stones.sumOf { blinkCount(it, 25) }

    println("Part 1: $result1")

    val result2 = stones.sumOf { blinkCount(it, 75) }

    println("Part 2: $result2")
}