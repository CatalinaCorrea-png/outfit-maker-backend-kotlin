package ar.outfitmaker.domain

import ar.outfitmaker.repository.RepositoryElement
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Column(unique = true, nullable = false)
    var email: String = "",

    @Column(name = "name", nullable = false)
    var name: String = "",

    @Column(name = "avatar_url")
    var avatarUrl: String = "",

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDate = LocalDate.now(),

    // Relaciones (lazy por defecto en @OneToMany)
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val garments: MutableList<Garment> =mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val outfits: MutableList<Outfit> = mutableListOf(),

    var password: String = "",
) : RepositoryElement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override var id: UUID = UUID.randomUUID()

    fun addGarment(garment: Garment) {
        garments.add(garment)
    }

    fun deleteGarment(garment: Garment) {
        garments.remove(garment)
    }

    fun addOutfit(outfit: Outfit) {
        outfits.add(outfit)
    }

    fun deleteOutfit(outfit: Outfit) {
        outfits.remove(outfit)
    }

    override fun validate() {
        TODO("Not yet implemented")
    }
}