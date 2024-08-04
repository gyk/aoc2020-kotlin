sealed class Token() {
    data class Number(val value: Long) : Token()
    data object Plus : Token()
    data object Times : Token()
    data object LeftParen : Token()
    data object RightParen : Token()

    operator fun invoke(lhs: Long, rhs: Long): Long {
        return when (this) {
            is Plus -> lhs + rhs
            is Times -> lhs * rhs
            else -> throw Exception("Not an operator")
        }
    }

    companion object {
        fun parse(s: String): Token? {
            return when (s) {
                "+" -> Plus
                "*" -> Times
                "(" -> LeftParen
                ")" -> RightParen
                else -> {
                    if (s.all { char -> char.isDigit() }) {
                        Number(s.toLong())
                    } else {
                        null
                    }
                }
            }
        }
    }
}

fun calculate(tokens: Iterator<Token>): Long {
    var lhs: Long? = null
    while (tokens.hasNext()) {
        when (val tk = tokens.next()) {
            is Token.Number -> lhs = tk.value
            is Token.Plus,
            is Token.Times -> {
                val rhsValue = when (val rhs = tokens.next()) {
                    is Token.Number -> rhs.value
                    is Token.LeftParen -> calculate(tokens)
                    else -> break
                }
                lhs = tk(lhs!!, rhsValue)
            }
            is Token.LeftParen -> lhs = calculate(tokens)
            is Token.RightParen -> return lhs!!
        }
    }
    return lhs!!
}

fun main() {
    val res = generateSequence { readlnOrNull() }
        .sumOf { line ->
            val tokens = Regex("""\d+|\(|\)|\*|\+""").findAll(line)
                .map { Token.parse(it.value)!! }
            calculate(tokens.iterator())
        }
    println("$res")
}
