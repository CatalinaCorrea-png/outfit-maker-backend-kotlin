package ar.outfitmaker.domain

data class OutfitFilters(
    val page: Int = 0,
    val pageSize: Int = 6,
    val sortBy: String = "createdAt", // "name", "createdAt"
    val ascending: Boolean = true,
) {
}

// ?: null bc Specifications ignore the filter when its null. Makes it optional (OR)
data class GarmentFilters(
    // filtering
    val userId: String? = null,
    val category: String? = null,
    val name: String? = null,
    val brand: String? = null,
    val pattern: Pattern? = null,
    val formality: Int? = null,
    val season: Season? = null,
    val active: Boolean? = null,
    // paging & sorting
    val page: Int = 0,
    val pageSize: Int = 6,
    val sortBy: String = "createdAt", // "name", "createdAt"
    val ascending: Boolean = true,
) {
}