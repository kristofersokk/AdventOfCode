package year2024

import java.io.File

private data class Segment(
    var value: Int,
    var length: Int
)

fun main() {
    val file = File("src/main/resources/2024/2024-day9.txt")
    val lines = file.readLines()

    val input = lines.first()

    val tape1 = run {
        val tape = input.flatMapIndexed { segmentIndex, segmentLength ->
            if (segmentIndex % 2 == 0) {
                List(segmentLength.toString().toInt()) { segmentIndex.div(2) }
            } else {
                List(segmentLength.toString().toInt()) { -1 }
            }
        }.toMutableList()

        var left = 0
        var right = tape.size - 1
        while (left < right) {
            if (tape[right] == -1) {
                right--
                continue
            }
            if (tape[left] != -1) {
                left++
                continue
            }
            tape[left] = tape[right]
            tape[right] = -1
        }

        tape
    }

    val result1 = tape1.mapIndexed { i, value ->
        if (value == -1) 0L else i.toLong() * value
    }.sum()

    println("Part 1: $result1")

    val tape2 = run {
        val tapeSegments = input.mapIndexed { segmentIndex, segmentLength ->
            if (segmentIndex % 2 == 0) {
                Segment(
                    value = segmentIndex.div(2),
                    length = segmentLength.toString().toInt()
                )
            } else {
                Segment(
                    value = -1,
                    length = segmentLength.toString().toInt()
                )
            }
        }.toMutableList()

        var left = 0
        var right = tapeSegments.size - 1
        while (right > 0) {
            if (tapeSegments[right].value == -1) {
                right--
                continue
            }
            if (tapeSegments[left].value != -1) {
                left++
                continue
            }
            if (left >= right) {
                left = 0
                right--
                continue
            }
            if (tapeSegments[left].length > tapeSegments[right].length) {
                val movedValue = tapeSegments[right].value
                val movedLength = tapeSegments[right].length
                tapeSegments[right].value = -1
                tapeSegments[left].length -= movedLength
                tapeSegments.add(left, Segment(movedValue, movedLength))
                left = 0
            } else if (tapeSegments[left].length == tapeSegments[right].length) {
                tapeSegments[left].value = tapeSegments[right].value
                tapeSegments[right].value = -1
                left = 0
            } else {
                left++
            }
        }

        tapeSegments.toList()
            .flatMap { segment -> List(segment.length) { segment.value } }
    }

    val result2 = tape2.mapIndexed { i, value ->
        if (value == -1) 0L else i.toLong() * value
    }.sum()

    println("Part 2: $result2")

}