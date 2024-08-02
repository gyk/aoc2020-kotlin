data class Bus(val id: Long, val deltaTs: Long)

// https://en.wikipedia.org/wiki/Chinese_remainder_theorem#Search_by_sieving
fun findSatisfiedTimestamp(unorderedBuses: List<Bus>): Long {
    val buses = unorderedBuses.sortedByDescending { it.id }
    var ts = 0L
    var prod = 1L
    outer@ for (bus in buses) {
        while (true) {
            if (ts % bus.id == bus.deltaTs) {
                prod *= bus.id
                continue@outer
            } else {
                ts += prod
            }
        }
    }
    return ts
}

fun main() {
    val _earliest = readlnOrNull()?.toIntOrNull()!!
    val busIds = readlnOrNull()!!
        .split(',')
        .mapIndexed { ts, id ->
            if (id == "x") {
                null
            } else {
                Bus(id.toLong(), (id.toLong() - ts.toLong()).mod(id.toLong()))
            }
        }
        .filterNotNull()
        .toList()
    val ts = findSatisfiedTimestamp(busIds)
    println("$ts")
}
