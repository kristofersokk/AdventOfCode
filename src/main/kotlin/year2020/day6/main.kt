package year2020.day6

import java.io.File

fun main() {
    val file = File("src/main/resources/2020-day6.txt")
    val groups = file.readText().replace("\r", "")
        .split("\n\n")

    val result1 = groups
        .map { it.replace("\\s+".toRegex(), "") }
        .sumOf { it.toCharArray().toSet().size }
    println(result1)

    val result2 = groups
        .onEach(::println)
        .map {
            it.split("\\s+".toRegex())
                .reduce { a, b ->
                    a.toCharArray().toSet().intersect(b.toCharArray().toSet()).joinToString(separator = "")
                }
        }
        .sumOf { it.length }
    println(result2)


}