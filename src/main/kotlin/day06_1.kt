fun inputForms(): Sequence<String> {
    return sequence {
        val chunk = mutableListOf<String>()
        for (line in generateSequence { readLine() } + "") {
            if (line.isBlank()) {
                if (chunk.isNotEmpty()) {
                    yield(chunk.joinToString("\n"))
                    chunk.clear()
                }
            } else {
                chunk.add(line)
            }
        }
    }
}

fun main() {
    val formSeq = inputForms()
    println(formSeq.map {
        it.asSequence().toSet().size
    }.sum())
}
