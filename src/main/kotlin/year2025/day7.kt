package year2025

import java.io.File
import java.math.BigInteger

fun main() {
    val file = File("src/main/resources/2025/2025-day7.txt")
    val lines = file.readLines()
        .filterIndexed { index, _ -> index % 2 == 0 }

    val startIndex = lines.first().indexOf('S')
    val layers = mutableListOf(setOf(startIndex))
    var splitCount = 0

    lines.indices.drop(1).forEach { layerIndex ->
        val layer = mutableSetOf<Int>()
        val previousLayer = layers[layerIndex - 1]
        val line = lines[layerIndex]

        previousLayer.forEach { tachyon ->
            when (line[tachyon]) {
                '^' -> {
                    layer += tachyon - 1
                    layer += tachyon + 1
                    splitCount++
                }

                else -> layer += tachyon
            }
        }

        layers.add(layer)
    }

    val result1 = splitCount

    println("result1: $result1")

    val cache = mutableMapOf<Pair<Int, Int>, BigInteger>()
    fun countPaths(layerIndex: Int, tachyon: Int): BigInteger {
        val key = layerIndex to tachyon
        if (key in cache) {
            return cache[key]!!
        }

        val result = when {
            layerIndex >= lines.size -> BigInteger.ONE
            lines[layerIndex][tachyon] == '^' -> countPaths(layerIndex + 1, tachyon - 1) + countPaths(
                layerIndex + 1,
                tachyon + 1
            )

            else -> countPaths(layerIndex + 1, tachyon)
        }

        cache[key] = result
        return result
    }

    val result2 = countPaths(1, startIndex)

    println("result2: $result2")
}