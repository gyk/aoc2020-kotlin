fun computeLoopsize(publicKey: Long): Int {
    var i = 0
    var x = 1L
    while (x != publicKey) {
        x = (x * 7).mod(20201227L)
        i++
    }
    return i
}

fun computeEncryptionKey(publicKey: Long, loopSize: Int): Long {
    var x = 1L
    repeat(loopSize) {
        x = (x * publicKey).mod(20201227L)
    }
    return x
}

fun main() {
    val cardPub = readln().toLong()
    val doorPub = readln().toLong()
    println("${computeEncryptionKey(doorPub, computeLoopsize(cardPub))}")
}
