fun arrangeCups2(cups: Array<Int>, nMoves: Int) {
    var curr = cups[0]
    repeat(nMoves) {
        val pickedUp = cups.removeRange(curr, 3)
        var dest = curr - 1
        while (true) {
            if (dest == 0) {
                dest = (cups.size - 1 downTo 1).first { it !in pickedUp }
                break
            } else {
                if (dest !in pickedUp) {
                    break
                }
                dest--
            }
        }

        cups.insertAt(dest, pickedUp.first(), pickedUp.last())
        curr = cups[curr]
    }
}

fun Array<Int>.removeRange(i: Int, n: Int): List<Int> {
    val removed = mutableListOf<Int>()
    repeat(n) {
        removed += this[i]
        this[i] = this[this[i]]
    }
    this[removed[removed.lastIndex]] = -1
    return removed
}

fun Array<Int>.insertAt(i: Int, firstIndex: Int, lastIndex: Int) {
    val currNext = this[i]
    this[i] = firstIndex
    this[lastIndex] = currNext
}

fun Array<Int>.print() {
    var i = this[0]
    do {
        print("$i ")
        i = this[i]
    } while (i != this[0] && i > 0)
    println()
}

const val N = 1_000_000
fun main() {
    val cupsPrefix = readln().map { char -> char.digitToInt() }
    val cups = Array(N + 1) { i ->
        when (i) {
            0, // sentinel
            N -> cupsPrefix[0]

            else -> i + 1
        }
    }

    for (i in 0..<cupsPrefix.size - 1) {
        cups[cupsPrefix[i]] = cupsPrefix[i + 1]
    }
    cups[cupsPrefix.last()] = if (cupsPrefix.size == N) cupsPrefix[0] else cupsPrefix.size + 1

    arrangeCups2(cups, 10_000_000)
    println("${cups[1].toLong() * cups[cups[1]].toLong()}")
}
