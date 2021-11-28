package year2020.day1

import java.io.File

fun main() {
    val file = File("src/main/resources/2020-day1.txt")
    val input = file.readLines()
        .filter { it.isNotEmpty() }
        .mapNotNull { it.toIntOrNull() }
    val inputSet = input.toSet()
    val result = input.first { 2020 - it in inputSet }
    println("$result * ${2020 - result} = ${result * (2020 - result)}")

    mainloop@ for (i in input.indices) {
        for (j in i + 1 until input.size) {
            val first = input[i]
            val second = input[j]
            val third = 2020 - first - second
            if (third in inputSet && third != first && third != second) {
                println("$first * $second * $third = ${first * second * third}")
                break@mainloop
            }
        }
    }

}
