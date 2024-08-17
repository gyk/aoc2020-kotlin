sealed class Rule {
    data class Terminal(val char: Char) : Rule()
    data class Concat(val subrules: List<Int>) : Rule()
    data class Alternative(val subrules: List<Rule>) : Rule()

    companion object {
        private fun parseR(ruleStr: String): Rule {
            Regex("\"([a-z])\"").find(ruleStr)?.let { m ->
                val ch = m.groupValues[1][0]
                return Terminal(ch)
            }

            val orClauses = ruleStr.split(Regex("""\s?\|\s?"""))
            if (orClauses.size > 1) {
                val subrules = orClauses.map { parseR(it) }
                return Alternative(subrules)
            }

            require(Regex("""\d+(\s+\d+)*""").matches(ruleStr))
            val concatClauses = ruleStr.split(Regex("\\s+"))
            val subrules = concatClauses.map { it.toInt() }
            return Concat(subrules)
        }

        fun parse(s: String): Pair<Int, Rule>? {
            val ss = s.split(": ", ignoreCase = false, limit = 2)
            val id = ss[0].toIntOrNull() ?: return null
            val ruleStr = ss.getOrNull(1) ?: return null
            val rule = parseR(ruleStr) ?: return null
            return id to rule
        }
    }
}

class Parser(val rules: Map<Int, Rule>, val input: String) {
    var cursor = 0
    fun matched(ruleId: Int): Boolean {
        return matchedBy(ruleId) && cursor == input.length
    }

    private fun matchedBy(ruleId: Int): Boolean {
        return matchedBy(rules[ruleId] ?: return false)
    }

    private fun matchedBy(rule: Rule): Boolean {
        when (rule) {
            is Rule.Terminal -> {
                if (input.getOrNull(cursor) == rule.char) {
                    cursor++
                    return true
                } else {
                    return false
                }
            }

            // Assuming there are no common prefixes
            is Rule.Alternative -> {
                for (subrule in rule.subrules) {
                    if (matchedBy(subrule)) {
                        return true
                    }
                }
                return false
            }

            is Rule.Concat -> {
                val oldCursor = cursor
                for (subrule in rule.subrules) {
                    if (!matchedBy(subrule)) {
                        cursor = oldCursor
                        return false
                    }
                }
                return true
            }
        }
    }

}

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()
    val ruleMap = mutableMapOf<Int, Rule>()
    for (line in lines) {
        if (line.isBlank()) {
            break
        }

        val (id, rule) = Rule.parse(line)!!
        ruleMap[id] = rule
    }

    val cnt = lines.asSequence().count { line ->
        Parser(ruleMap, line).matched(0)
    }
    println("$cnt")
}
