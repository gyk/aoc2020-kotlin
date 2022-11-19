fun main() {
    val numbers = generateSequence { readLine() }.flatMap { it.splitToSequence(' ') }.map { it.toInt() }

    val m = HashMap<Int, Int>()
    for (x in numbers) {
        m[x]?.let {
            println("${it * x}")
            return
        }
        m[2020 - x] = x
    }
}
