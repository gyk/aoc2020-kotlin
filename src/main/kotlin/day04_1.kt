data class Passport(val map: Map<String, String>) {
    private val defaultMap = map.withDefault { null }

    /// Birth Year
    val byr by defaultMap

    /// Issue Year
    val iyr by defaultMap

    /// Expiration Year
    val eyr by defaultMap

    /// Height
    val hgt by defaultMap

    /// Hair Color
    val hcl by defaultMap

    /// Eye Color
    val ecl by defaultMap

    /// Passport ID
    val pid by defaultMap

    /// Country ID
    val cid by defaultMap

    fun isValid(): Boolean {
        return byr != null
                && iyr != null
                && eyr != null
                && hgt != null
                && hcl != null
                && ecl != null
                && pid != null
    }
}

fun inputPassports(): Sequence<Passport> {
    val strSeq = sequence {
        val chunk = mutableListOf<String>()
        for (line in generateSequence { readLine() } + "") {
            if (line.isBlank()) {
                if (chunk.isNotEmpty()) {
                    yield(chunk.joinToString(" "))
                    chunk.clear()
                }
            } else {
                chunk.add(line)
            }
        }
    }

    return strSeq.map {
        val passportMap = it.splitToSequence(' ').map {
            val (k, v) = it.split(':', limit = 2)
            k to v
        }.toMap()

        Passport(passportMap)
    }
}

fun main() {
    println(inputPassports().count { it.isValid() })
}
