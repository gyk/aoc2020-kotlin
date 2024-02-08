import kotlin.math.absoluteValue

enum class Direction(val x: Int, val y: Int) {
    E(1, 0),
    S(0, -1),
    W(-1, 0),
    N(0, 1);

    fun turnClockwise(): Direction {
        return when (this) {
            E -> S
            S -> W
            W -> N
            N -> E
        }
    }
}

sealed class NavigationInstruction {
    data class Move(val dir: Direction, val distance: Int) : NavigationInstruction()
    data class TurnClockwise(val times: Int) : NavigationInstruction()
    data class Forward(val times: Int) : NavigationInstruction()

    companion object {
        fun parse(s: String): NavigationInstruction? {
            val (_, instructionS, operandS) = Regex("""([A-Z]+)(\d+)""")
                .find(s)
                ?.groupValues
                ?: return null
            val operand = operandS.toIntOrNull() ?: return null
            return when (instructionS) {
                "E" -> Move(Direction.E, operand)
                "S" -> Move(Direction.S, operand)
                "W" -> Move(Direction.W, operand)
                "N" -> Move(Direction.N, operand)
                "L" -> TurnClockwise((-operand / 90).mod(4))
                "R" -> TurnClockwise((operand / 90).mod(4))
                "F" -> Forward(operand)
                else -> null
            }
        }
    }
}

data class Ferry(val x: Int, val y: Int, val face: Direction) {
    fun navigate(ins: NavigationInstruction): Ferry {
        return when (ins) {
            is NavigationInstruction.Move -> {
                val dx = ins.dir.x * ins.distance
                val dy = ins.dir.y * ins.distance
                Ferry(x + dx, y + dy, face)
            }

            is NavigationInstruction.TurnClockwise -> {
                var facing = face
                repeat(ins.times) {
                    facing = facing.turnClockwise()
                }
                Ferry(x, y, facing)
            }

            is NavigationInstruction.Forward -> {
                val dx = face.x * ins.times
                val dy = face.y * ins.times
                Ferry(x + dx, y + dy, face)
            }
        }
    }
}

fun main() {
    var ferry = Ferry(0, 0, Direction.E)
    generateSequence { readlnOrNull() }
        .map { NavigationInstruction.parse(it)!! }
        .forEach { ins ->
            ferry = ferry.navigate(ins)
        }
    println("${ferry.x.absoluteValue + ferry.y.absoluteValue}")
}
