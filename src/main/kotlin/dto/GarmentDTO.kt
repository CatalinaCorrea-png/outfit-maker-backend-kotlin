package ar.outfitmaker.dto

import ar.outfitmaker.domain.Fit
import ar.outfitmaker.domain.Garment
import ar.outfitmaker.domain.Pattern
import ar.outfitmaker.domain.Season

data class GarmentDTO(
    var id: String,
    var category: String, // category name
    var name: String,
    var brand: String,
    var primaryColor: String,
    var secondaryColor: String,
    var pattern: Pattern,
    var material: String,
    var formality: Int,
    var fit: Fit,
    var season: Season,
    var careNotes: String?,
    var active: Boolean,
    var createdAt: String,
    var imageUrl: String?,
) {
}

fun Garment.toDTO(garment: Garment) = GarmentDTO(
    id = garment.id!!,
    category = garment.category.name,
    name = garment.name,
    brand = garment.brand,
    primaryColor = garment.primaryColor,
    secondaryColor = garment.secondaryColor,
    pattern = garment.pattern,
    material = garment.material,
    formality = garment.formality,
    fit = garment.fit,
    season = garment.season,
    careNotes = garment.careNotes,
    active = garment.active,
    createdAt = garment.createdAt.toString(),
    imageUrl = garment.primaryImage()?.imageUrl,
)