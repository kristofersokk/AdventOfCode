package year2021.day24

import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt

fun alu(instructions: List<String>, inputStr: String): Map<Char, Long> {
    val variables = mutableMapOf(
        'w' to 0L,
        'x' to 0L,
        'y' to 0L,
        'z' to 0L,
    )
    var inputIndex = 0
    instructions.forEach { instruction ->
        val parts = instruction.split(" ")
        if (parts[0] == "inp") {
            variables[parts[1][0]] = inputStr[inputIndex].digitToInt().toLong()
            inputIndex++
        } else {
            val secondIsLetter = parts[2][0].isLetter()
            val a = parts[1][0]
            val aValue = variables[a]!!
            val secondValue = if (secondIsLetter) variables.getOrDefault(parts[2][0], 0L) else parts[2].toLong()
            when (parts[0]) {
                "add" -> {
                    variables[a] = aValue + secondValue
                }
                "mul" -> {
                    variables[a] = aValue * secondValue
                }
                "div" -> {
                    variables[a] = aValue / secondValue
                }
                "mod" -> {
                    variables[a] = aValue % secondValue
                }
                "eql" -> {
                    variables[a] = if (aValue == secondValue) 1 else 0
                }
            }
        }
    }
    return variables
}

fun simplifyInstructions(instructions: List<String>): Map<String, String> {
    val variables = mutableMapOf(
        "w" to "0",
        "x" to "0",
        "y" to "0",
        "z" to "0",
    )
    (0 until 14).forEach {
        variables["i$it"] = "i$it"
    }
    var inputIndex = 0
    for ((index, instruction) in instructions.withIndex()) {
//        variables.forEach(::println)
//        println()
//        println(instruction)
//        println()
//        if (index >= 10) {
//            break
//        }
        val parts = instruction.split(" ")
        if (parts[0] == "inp") {
            variables[parts[1]] = "i$inputIndex"
            inputIndex++
        } else {
            val a = parts[1]
            val aValue = variables[a]!!
            val aValueIsLong = aValue.toLongOrNull() != null
            val secondIsLetter = parts[2][0].isLetter()
            val secondValue = if (secondIsLetter) variables.getOrDefault(parts[2], "0") else parts[2]
            val secondValueIsLong = secondValue.toLongOrNull() != null
//            println(listOf(parts, secondIsLetter, aValue, aValueIsLong, secondValue, secondValueIsLong))
            when (parts[0]) {
                "add" -> {
                    if (aValueIsLong && aValue.toLong() == 0L) {
                        variables[a] = secondValue
                    } else if (secondValueIsLong && secondValue.toLong() == 0L) {
                        //nothing
                    } else if (aValueIsLong && secondValueIsLong) {
                        variables[a] = (aValue.toLong() + secondValue.toLong()).toString()
                    } else {
                        variables[a] = "${
                            if (' ' !in aValue) aValue else "(${aValue})"
                        } + ${
                            if (' ' !in secondValue) secondValue else "(${secondValue})"
                        }"
                    }
                }
                "mul" -> {
                    if (aValueIsLong && aValue.toLong() == 0L ||
                        secondValueIsLong && secondValue.toLong() == 0L
                    ) {
                        variables[a] = "0"
                    } else if (secondValueIsLong && secondValue.toLong() == 1L) {
                        //nothing
                    } else if (aValueIsLong && aValue.toLong() == 1L) {
                        variables[a] = secondValue
                    } else if (aValueIsLong && secondValueIsLong) {
                        variables[a] = (aValue.toLong() * secondValue.toLong()).toString()
                    } else {
                        variables[a] = "${
                            if (' ' !in aValue) aValue else "(${aValue})"
                        } * ${
                            if (' ' !in secondValue) secondValue else "(${secondValue})"
                        }"
                    }
                }
                "div" -> {
                    if (secondValueIsLong && secondValue.toLong() == 1L) {
                        //nothing
                    } else if (aValueIsLong && secondValueIsLong) {
                        variables[a] = (aValue.toLong() / secondValue.toLong()).toString()
                    } else {
                        variables[a] = "${
                            if (' ' !in aValue) aValue else "(${aValue})"
                        } / ${
                            if (' ' !in secondValue) secondValue else "(${secondValue})"
                        }"
                    }

                }
                "mod" -> {
                    if (secondValueIsLong && secondValue.toLong() == 1L) {
                        variables[a] = "0"
                    } else if (aValueIsLong && secondValueIsLong) {
                        variables[a] = (aValue.toLong() % secondValue.toLong()).toString()
                    } else {
                        variables[a] = "${
                            if (' ' !in aValue) aValue else "(${aValue})"
                        } % ${
                            if (' ' !in secondValue) secondValue else "(${secondValue})"
                        }"
                    }
                }
                "eql" -> {
                    if (aValueIsLong && secondValueIsLong) {
                        variables[a] = if (aValue.toLong() == secondValue.toLong()) "1" else "0"
                    } else if (aValue.matches("i\\d+".toRegex()) &&
                        secondValueIsLong &&
                        secondValue.toLong() !in 1..9
                    ) {
                        variables[a] = "0"
                    } else if (secondValue.matches("i\\d+".toRegex()) &&
                        aValueIsLong &&
                        aValue.toLong() !in 1..9
                    ) {
                        variables[a] = "0"
                    } else {
                        variables[a] = "${
                            if (' ' !in aValue) aValue else "(${aValue})"
                        } == ${
                            if (' ' !in secondValue) secondValue else "(${secondValue})"
                        }"
                    }
                }
            }
        }
    }
    return variables
}

private fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}

fun main() {
    val file = File("src/main/resources/2021/2021-day24.txt")
    val lines = file.readLines().map { it.trim() }

    // Biggest
    (1..9).flatMap { i0 ->
        (1..9).flatMap { i1 ->
            (3..9).mapNotNull { i2 ->
                val i3 = i2 - 2
                val i4 = 9
                val i5 = 9
                val i6 = 8
                val i7 = 9
                val i8 = 6
                val i9 = 9
                val i10 = 2
                val i11 = 4
                val f = (26 * (26 * i0 + i1 + 35) + (i4 + 6)) / 26
                val i12 = f % 26 - 6
                val i13 = ((f / 26) % 26) - 5
                if (i12 in 1..9 && i13 in 1..9) {
                    "$i0$i1$i2$i3$i4$i5$i6$i7$i8$i9$i10$i11$i12$i13"
                } else null
            }
        }
    }.forEach {
        println(listOf(it, calculateTheoreticalZValue(it)))
    }
    println()
    println()
    // Smallest
    (1..9).flatMap { i0 ->
        (1..9).flatMap { i1 ->
            (3..9).mapNotNull { i2 ->
                val i3 = i2 - 2
                val i4 = 6
                val i5 = 2
                val i6 = 1
                val i7 = 4
                val i8 = 1
                val i9 = 8
                val i10 = 1
                val i11 = 1
                val f = (26 * (26 * i0 + i1 + 35) + (i4 + 6)) / 26
                val i12 = f % 26 - 6
                val i13 = ((f / 26) % 26) - 5
                if (i12 in 1..9 && i13 in 1..9) {
                    "$i0$i1$i2$i3$i4$i5$i6$i7$i8$i9$i10$i11$i12$i13"
                } else null
            }
        }
    }.forEach {
        println(listOf(it, calculateTheoreticalZValue(it)))
    }
    return

    var total = 0L
    var correct = 0L
    val largestModel = (99999999999999 downTo 11111111111111).asSequence().filter {
        '0' !in it.toString()
    }.onEach {
        if (it % 100000 == 0L) {
            println(it)
        }
    }.first {
        val itString = it.toString()
//        val variables = alu(lines, it.toString())
        val z = calculateTheoreticalZValue(itString)
//        println(it)
        total++
//        if (z == variables['z']) {
//            correct++
//        } else {
//            println("Real: ${variables['z']}, theory: $z")
//        }
        if (it % 4_782_969 == 0L) {
            println(it)
            println(z)
            println()
//            println("Real: ${variables['z']}, theory: $z")
//            println("${(correct.toDouble() / total * 100.0).roundTo(2)}%")
        }
//        variables['z'] == 0L
        z == 0L
    }
    println(largestModel)


    // Part 1
    // Correct: 96979989692495

    // Part 2
    // Correct: 51316214181141
}

private fun compareLong(a: Long, b: Long) =
    if (a == b) 1L else 0L

private fun calculateTheoreticalZValue(i: String): Long {
    val iS = i.map { it.digitToInt().toLong() }
    val i0 = iS[0]
    val i1 = iS[1]
    val i2 = iS[2]
    val i3 = iS[3]
    val i4 = iS[4]
    val i5 = iS[5]
    val i6 = iS[6]
    val i7 = iS[7]
    val i8 = iS[8]
    val i9 = iS[9]
    val i10 = iS[10]
    val i11 = iS[11]
    val i12 = iS[12]
    val i13 = iS[13]
    val a = compareLong(i2 - 2, i3)
    val b = compareLong(i5 - 1, i6)
    val c = compareLong(i7 - 3, i8)
    val d = compareLong(i9 - 7, i10)
    val e = compareLong(
        if (d == 0L) i10 - 1 else
            if (c == 0L) i8 - 4 else
                if (b == 0L) i6 + 2 else
                    i4 - 5, i11
    )
    val f =
        ((26 - 25 * d) * ((26 - 25 * c) * ((26 - 25 * b) * (26 * (26 * i0 + i1 + 35) + (i4 + 6)) + (i6 + 13) * (1 - b)) + (1 - c) * (i8 + 7))
            + (1 - d) * (i10 + 10)) / 26
    val g = compareLong(if (e == 0L) i11 + 8 else (f % 26 - 6), i12)
    val h = compareLong(
        if (g == 0L) i12 + 2 else (((((26 - 25 * e) * f
            + (1 - e) * (i11 + 14)) / 26) % 26) - 5), i13
    )
    val z = (26 - 25 * h) *
        (((26 - 25 * g) *
            (((26 - 25 * e) *
                (((26 - 25 * d) *
                    ((26 - 25 * c) *
                        ((26 - 25 * b) *
                            (26 *
                                ((26 - 25 * a) *
                                    (26 * (i0 + 1) + (i1 + 9)
                                        ) + (1 - a) * (i3 + 6)
                                    ) + (i4 + 6)
                                ) + (i6 + 13) * (1 - b)
                            ) + (1 - c) * (i8 + 7)
                        ) + (1 - d) * (i10 + 10)) / 26)
                + (1 - e) * (i11 + 14)) / 26) + (1 - g) * (i12 + 7)) / 26) + (1 - h) * (i13 + 1)
    return z
}
