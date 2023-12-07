package year2023.day7

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day7.txt")
    val lines = file.readLines()

    val hands = lines.map { line ->
        val (cards, bid) = line.split(" ")
        Hand(cards, bid.toInt())
    }

    val result1 = hands.sortedWith(
        compareBy(
            ::getHandRank,
            ::getHandNumericValue
        )
    )
        .mapIndexed { handIndex, hand -> hand.bid * (handIndex + 1) }
        .sum()

    println("Part 1: $result1")

    val result2 = hands.sortedWith(
        compareBy(
            ::getHandRankWithJokerReplacement,
            ::getHandNumericValueWithJoker
        )
    )
        .mapIndexed { handIndex, hand -> hand.bid * (handIndex + 1) }
        .sum()

    println("Part 2: $result2")
}

private data class Hand(
    val cards: String,
    val bid: Int
)

const val CARD_VALUES = "23456789TJQKA"
const val CARD_VALUES2 = "J23456789TQKA"

private fun getHandNumericValueWithJoker(hand: Hand) =
    getHandNumericValue(hand, CARD_VALUES2)

private fun getHandNumericValue(hand: Hand, cardValues: String = CARD_VALUES): Long {
    fun getCharValue(char: Char) = cardValues.indexOf(char).toLong()
    fun getStringValue(string: String): Long =
        if (string == "") 0 else getStringValue(string.dropLast(1)) * cardValues.length + getCharValue(string.last())
    return getStringValue(hand.cards)
}

private fun getHandRankWithJokerReplacement(hand: Hand): Int {
    if ('J' !in hand.cards) {
        return getHandRank(hand)
    }

    return CARD_VALUES.maxOf { jokerReplaceChar ->
        getHandRank(Hand(hand.cards.replace('J', jokerReplaceChar), 0))
    }
}

private fun getHandRank(hand: Hand): Int {
    val groupSizes = hand.cards.groupBy { it }.values.map { it.size }

    return when {
        5 in groupSizes -> 7
        4 in groupSizes -> 6
        3 in groupSizes && 2 in groupSizes -> 5
        3 in groupSizes -> 4
        groupSizes.count { it == 2 } == 2 -> 3
        2 in groupSizes -> 2
        else -> 1
    }
}
