package year2020.day4

import java.io.File

fun main() {
    val file = File("src/main/resources/2020-day4.txt")
    val fileStr = file.readText()
    val requiredFields = arrayOf<Pair<String, (String) -> Boolean>>(
        "byr" to { it.matches("\\d{4}".toRegex()) && it.toInt() in 1920..2002 },
        "iyr" to { it.matches("\\d{4}".toRegex()) && it.toInt() in 2010..2020 },
        "eyr" to { it.matches("\\d{4}".toRegex()) && it.toInt() in 2020..2030 },
        "hgt" to {
            it.matches("\\d{2,3}cm".toRegex()) && it.split("cm")[0].toInt() in 150..193 ||
                it.matches("\\d{2,3}in".toRegex()) && it.split("in")[0].toInt() in 59..76
        },
        "hcl" to { it.matches("#[0-9a-f]{6}".toRegex()) },
        "ecl" to { it in arrayOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") },
        "pid" to { it.matches("\\d{9}".toRegex()) }
    )

    val passports = fileStr.replace("\r", "").split("\n{2,}".toRegex()).map { passportStr ->
        passportStr.split("[ \r\n]+".toRegex()).map { it.split(":") }
    }
    println(passports.size)
    println(passports)

    val validPassports1 = passports.count { passport ->
        requiredFields.map { it.first }
            .all { requiredField -> requiredField in passport.map { it[0] } }
    }
    println(validPassports1)

    val validPassports2 = passports.count { passport ->
        requiredFields
            .all { requiredField -> passport.any { it[0] == requiredField.first && requiredField.second.invoke(it[1]) } }
    }
    println(validPassports2)

}