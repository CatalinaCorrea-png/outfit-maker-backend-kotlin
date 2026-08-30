package ar.outfitmaker.dto

data class PageResponse<T>(
    var content: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int
)