package year2022.day25

import java.io.File
import java.math.BigInteger

fun main() {
    val file = File("src/main/resources/2022/2022-day25.txt")
    val lines = file.readLines()

    fun analogToDecimal(analog: String): BigInteger {
        var result = BigInteger.ZERO
        analog.trim().reversed().forEachIndexed { index, c ->
            result += BigInteger.valueOf(5).pow(index) * BigInteger.valueOf(when (c) {
                '2' -> 2
                '1' -> 1
                '0' -> 0
                '-' -> -1
                '=' -> -2
                else -> throw IllegalArgumentException("Invalid character: $c")
            })
        }
        return result
    }

    fun digitalToAnalog(digital: BigInteger): String {
        val values = digital.toString(5).reversed().map { it.digitToInt() }.toMutableList()
        var index = 0
        while (index < values.size) {
            if (values[index] > 2) {
                values[index] -= 5
                if (index == values.size - 1) {
                    values.add(1)
                } else {
                    values[index + 1] += 1
                }
            }
            index++
        }
        return values.reversed().joinToString("") {
            when (it) {
                -2 -> "="
                -1 -> "-"
                0 -> "0"
                1 -> "1"
                2 -> "2"
                else -> throw IllegalArgumentException("Invalid value: $it")
            }
        }
    }

    val decimals = lines.map { analogToDecimal(it).also { println(it) } }

    val sum = decimals.sumOf { it }
    println("Sum: $sum")
    println("Analog sum: ${digitalToAnalog(sum)}")

}
