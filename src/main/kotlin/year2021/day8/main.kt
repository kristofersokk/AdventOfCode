package year2021.day8

import java.io.File

private data class InputLine(
    val trainData: List<String>,
    val testData: List<String>,
)

private suspend fun SequenceScope<Map<Char, Char>>.fillMapping(currentMap: Map<Char, Char>) {
    val freeKey = (setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') - currentMap.keys).first()
    val freeValues = setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') - currentMap.values.toSet()
    freeValues.forEach { freeValue ->
        val mapCopy = currentMap.toMutableMap()
        mapCopy[freeKey] = freeValue
        if (mapCopy.size == 7) {
            yield(mapCopy)
        } else {
            fillMapping(mapCopy)
        }
    }
}

private fun generateMappingSequence(): Sequence<Map<Char, Char>> =
    sequence {
        fillMapping(mapOf())
    }

private fun get7DiodeDisplayNumber(input: Set<Char>): Int? = when (input) {
    setOf('a', 'b', 'c', 'e', 'f', 'g') -> 0
    setOf('c', 'f') -> 1
    setOf('a', 'c', 'd', 'e', 'g') -> 2
    setOf('a', 'c', 'd', 'f', 'g') -> 3
    setOf('b', 'c', 'd', 'f') -> 4
    setOf('a', 'b', 'd', 'f', 'g') -> 5
    setOf('a', 'b', 'd', 'e', 'f', 'g') -> 6
    setOf('a', 'c', 'f') -> 7
    setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') -> 8
    setOf('a', 'b', 'c', 'd', 'f', 'g') -> 9
    else -> null
}

fun main() {
    val file = File("src/main/resources/2021/2021-day8.txt")
    val input = file.readLines()

    val parsedInput = input.map {
        val (train, data) = it.split(" | ").map { it.trim().split(" ") }
        InputLine(train, data)
    }

    val part1Count = parsedInput.flatMap { it.testData }
        .count { it.length in intArrayOf(2, 3, 4, 7) }
    println(part1Count)

    val part2Sum = parsedInput.sumOf { parsedLine ->
        val (trainData, testData) = parsedLine
        val correctPermutation = generateMappingSequence().first { mapping ->
            trainData.all {
                get7DiodeDisplayNumber(it.toSet().map { mapping[it]!! }.toSet()) != null
            }
        }
        val number = testData.joinToString(separator = "") {
            get7DiodeDisplayNumber(it.toSet().map { correctPermutation[it]!! }.toSet()).toString()
        }.toInt()
        number
    }
    println(part2Sum)


}
