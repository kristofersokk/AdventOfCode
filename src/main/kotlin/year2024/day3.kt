package year2024

import java.io.File

private data class DoMatch(val index: Int, val doMatch: Boolean, val segment: String)

fun main() {
    val file = File("src/main/resources/2024/2024-day3.txt")
    val text = file.readText()

    val regexMul = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
    val result1 = regexMul.findAll(text).map {
        it.groupValues[1].toInt() * it.groupValues[2].toLong()
    }.sum()

    println("Result 1: $result1")

    val regexDoDont = "do(n't)?\\(\\)".toRegex()
    val matchResults = regexDoDont.findAll(text).toList()
    val segments = matchResults.mapIndexed { index, matchResult ->
        val segment = when (index) {
            matchResults.size - 1 -> text.substring(matchResult.range.first)
            else -> text.substring(matchResult.range.first, matchResults[index + 1].range.first)
        }
        DoMatch(matchResult.range.first, matchResult.groupValues[0] == "do()", segment)
    }.toMutableList()
    segments.addFirst(DoMatch(0, true, text.substring(0, matchResults.first().range.first)))

    val result2 = segments.filter { it.doMatch }.sumOf { segment ->
        val mulResults = regexMul.findAll(segment.segment).map {
            it.groupValues[1].toInt() * it.groupValues[2].toLong()
        }.sum()
        mulResults
    }


    println("Result 2: $result2")
}