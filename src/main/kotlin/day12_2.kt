import kotlin.math.absoluteValue

data class Waypoint(val x: Int, val y: Int)

data class FerryWaypoint(val x: Int, val y: Int, val waypoint: Waypoint) {
    fun navigate(ins: NavigationInstruction): FerryWaypoint {
        return when (ins) {
            is NavigationInstruction.Move -> {
                val dx = ins.dir.x * ins.distance
                val dy = ins.dir.y * ins.distance
                val waypoint = Waypoint(waypoint.x + dx, waypoint.y + dy)
                FerryWaypoint(x, y, waypoint)
            }

            is NavigationInstruction.TurnClockwise -> {
                var wp = waypoint
                repeat(ins.times) {
                    wp = Waypoint(wp.y, -wp.x)
                }
                FerryWaypoint(x, y, wp)
            }

            is NavigationInstruction.Forward -> {
                val dx = waypoint.x * ins.times
                val dy = waypoint.y * ins.times
                FerryWaypoint(x + dx, y + dy, waypoint)
            }
        }
    }
}

fun main() {
    var ferry = FerryWaypoint(0, 0, Waypoint(10, 1))
    generateSequence { readlnOrNull() }
        .map { NavigationInstruction.parse(it)!! }
        .forEach { ins ->
            ferry = ferry.navigate(ins)
        }
    println("${ferry.x.absoluteValue + ferry.y.absoluteValue}")
}
