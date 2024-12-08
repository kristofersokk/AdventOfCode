package year2024

import java.io.File
import java.math.BigInteger

private data class Equation(
    val result: BigInteger,
    val terms: List<BigInteger>
)

private fun String.toEquation(): Equation {
    val parts = trim().split(": ")
    val result = parts[0].toBigInteger()
    val terms = parts[1].split(" ").map { it.toBigInteger() }
    return Equation(result, terms)
}

private class FoundException : RuntimeException()

fun main() {
    val file = File("src/main/resources/2024/2024-day7.txt")
    val lines = file.readLines()

    val equations = lines.map { it.toEquation() }

    val result1 = equations.sumOf { equation ->
        var accum = equation.terms.first()
        fun accumulate(index: Int) {
            if (index >= equation.terms.size) {
                if (accum == equation.result) {
                    throw FoundException()
                }
                return
            }
            val term = equation.terms[index]
            accum *= term
            accumulate(index + 1)
            accum /= term
            accum += term
            accumulate(index + 1)
            accum -= term
        }

        try {
            accumulate(1)
            0.toBigInteger()
        } catch (e: FoundException) {
            equation.result
        }
    }

    println("Result 1: $result1")

    val result2 = equations.sumOf { equation ->
        var accum = equation.terms.first()
        fun accumulate(index: Int) {
            if (index >= equation.terms.size) {
                if (accum == equation.result) {
                    throw FoundException()
                }
                return
            }
            val term = equation.terms[index]
            val oldAccum = accum
            accum *= term
            accumulate(index + 1)
            accum = oldAccum
            accum += term
            accumulate(index + 1)
            accum = oldAccum
            accum = (oldAccum.toString() + term.toString()).toBigInteger()
            accumulate(index + 1)
            accum = oldAccum
        }

        try {
            accumulate(1)
            0.toBigInteger()
        } catch (e: FoundException) {
            equation.result
        }
    }

    println("Result 2: $result2")

}