fun computeSeatId(rc: Pair<Int, Int>): Int {
    val (r, c) = rc
    return r * 8 + c
}

fun List<Int>.binToNum(): Int {
    return this.reduce { acc, x -> acc * 2 + x }
}

fun locateSeat(s: String): Pair<Int, Int> {
    val (row, col) = s.asSequence().chunked(7).toList()

    val rowNum = row.map {
        when (it) {
            'F' -> 0
            else -> 1
        }
    }.binToNum()
    val colNum = col.map {
        when (it) {
            'L' -> 0
            else -> 1
        }
    }.binToNum()

    return Pair(rowNum, colNum)
}

fun main() {
    val lines = generateSequence { readLine() }
    println(lines.map { locateSeat(it) }.maxOf { computeSeatId(it) })
}
