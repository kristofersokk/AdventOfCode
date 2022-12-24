package year2022.day20

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File("src/main/resources/2022/2022-day20.txt")
    val lines = file.readLines()

    println(lines.size)

    val initNumbers = lines.map { it.trim().toInt() }
    var list = initNumbers.toList()
    initNumbers.forEach { number ->
//        println(list)
//        println("number: $number")
        val index = list.indexOf(number)
        val simplifiedNumber = (number % (list.size - 1) + list.size - 1) % (list.size - 1)
        val offset = if (index + simplifiedNumber >= list.size) 1 else 0
        val newIndex = (index + simplifiedNumber + offset) % list.size
//        println("index: $index, newIndex: $newIndex")
        list = if (newIndex < index) {
            list.subList(0, newIndex) + number + list.subList(newIndex, index) + list.subList(index + 1, list.size)
        } else if (newIndex > index) {
            list.subList(0, index) + list.subList(index + 1, newIndex + 1) + number + list.subList(
                newIndex + 1,
                list.size
            )
        } else {
            list
        }
//        println(list)
//        println()
    }
    println(list)

    val zeroIndex = list.indexOf(0)
    val result1 = arrayOf(1000, 2000, 3000).sumOf {
        list[(zeroIndex + it) % list.size]
    }
    println("result1: $result1")

    println("Development time: 55m")
}
