package year2023.day6

import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

private val BigDecimal.bi: BigInteger
    get() = this.toBigInteger()
private val Int.bi: BigInteger
    get() = BigInteger.valueOf(this.toLong())
private val BigInteger.bd: BigDecimal
    get() = BigDecimal.valueOf(this.toDouble())
private val Int.bd: BigDecimal
    get() = BigDecimal.valueOf(this.toDouble())
private val Double.bd: BigDecimal
    get() = BigDecimal.valueOf(this)

fun main() {
    val file = File("src/main/resources/2023/2023-day6.txt")
    val lines = file.readLines()

    val times = lines[0].split(" +".toRegex()).drop(1).map { it.toBigInteger() }
    val records = lines[1].split(" +".toRegex()).drop(1).map { it.toBigInteger() }

    val recordBeatingRangeSizes = times.zip(records)
        .map { (time, record) -> getRecordBeatingRange(time, record) }
        .map { it.last - it.first + 1 }

    val result1 = recordBeatingRangeSizes.multiply()

    println("Result 1: $result1")

    val time2 = lines[0].split(": +".toRegex())[1].replace(" ", "").toBigInteger()
    val record2 = lines[1].split(": +".toRegex())[1].replace(" ", "").toBigInteger()

    val recordBeatingRangeSize2 = getRecordBeatingRange(time2, record2)
        .let { it.last - it.first + 1 }

    val result2 = recordBeatingRangeSize2

    println("Result 2: $result2")
}

fun getRecordBeatingRange(timeInMs: BigInteger, previousRecord: BigInteger): LongRange {
    // times which beats the record is defined by whole number x values between x1 and x2 (both excluded) of the formula
    // y = -x^2 + Tx - R

    val discriminant = timeInMs * timeInMs - 4.bi * (-1).bi * -previousRecord
    if (discriminant < 0.bi) {
        return LongRange.EMPTY
    }
    if (discriminant == 0.bi) {
        val x = (timeInMs / 2.bi).toLong()
        return x..x
    }

    val x1 = (timeInMs.bd - discriminant.bd.sqrt(MathContext.DECIMAL128)) / 2.bd
    val x2 = (timeInMs.bd + discriminant.bd.sqrt(MathContext.DECIMAL128)) / 2.bd

    val rangeStart =
        if (x1.closeToInteger())
            x1.setScale(0, RoundingMode.HALF_UP).bi + 1.bi
        else
            x1.setScale(0, RoundingMode.CEILING).bi
    val rangeEnd =
        if (x2.closeToInteger())
            x2.setScale(0, RoundingMode.HALF_UP).bi - 1.bi
        else
            x2.setScale(0, RoundingMode.FLOOR).bi
    return rangeStart.toLong()..rangeEnd.toLong()
}

private fun BigDecimal.closeToInteger(): Boolean {
    return (this.setScale(0, RoundingMode.HALF_UP) - this).abs() < 0.0000001.bd
}

private fun Iterable<Long>.multiply(): Long {
    var result = 1L
    for (i in this) {
        result *= i
    }
    return result
}
