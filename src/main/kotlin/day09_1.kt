fun findInvalidBasedOnPreamble(numbers: Sequence<Long>): Long? {
    val dq = ArrayDeque<Long>()
    val m = hashSetOf<Long>()

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
    val numbers = generateSequence { readLine() }.map { it.toLong() }
    val invalid = findInvalidBasedOnPreamble(numbers)

    when (invalid) {
        null -> println("Not found")
        else -> println("$invalid")
    }
}
