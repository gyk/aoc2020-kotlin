fun main() {
    val numbers = generateSequence { readLine() }.flatMap { it.splitToSequence(' ') }.map { it.toInt() }.toList()

    val m = HashMap<Int, Int>();
    for (x in numbers) {
        m[2020 - x] = x
    }

    for (i in numbers.indices) {
        for (j in i until numbers.size) {
            val x = numbers[i]
            val y = numbers[j]
            m[x + y]?.let { z ->
                println("${x * y * z}")
                return
            }
        }
    }
}
