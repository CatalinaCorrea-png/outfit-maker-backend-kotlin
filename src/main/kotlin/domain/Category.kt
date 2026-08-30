package ar.outfitmaker.domain

import ar.outfitmaker.repository.RepositoryElement
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

enum class Slot {
    UPPER,
    LOWER,
    FOOTWEAR,
    OUTERWEAR,
    ACCESSORY,
    FULL_BODY
}

@Entity
@Table(name = "categories")
class Category(
    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var slot: Slot,
) : RepositoryElement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override var id: UUID = UUID.randomUUID()


    override fun validate() {
        TODO("Not yet implemented")
    }
}
 /*
 slot indica la posición corporal: upper, lower, footwear, outerwear, accessory, full_body (vestidos, enteritos).
  */