fun findTimestamp(earliest: Int, busIds: List<Int>): Pair<Int, Int> {
    return generateSequence(earliest) { it + 1 }
        .map { ts ->
            for (id in busIds) {
                if (ts % id == 0) {
                    return@map ts to id
                }
            }
            null
        }
        .filterNotNull()
        .first()
}

fun main() {
    val earliest = readlnOrNull()?.toIntOrNull()!!
    val busIds = readlnOrNull()!!
        .split(',')
        .filter { it != "x" }
        .map { it.toInt() }
        .toList()
    val (ts, id) = findTimestamp(earliest, busIds)
    println("${(ts - earliest) * id}")
}
