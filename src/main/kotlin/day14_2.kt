class DockingData2 {
    private val mem: HashMap<Long, ULong> = HashMap()
    private var eraser: ULong = ULong.MAX_VALUE
    private var mask: ULong = 0UL
    private val bitList: ArrayList<ULong> = ArrayList()

    fun applyStatement(statement: ProgramStatement) {
        when (statement) {
            is ProgramStatement.Mask -> {
                val maskString = statement.maskString
                require(maskString.length == 36)

                bitList.clear()
                var eraser = ULong.MAX_VALUE
                var mask = 0UL
                for ((i, ch) in maskString.toList().asReversed().withIndex()) {
                    if (ch != '0') {
                        eraser = eraser xor (1UL shl i)
                    }
                    if (ch == 'X') {
                        bitList.add(1UL shl i)
                    } else if (ch == '1') {
                        mask = mask or (1UL shl i)
                    }
                }
                this.eraser = eraser
                this.mask = mask
            }

            is ProgramStatement.Mem -> {
                val oriAddr = (statement.address and eraser) or mask
                var addr = oriAddr

                var i = 0
                var end = 0
                while (true) {
                    if (end == bitList.size) {
                        mem[addr.toLong()] = statement.value
                        i--
                    }
                    if (i == end) {
                        if (addr and bitList[i] == oriAddr and bitList[i]) {
                            end++
                        }
                        i++
                    } else { // backtracking, i == end - 1
                        if (addr and bitList[i] != oriAddr and bitList[i]) {
                            addr = addr xor bitList[i]
                            end--
                            if (end == 0) {
                                break
                            }
                            i--
                        } else {
                            addr = addr xor bitList[i]
                            i++
                        }
                    }
                }
            }
        }
    }

    fun sum(): ULong = mem.map { (_, v) -> v }.sum()
}

fun main() {
    val lines = generateSequence { readlnOrNull() }
    val dd2 = DockingData2()
    lines.map { ProgramStatement.parse(it) }
        .forEach { statement ->
            dd2.applyStatement(statement!!)
        }
    println("${dd2.sum()}")
}
