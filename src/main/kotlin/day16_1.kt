fun String.parseRanges(): Pair<String, List<IntRange>> {
    val label = this.substringBefore(": ")
    val rangesStr = this.substringAfter(": ")
    return label to rangesStr
        .split(" or ")
        .map { rangeStr ->
            val fromTo = rangeStr.split("-")
            val from = fromTo[0].toInt()
            val to = fromTo[1].toInt()
            from..to
        }
}

fun String.parseTicket() = this.split(",").map { it.toInt() }

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()

    val ranges = ArrayList<IntRange>()
    for (line in lines) {
        if (line.isBlank()) {
            break
        }
        ranges.addAll(line.parseRanges().second)
    }

    require(lines.next() == "your ticket:")
    lines.next().parseTicket() // myTicket

    require(lines.next().isBlank())
    require(lines.next() == "nearby tickets:")
    var errorRate = 0
    for (line in lines) {
        val ticket = line.parseTicket()
        errorRate += ticket.filter { t -> !ranges.any { t in it } }.sum()
    }
    println("$errorRate")
}
