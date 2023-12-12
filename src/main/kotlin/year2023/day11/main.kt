package year2023.day11

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File("src/main/resources/2023/2023-day11.txt")
    val lines = file.readLines()

    val galaxies = lines.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            if (char == '#') (x to y) else null
        }
    }

    val expandedRows = lines.mapIndexedNotNull { index, line ->
        if (line.all { it == '.' }) index else null
    }

    val expandedColumns = lines[0].indices.filter {
        lines.all { line -> line[it] == '.' }
    }.toSet()

    val result1Galaxies = galaxies.map { (x, y) ->
        val expansionsOnTheLeft = expandedColumns.count { it < x }
        val expansionsOnTheTop = expandedRows.count { it < y }
        (x + expansionsOnTheLeft) to (y + expansionsOnTheTop)
    }

    val result1 = result1Galaxies.flatMapIndexed { galaxy1Index, galaxy1 ->
        result1Galaxies.drop(galaxy1Index + 1).map { galaxy2 ->
            val (x1, y1) = galaxy1
            val (x2, y2) = galaxy2
            return@map abs(x1 - x2) + abs(y1 - y2)
        }
    }.sum()

    println("Part 1: $result1")

    val result2Galaxies = galaxies.map { (x, y) ->
        val expansionsOnTheLeft = expandedColumns.count { it < x }
        val expansionsOnTheTop = expandedRows.count { it < y }
        (x + expansionsOnTheLeft * (1_000_000 - 1)) to (y + expansionsOnTheTop * (1_000_000 - 1))
    }

    val result2 = result2Galaxies.flatMapIndexed { galaxy1Index, galaxy1 ->
        result2Galaxies.drop(galaxy1Index + 1).map { galaxy2 ->
            val (x1, y1) = galaxy1
            val (x2, y2) = galaxy2
            return@map abs(x1 - x2).toLong() + abs(y1 - y2)
        }
    }.sum()

    println("Part 2: $result2")

}
