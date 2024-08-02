fun recite(starting: List<Int>, n: Int): Int {
    val lastSeenMap = HashMap<Int, Int>()
    val endIndex = starting.size - 1
    for (i in 0..<endIndex) {
        lastSeenMap[starting[i]] = i
    }

    var last = starting[endIndex]
    for (i in endIndex..<(n - 1)) {
        val lastSeen = lastSeenMap.put(last, i)
        last = lastSeen?.let { i - it } ?: 0
    }
    return last
}

fun main() {
    val startingNumbers = readlnOrNull()!!
        .split(',')
        .map { it.toInt() }
    println("${recite(startingNumbers, 2020)}")
}
