package year2021.day19

import java.io.File

private data class Scanner(
    val scannedBeacons: List<List<Int>>,
) {
    var location: Triple<Int, Int, Int>? = null
    var orientation: Int? = null
    var upsideDown: Boolean? = null
}

fun main() {
    val file = File("src/main/resources/2021/2021-day19.txt")
    val scanners = file.readText().replace("\r", "").split("\n*---.{1,20}---\n".toRegex())
        .filter { it.isNotBlank() }.map {
            Scanner(it.split("\n").filter { it.isNotBlank() }.map { it.split(",").map { it.toInt() } })
        }
    scanners.forEach(::println)
    scanners[0].location = Triple(0, 0, 0)
    scanners[0].orientation = 0
    scanners[0].upsideDown = false


    // Part 1
    // Correct:

    // Part 2
    // Correct:
}
