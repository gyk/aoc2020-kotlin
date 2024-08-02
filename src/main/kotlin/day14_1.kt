class DockingData {
    private val mem: HashMap<Int, ULong> = HashMap()
    private var eraser: ULong = ULong.MAX_VALUE
    private var mask: ULong = 0UL

    fun applyStatement(statement: ProgramStatement) {
        when (statement) {
            is ProgramStatement.Mask -> {
                val maskString = statement.maskString
                require(maskString.length == 36)
                var eraser = ULong.MAX_VALUE
                var mask = 0UL
                for ((i, ch) in maskString.toList().asReversed().withIndex()) {
                    if (ch != 'X') {
                        eraser = eraser xor (1UL shl i)
                        if (ch == '1') {
                            mask = mask or (1UL shl i)
                        }
                    }
                }
                this.eraser = eraser
                this.mask = mask
            }
            is ProgramStatement.Mem -> {
                mem[statement.address.toInt()] = (statement.value and eraser) or mask
            }
        }
    }

    fun sum(): ULong = mem.map { (_, v) -> v }.sum()
}

val MASK_PATTERN = Regex("""mask = (?<mask>[01X]+)""")
val MEM_PATTERN = Regex("""mem\[(?<address>\d+)] = (?<value>\d+)""")

sealed class ProgramStatement {
    data class Mask(val maskString: String) : ProgramStatement()
    data class Mem(val address: ULong, val value: ULong) : ProgramStatement()

    companion object {
        fun parse(s: String): ProgramStatement? {
            val maskMatches = MASK_PATTERN.find(s)
            if (maskMatches != null) {
                return Mask(maskMatches.groups["mask"]!!.value)
            } else {
                val memMatches = MEM_PATTERN.find(s)
                if (memMatches != null) {
                    val g = memMatches.groups
                    return Mem(g["address"]!!.value.toULong(), g["value"]!!.value.toULong())
                }
            }
            return null
        }
    }
}

fun main() {
    val lines = generateSequence { readlnOrNull() }
    val dockingData = DockingData()
    lines.map { ProgramStatement.parse(it) }
        .forEach { statement ->
            dockingData.applyStatement(statement!!)
        }
    println("${dockingData.sum()}")
}
