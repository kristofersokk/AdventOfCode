package year2023.day13

import java.io.File
import kotlin.math.min

fun main() {
    val file = File("src/main/resources/2023/2023-day13.txt")
    val text = file.readText()

    val patterns = text.split("\r?\n\r?\n".toRegex()).map { it.split("\r?\n".toRegex()) }

    val result1 = findSumOfPatternValues(patterns, ::findPossibleHorizontalMirror)

    println("Part 1: $result1")

    val result2 = findSumOfPatternValues(patterns, ::findPossibleHorizontalMirrorWithSmudge)

    println("Part 2: $result2")

}

private fun findSumOfPatternValues(
    patterns: List<List<String>>,
    mirrorDetector: (List<String>) -> Int?
) = patterns.sumOf { pattern ->
    val horizontalMirror = mirrorDetector(pattern)
    if (horizontalMirror != null)
        return@sumOf 100L * (horizontalMirror + 1)

    val transposedPattern = pattern.first().indices.map { index ->
        pattern.map { it[index] }.joinToString("")
    }
    val verticalMirror = mirrorDetector(transposedPattern)
    if (verticalMirror != null)
        return@sumOf verticalMirror.toLong() + 1
    0L
}

private fun findPossibleHorizontalMirror(pattern: List<String>): Int? {
    val lineHashes = pattern.map { it.hashCode() }
    val verticalMirror = (0 until lineHashes.size - 1).firstNotNullOfOrNull { index ->
        val reflectionWidth = min(
            index + 1, lineHashes.size - index - 1
        )
        val isReflection = (1..reflectionWidth).all { width ->
            lineHashes[index - width + 1] == lineHashes[index + width]
        }
        if (isReflection)
            index
        else
            null
    }
    return verticalMirror
}

private fun findPossibleHorizontalMirrorWithSmudge(pattern: List<String>): Int? {
    val lineHashes = pattern.map { it.hashCode() }
    val verticalMirror = (0 until lineHashes.size - 1).firstNotNullOfOrNull { index ->
        val reflectionWidth = min(
            index + 1,
            lineHashes.size - index - 1
        )
        val nonReflectedLineWidths = (1..reflectionWidth).filter { width ->
            lineHashes[index - width + 1] != lineHashes[index + width]
        }
        if (nonReflectedLineWidths.size != 1)
            return@firstNotNullOfOrNull null
        val nonReflectedLineWidth = nonReflectedLineWidths.first()
        val line1 = pattern[index - nonReflectedLineWidth + 1]
        val line2 = pattern[index + nonReflectedLineWidth]
        val mismatchCount = line1.indices.count { line1[it] != line2[it] }
        if (mismatchCount == 1)
            index
        else
            null
    }
    return verticalMirror
}
