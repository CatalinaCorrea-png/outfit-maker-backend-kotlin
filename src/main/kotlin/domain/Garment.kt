package ar.outfitmaker.domain

import ar.outfitmaker.repository.RepositoryElement
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

enum class Pattern {
    SOLID,
    STRIPED,
    PLAID,
    FLORAL,
    GRAPHIC,
    DOTS,
    CAMO,
    ABSTRACT
}

enum class Season {
    SUMMER,
    WINTER,
    MID_SEASON,
    ALL_SEASONS
}

enum class Fit {
    SLIM,
    REGULAR,
    RELAXED,
    OVERSIZED
}

@Entity
@Table(name = "garments")
class Garment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(nullable = false)
    var name: String = "",

    var brand: String = "",

    // Colores como hex o nombre normalizado: "navy", "#1a237e"
    @Column(name = "primary_color", nullable = false)
    var primaryColor: String = "",

    @Column(name = "secondary_color")
    var secondaryColor: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var pattern: Pattern = Pattern.SOLID,

    // "algodón", "lino", "cuero", "poliéster"
    var material: String = "",

    // 1 = deportivo, 2 = casual, 3 = smart casual, 4 = semi formal, 5 = formal
    @Column(nullable = false)
    var formality: Int = 0, // 1 - 5

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var fit: Fit = Fit.REGULAR,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var season: Season = Season.ALL_SEASONS,

    @Column(name = "care_notes")
    val careNotes: String? = null,

    // Soft delete: false = prenda donada/vendida, pero los outfits históricos quedan
    @Column(nullable = false)
    val active: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDate = LocalDate.now(),

    ) : RepositoryElement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override var id: UUID = UUID.randomUUID()


    override fun validate() {
        TODO("Not yet implemented")
    }
}

@Entity
@Table(name = "garment_images")
class GarmentImage(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_id", nullable = false)
    val garment: Garment,

    // URL en S3 o el storage que uses
    @Column(name = "image_url", nullable = false)
    val imageUrl: String,

    @Column(name = "is_primary", nullable = false)
    val isPrimary: Boolean = false,

    @Column(name = "sort_order", nullable = false)
    val sortOrder: Int = 0,

) : RepositoryElement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override var id: UUID = UUID.randomUUID()


    override fun validate() {
        TODO("Not yet implemented")
    }
}