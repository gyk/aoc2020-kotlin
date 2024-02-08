fun findJoltArrangement(jolts: IntArray): Pair<Int, Int> {
    jolts.sort()
    var countDiff1 = 0
    var countDiff3 = 0
    for ((a, b) in jolts.asSequence().windowed(2, 1)) {
        when (b - a) {
            1 -> countDiff1++
            3 -> countDiff3++
        }
    }
    return Pair(countDiff1, countDiff3)
}

fun main() {
    val jolts = sequenceOf(0) + generateSequence { readLine() }.map { it.toInt() }
    val (countDiff1, countDiff3) = findJoltArrangement(jolts.toList().toIntArray())
    println("${countDiff1 * (countDiff3 + 1)}")
}
