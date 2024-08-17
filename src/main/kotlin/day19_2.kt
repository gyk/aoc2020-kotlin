fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()
    val ruleMap = mutableMapOf<Int, Rule>()
    for (line in lines) {
        if (line.isBlank()) {
            break
        }

        // Manually eliminate left recursions and factor the common prefix
        val lineOverridden = when (line.trim()) {
            "0: 8 11" -> "0: 42 8"
            "8: 42" -> "8: 11 | 42 8"
            "11: 42 31" -> "11: 42 11 31 | 42 31"
            else -> line
        }

        val (id, rule) = Rule.parse(lineOverridden)!!
        ruleMap[id] = rule
    }

    val cnt = lines.asSequence().count { line ->
        val res = Parser(ruleMap, line).matched(0)
        res
    }
    println("$cnt")
}
