import java.util.Stack

fun calculatePlus(tokens: Iterator<Token>): Long {
    val operatorStack = Stack<Token>()
    val operandStack = Stack<Long>()

    fun frob(currOp: Token?) {
        while (operatorStack.isNotEmpty()) {
            val op = operatorStack.peek()
            if (op is Token.LeftParen) {
                if (currOp is Token.RightParen) {
                    operatorStack.pop()
                }
                return
            } else if (currOp is Token.Plus && op is Token.Times) {
                return
            }
            operatorStack.pop()
            val rhs = operandStack.pop()
            val lhs = operandStack.pop()
            operandStack.push(op(lhs, rhs))
        }
    }

    while (tokens.hasNext()) {
        when (val tk = tokens.next()) {
            is Token.Number -> operandStack.add(tk.value)
            is Token.Plus,
            is Token.Times -> {
                frob(tk)
                operatorStack.add(tk)
            }

            is Token.LeftParen -> operatorStack.add(tk)
            is Token.RightParen -> {
                frob(tk)
            }
        }
    }
    frob(null)
    return operandStack.pop()
}

fun main() {
    val res = generateSequence { readlnOrNull() }
        .sumOf { line ->
            val tokens = Regex("""\d+|\(|\)|\*|\+""").findAll(line)
                .map { Token.parse(it.value)!! }
            calculatePlus(tokens.iterator())
        }
    println("$res")
}
