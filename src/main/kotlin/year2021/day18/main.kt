package year2021.day18

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

data class SnailfishNumber(
    var literalLeft: Int? = null,
    var literalRight: Int? = null,
    var leftSubNumber: SnailfishNumber? = null,
    var rightSubNumber: SnailfishNumber? = null,
    var parent: SnailfishNumber? = null
) {

    private fun copy(): SnailfishNumber {
        val leftCopy = leftSubNumber?.copy()
        val rightCopy = rightSubNumber?.copy()
        val newCopy = SnailfishNumber(literalLeft, literalRight, leftCopy, rightCopy)
        leftCopy?.parent = newCopy
        rightCopy?.parent = newCopy
        return newCopy
    }

    val magnitude: Int
        get() = 3 * (leftSubNumber?.magnitude ?: literalLeft ?: 0) +
            2 * (rightSubNumber?.magnitude ?: literalRight ?: 0)

    operator fun plus(other: SnailfishNumber): SnailfishNumber {
        val newNumber = SnailfishNumber(null, null, this.copy(), other.copy(), null)
        newNumber.leftSubNumber?.parent = newNumber
        newNumber.rightSubNumber?.parent = newNumber
        newNumber.reduce()
        return newNumber
    }

    fun explode(): Boolean {
        val explodable = getSubNumbersSequence().firstOrNull { it.first >= 4 }?.second
        explodable?.let {
            val leftValue = it.literalLeft!!
            val rightValue = it.literalRight!!
            val parent = it.parent!!

            // Propagate leftValue to left
            var u = it
            var p: SnailfishNumber? = parent
            while (p != null) {
                if (u === p.rightSubNumber) {
                    if (p.literalLeft != null) {
                        p.literalLeft = p.literalLeft?.plus(leftValue)
                    }
                    if (p.leftSubNumber != null) {
                        u = p.leftSubNumber!!
                        while (u.rightSubNumber != null) {
                            u = u.rightSubNumber!!
                        }
                        u.literalRight = u.literalRight?.plus(leftValue)
                    }
                    break
                }
                u = p
                p = p.parent
            }

            // Propagate rightValue to right
            u = it
            p = parent
            while (p != null) {
                if (u === p.leftSubNumber) {
                    if (p.literalRight != null) {
                        p.literalRight = p.literalRight?.plus(rightValue)
                    }
                    if (p.rightSubNumber != null) {
                        u = p.rightSubNumber!!
                        while (u.leftSubNumber != null) {
                            u = u.leftSubNumber!!
                        }
                        u.literalLeft = u.literalLeft?.plus(rightValue)
                    }
                    break
                }
                u = p
                p = p.parent
            }

            // Delete links, replace with 0
            if (parent.leftSubNumber === it) {
                parent.leftSubNumber = null
                parent.literalLeft = 0
            }
            if (parent.rightSubNumber === it) {
                parent.rightSubNumber = null
                parent.literalRight = 0
            }
            it.parent = null
            return true
        }
        return false
    }

    fun getSubNumbersSequence(level: Int = 0): Sequence<Pair<Int, SnailfishNumber>> = sequence {
        leftSubNumber?.let { yieldAll(it.getSubNumbersSequence(level + 1)) }
        yield(level to this@SnailfishNumber)
        rightSubNumber?.let { yieldAll(it.getSubNumbersSequence(level + 1)) }
    }

    fun split(): Boolean {
        val leftValue = literalLeft
        if (leftValue != null && leftValue > 9) {
            leftSubNumber =
                SnailfishNumber(floor(leftValue / 2.0).toInt(), ceil(leftValue / 2.0).toInt(), null, null, this)
            literalLeft = null
            return true
        }
        if (leftSubNumber != null && leftSubNumber!!.split()) {
            return true
        }
        val rightValue = literalRight
        if (rightValue != null && rightValue > 9) {
            rightSubNumber =
                SnailfishNumber(floor(rightValue / 2.0).toInt(), ceil(rightValue / 2.0).toInt(), null, null, this)
            literalRight = null
            return true
        }
        if (rightSubNumber != null && rightSubNumber!!.split()) {
            return true
        }
        return false
    }

    fun reduce() {
        while (true) {
            val exploded = explode()
            if (exploded) {
                continue
            }
            val splitted = split()
            if (splitted) {
                continue
            }
            break
        }
    }

    override fun toString() =
        "[${
            if (literalLeft == null) "" else literalLeft.toString()
        }${
            if (leftSubNumber == null) "" else leftSubNumber.toString()
        },${
            if (literalRight == null) "" else literalRight.toString()
        }${
            if (rightSubNumber == null) "" else rightSubNumber.toString()
        }]"

    companion object {
        fun fromString(inputStr: String): Pair<SnailfishNumber, String> {
            var str = inputStr.drop(1) // remove '['
            var literalLeft: Int? = null
            var literalRight: Int? = null
            var leftSubNumber: SnailfishNumber? = null
            var rightSubNumber: SnailfishNumber? = null
            if (str[0] == '[') {
                val (subNumber, restString) = fromString(str)
                leftSubNumber = subNumber
                str = restString
            } else {
                val numberStr = "\\d+".toRegex().find(str)!!.value
                literalLeft = numberStr.toInt()
                str = str.removePrefix(numberStr)
            }
            str = str.drop(1) // remove ','
            if (str[0] == '[') {
                val (subNumber, restString) = fromString(str)
                rightSubNumber = subNumber
                str = restString
            } else {
                val numberStr = "\\d+".toRegex().find(str)!!.value
                literalRight = numberStr.toInt()
                str = str.removePrefix(numberStr)
            }
            str = str.drop(1) // remove ']'
            val snailfishNumber = SnailfishNumber(
                literalLeft, literalRight, leftSubNumber, rightSubNumber
            )
            leftSubNumber?.parent = snailfishNumber
            rightSubNumber?.parent = snailfishNumber
            return snailfishNumber to str
        }
    }
}

fun main() {
    val file = File("src/main/resources/2021/2021-day18.txt")
    val lines = file.readLines().map { it.trim() }
    val snailfishNumbers = lines.map { SnailfishNumber.fromString(it).first }
    val sum = snailfishNumbers.reduce(SnailfishNumber::plus)
    println("Part 1: ${sum.magnitude}")

    val biggestMagnitude = snailfishNumbers.maxOf { first ->
        snailfishNumbers.filter { it !== first }.maxOf { (first + it).magnitude }
    }
    println("Part 2: $biggestMagnitude")

    // Part 1
    // Correct: 4116

    // Part 2
    // Correct: 4638
}
