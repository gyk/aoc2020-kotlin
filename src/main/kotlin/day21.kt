val FOOD_PATTERN = Regex("""(?<ingredients>[^)]+?)\s*\(contains\s+(?<allergens>[^)]+)\)""")

fun parseFoods(): Sequence<Pair<Set<String>, Set<String>>> {
    return generateSequence { readlnOrNull() }
        .map { line ->
            val g = FOOD_PATTERN.find(line)!!.groups
            val ingredients = g["ingredients"]!!.value.split(' ').toSet()
            val allergens = g["allergens"]!!.value.split(Regex(",\\s*")).toSet()
            ingredients to allergens
        }
}

fun main() {
    val foods = parseFoods().toList()
    val allIngredients = mutableSetOf<String>()
    val allAllergens = mutableSetOf<String>()
    val responsible = mutableMapOf<String, Set<String>>()
    val appearCount = mutableMapOf<String, Int>()
    foods.forEach { (ingredients, allergens) ->
        allIngredients += ingredients
        allAllergens += allergens
        for (ingredient in ingredients) {
            appearCount.merge(ingredient, 1) { old, new -> old + new }
        }
        for (allergen in allergens) {
            responsible.merge(allergen, ingredients) { old, new -> old intersect new }
        }
    }

    val unsafe = responsible.values.reduce(Set<String>::union)
    val safe = allIngredients subtract unsafe
    println("Part 1: ${safe.sumOf { appearCount[it] ?: 0 }}")

    var dangerousFoods = foods.map { (ingredients, allergens) ->
        (ingredients subtract safe) to allergens
    }

    val assignments = mutableMapOf<String, String>()
    while (assignments.size < allAllergens.size) {
        responsible.clear()
        dangerousFoods.forEach { (ingredients, allergens) ->
            for (allergen in allergens) {
                responsible.merge(allergen, ingredients) { old, new -> old intersect new }
            }
        }

        for ((allergen, ingredients) in responsible) {
            if (ingredients.size == 1) {
                assignments[allergen] = ingredients.first()
                dangerousFoods = dangerousFoods.map { (ingInner, allergens) ->
                    (ingInner subtract ingredients) to allergens.minus(allergen)
                }
            }
        }
    }

    val res = assignments.toSortedMap().values.joinToString(",")
    println("Part 2: $res")
}
