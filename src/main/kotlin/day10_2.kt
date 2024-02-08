fun findCountOfJoltArrangements(jolts: Sequence<Int>): Long {
    val jolts = (sequenceOf(0) + jolts + sequenceOf(Int.MAX_VALUE))
        .toList()
        .toIntArray()
    jolts.sort()
    jolts[jolts.size - 1] = jolts[jolts.size - 2] + 3

    val states = Array<Long>(jolts.size) { 0 }
    states[0] = 1
    for (i in 1..<jolts.size) {
        for (j in (i - 1) downTo 0) {
            if (jolts[i] - jolts[j] <= 3) {
                states[i] += states[j]
            }
        }
    }
    return states[jolts.size - 1]
}

fun main() {
    val jolts = generateSequence { readLine() }.map { it.toInt() }
    val count = findCountOfJoltArrangements(jolts)
    println("$count")
}
