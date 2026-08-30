package ar.outfitmaker.domain

data class OutfitFilters(
    val page: Int = 0,
    val pageSize: Int = 6,
    val sortBy: String = "createdAt", // "name", "createdAt"
    val ascending: Boolean = true,
) {
}