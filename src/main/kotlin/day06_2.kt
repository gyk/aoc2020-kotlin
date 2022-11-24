fun main() {
    val formSeq = inputForms()

    println(
        formSeq.map {
            it.lineSequence().map {
                it.asSequence().toSet()
            }.reduce { accSet, aSet -> accSet.intersect(aSet) }.size
        }.sum()
    )
}
