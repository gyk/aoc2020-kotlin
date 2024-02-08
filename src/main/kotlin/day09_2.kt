fun findSeqOfSum(numbers: List<Int>, v: Int): Pair<Int, Int>? {
    var start = 0
    var end = 0
    var a = numbers[0]
    while (true) {
        if (a < v) {
            if (end >= numbers.size) {
                return null
            }
            a += numbers[++end]   
        } else if (a == v) {
            return start to end            
        } else { // a > v
            if (start >= end) {
                return null
            }
            a -= numbers[start++]
        }
    }
    return null
}

fun main() {
    val numbers = generateSequence { readLine() }.map { it.toInt() }.toList()
    val v = findInvalidBasedOnPreamble(numbers.asSequence())
    println("Value = ${findSeqOfSum(numbers, v)}")
}
