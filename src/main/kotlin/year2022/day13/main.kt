package year2022.day13

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day13.txt")
    val lines = file.readLines()
    val pairsOfInputs = file.readText().split("\r?\n\r?\n".toRegex())

    val correctIndices = mutableListOf<Int>()
    pairsOfInputs.forEachIndexed { index, pairOfInputs ->
        println("\"$pairOfInputs\"")
        val (first, second) = pairOfInputs.split("\r?\n".toRegex()).map { stringToDistressSignalEl(it) }
        println(first)
        println(second)
        val rightOrder = first <= second
        println("rightOrder: $rightOrder")
        if (rightOrder) {
            correctIndices.add(index)
        }

        println()
    }
    println(correctIndices)

    val result1 = correctIndices.sumOf { it + 1 }
    println("Result 1: $result1")

    val decoders = listOf("[[2]]", "[[6]]")
    val inputs = (lines + decoders).filter { it.isNotBlank() }.map { stringToDistressSignalEl(it) }.sortedBy { it }
    inputs.forEach { println(it) }
    val decoderIndices =
        decoders.map { decoderStr -> inputs.indexOfFirst { it.sourceStr == decoderStr } }.map { it + 1 }
    val result2 = decoderIndices.multiply()
    println("Result 2: $result2")

    println("Development time: 42m15s")
}

private fun Iterable<Int>.multiply(): Int {
    var result = 1
    this.forEach { result *= it }
    return result
}

private fun stringToDistressSignalEl(input: String): DistressSignalEl {
//    println("Converting \"$input\"")
    if (input.startsWith("[")) {
        val items = mutableListOf<DistressSignalEl>()
        val commaLocations = mutableListOf<Int>()
        val center = input.removePrefix("[").removeSuffix("]")
        if (center.isBlank()) {
            return DistressSignalEl(input, null, mutableListOf())
        }

        var openBrackets = 0
        center.forEachIndexed { chIndex, ch ->
            if (ch == '[') {
                openBrackets++
            } else if (ch == ']') {
                openBrackets--
            } else if (ch == ',' && openBrackets == 0) {
                commaLocations.add(chIndex)
            }
        }
        val segments = (listOf(-1) + commaLocations + listOf(center.length))
            .zipWithNext()
            .map { (start, end) -> center.substring(start + 1 until end) }
        segments.forEach { segment ->
            items.add(stringToDistressSignalEl(segment))
        }

        return DistressSignalEl(input, null, items)
    } else {
        return DistressSignalEl(input, input.toInt())
    }
}

private data class DistressSignalEl(
    val sourceStr: String,
    val intValue: Int? = null,
    val listValue: MutableList<DistressSignalEl>? = null,
) : Comparable<DistressSignalEl> {
    override fun toString(): String {
        return intValue?.toString() ?: listValue.toString()
    }

    override operator fun compareTo(other: DistressSignalEl): Int {
        return when {
            intValue != null && other.intValue != null -> intValue.compareTo(other.intValue)
            intValue != null && other.intValue == null ->
                DistressSignalEl("[$intValue]", null, mutableListOf(this)).compareTo(other)

            intValue == null && other.intValue != null ->
                compareTo(DistressSignalEl("[${other.intValue}]", null, mutableListOf(other)))

            listValue != null && other.listValue != null -> {
                val minSize = minOf(listValue.size, other.listValue.size)
                for (i in 0 until minSize) {
                    val comparison = listValue[i].compareTo(other.listValue[i])
                    if (comparison != 0) {
                        return comparison
                    }
                }
                listValue.size.compareTo(other.listValue.size)
            }

            else -> throw IllegalArgumentException("Cannot compare $this to $other")
        }
    }
}

