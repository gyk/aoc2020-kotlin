val HEIGHT_PATTERN = Regex("^(\\d+)(cm|in)$")
val HAIR_COLOR_PATTERN = Regex("^#[0-9a-f]{6}$")
val PASSPORT_ID_PATTERN = Regex("^\\d{9}$")
val EYE_COLOR_SET = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

fun Passport.isValidStrict(): Boolean {
    return isValid() &&
            (byr!!.toIntOrNull() in 1920..2002) &&
            (iyr!!.toIntOrNull() in 2010..2020) &&
            (eyr!!.toIntOrNull() in 2020..2030) &&
            (HEIGHT_PATTERN.find(hgt!!)?.groupValues?.drop(1)?.let {
                val (heightNum, measure) = it
                when (measure) {
                    "cm" -> heightNum.toInt() in 150..193
                    "in" -> heightNum.toInt() in 59..76
                    else -> false
                }
            } ?: false) &&
            (HAIR_COLOR_PATTERN.matches(hcl!!)) &&
            (ecl in EYE_COLOR_SET) &&
            (PASSPORT_ID_PATTERN.matches(pid!!))
}

fun main() {
    println(inputPassports().count { it.isValidStrict() })
}
