fun findInvalidBasedOnPreamble(numbers: Sequence<Int>): Int? {
    val dq = ArrayDeque<Int>()
    val m = hashSetOf<Int>()

    for (i in numbers) {
        if (dq.size >= 25) {
            val isValid = dq.any { m.contains(i - it) }
            if (!isValid) {
                return i
            }

            m.remove(dq.removeFirst())
        }

        dq.addLast(i)
        m.add(i)
    }

    return null
}

fun main() {
    val numbers = generateSequence { readLine() }.map { it.toInt() }
    val invalid = findInvalidBasedOnPreamble(numbers)

    when (invalid) {
        null -> println("Not found")
        else -> println("$invalid")
    }
}
