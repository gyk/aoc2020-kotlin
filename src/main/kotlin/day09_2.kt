fun findSeqOfSum(numbers: List<Long>, v: Long): Pair<Int, Int>? {
    var start = 0
    var end = 0
    var a = numbers[0]
    while (true) {
        if (a < v) {
            if (++end >= numbers.size) {
                return null
            }
            a += numbers[end]
        } else if (a == v) {
            return start to end
        } else { // a > v
            if (start >= end) {
                return null
            }
            a -= numbers[start++]
        }
    }
}

fun main() {
    val numbers = generateSequence { readLine() }.map { it.toLong() }.toList()
    val v = findInvalidBasedOnPreamble(numbers.asSequence())
    val (from, to) = findSeqOfSum(numbers, v!!)!!
    val xs = numbers.subList(from, to)
    println("Value = ${xs.min() + xs.max()}")
}
