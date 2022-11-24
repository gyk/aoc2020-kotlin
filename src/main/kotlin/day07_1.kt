val BAG_RULE_PATTERN = Regex("""^(?<outer>\w+ \w+) bags contain (?<inner>.+)$""")
val BAG_INNER_RULE_PATTERN = Regex("""((?<num>\d+) (?<inner>\w+ \w+) bags?[,. ]+)""")

fun parseLuggageRules(lines: Sequence<String>): Map<String, List<String>> {
    val graph = mutableMapOf<String, MutableList<String>>()
    lines.forEach {
        val m = BAG_RULE_PATTERN.find(it)!!.groups
        val outBag = m["outer"]!!.value
        val inBags = BAG_INNER_RULE_PATTERN
            .findAll(m["inner"]!!.value)
            .map { mInner -> mInner.groups["inner"]!!.value }
            .toList()

        for (inBag in inBags) {
            val outBags = graph.getOrPut(inBag) { mutableListOf() }
            outBags.add(outBag)
        }
    }
    return graph
}

fun listOutermostColors(graph: Map<String, List<String>>): Set<String> {
    val results = mutableSetOf<String>()
    val q = ArrayDeque<String>()
    q.addLast("shiny gold")
    while (q.isNotEmpty()) {
        val head = q.removeFirst()
        val outers = graph.get(head)
        if (outers != null) {
            results.addAll(outers)
            q.addAll(outers)
        }
    }
    return results
}

fun main() {
    val lines = generateSequence { readLine() }
    val g = parseLuggageRules(lines)
    println(listOutermostColors(g).size)
}
