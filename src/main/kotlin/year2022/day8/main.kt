package year2022.day8

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day8.txt")
    val lines = file.readLines()

    val width = lines[0].length
    val height = lines.size
    val visible = Array(height) { BooleanArray(width) { false } }

    (0 until height).map { y ->
        var prev = -1
        (0 until width).forEach { x ->
            val current = lines[y][x].digitToInt()
            if (current > prev) {
                visible[y][x] = true
                prev = current
            }
        }
        prev = -1
        (width - 1 downTo 0).forEach { x ->
            val current = lines[y][x].digitToInt()
            if (current > prev) {
                visible[y][x] = true
                prev = current
            }
        }
    }
    (0 until width).map { x ->
        var prev = -1
        (0 until height).forEach { y ->
            val current = lines[y][x].digitToInt()
            if (current > prev) {
                visible[y][x] = true
                prev = current
            }
        }
        prev = -1
        (height - 1 downTo 0).forEach { y ->
            val current = lines[y][x].digitToInt()
            if (current > prev) {
                visible[y][x] = true
                prev = current
            }
        }
    }
//    visible.map { it.joinToString(separator = "") { if (it) "1" else "0" } }.forEach(::println)

    val result1 = visible.sumOf { it.count { it } }

    println("Result1: $result1")
    val result2 = lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            val WVis = (x - 1 downTo 0).firstOrNull { lines[y][it].digitToInt() >= c.digitToInt() }?.let { x - it } ?: x
            val EVis = (x + 1 until width).firstOrNull { lines[y][it].digitToInt() >= c.digitToInt() }?.let { it - x }
                ?: (width - 1 - x)
            val NVis = (y - 1 downTo 0).firstOrNull { lines[it][x].digitToInt() >= c.digitToInt() }?.let { y - it } ?: y
            val SVis = (y + 1 until height).firstOrNull { lines[it][x].digitToInt() >= c.digitToInt() }?.let { it - y }
                ?: (height - 1 - y)
            WVis * EVis * NVis * SVis
        }
    }.max()

    println("Result2: $result2")

    println("Development time: 45m32s")
}
