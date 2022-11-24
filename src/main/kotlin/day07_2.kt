typealias NBags = Pair<Int, String>

fun parseLuggageRules2(lines: Sequence<String>): Map<String, List<NBags>> {
    return lines.map {
        val m = BAG_RULE_PATTERN.find(it)!!.groups
        val outBag = m["outer"]!!.value
        val innerPairs = BAG_INNER_RULE_PATTERN
            .findAll(m["inner"]!!.value)
            .map { mInner ->
                val n = mInner.groups["num"]!!.value.toInt()
                val inBag = mInner.groups["inner"]!!.value
                NBags(n, inBag)
            }
            .toList()
        outBag to innerPairs
    }.toMap()
}

fun countBags(graph: Map<String, List<NBags>>, nBags: NBags): Int {
    val (n, bag) = nBags
    val inners = graph.get(bag)
    val nContained = if (inners != null) {
        inners.map { countBags(graph, it) }.sum()
    } else {
        0
    }
    return (1 + nContained) * n
}

fun main() {
    val lines = generateSequence { readLine() }
    val g = parseLuggageRules2(lines)
    println(countBags(g, NBags(1, "shiny gold")) - 1)
}
