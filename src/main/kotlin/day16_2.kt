fun isValid(ts: List<Int>, ranges: List<IntRange>) =
    ts.all { t -> ranges.any { t in it } }

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()

    val rangesMap = mutableMapOf<String, List<IntRange>>()
    for (line in lines) {
        if (line.isBlank()) {
            break
        }
        val (label, ranges) = line.parseRanges()
        rangesMap[label] = ranges
    }
    val allRanges = rangesMap.values.flatten()

    require(lines.next() == "your ticket:")
    val myTicket = lines.next().parseTicket()

    require(lines.next().isBlank())
    require(lines.next() == "nearby tickets:")
    val nearbyTickets = ArrayList<List<Int>>()
    for (line in lines) {
        val ticket = line.parseTicket()
        if (isValid(ticket, allRanges)) {
            nearbyTickets.add(ticket)
        }
    }

    val matchMap = mutableMapOf<Int, MutableSet<String>>()
    for (i in myTicket.indices) {
        for ((label, ranges) in rangesMap) {
            val matched = isValid(nearbyTickets.map { it[i] }, ranges)
            if (matched) {
                matchMap.getOrPut(i) { mutableSetOf() }.add(label)
            }
        }
    }

    val assignments = mutableMapOf<Int, String>()
    outer@ while (true) {
        for ((pos, labels) in matchMap) {
            if (labels.size == 1) {
                val label = labels.first()
                assignments[pos] = label
                matchMap.forEach { (_, labels) -> labels.remove(label) }
                continue@outer
            }
        }
        break
    }

    val res = assignments
        .filter { it.value.startsWith("departure") }
        .map { myTicket[it.key].toLong() }
        .reduce(Long::times)
    println("$res")
}
