package ar.outfitmaker.domain

import ar.outfitmaker.repository.RepositoryElement
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "outfits")
class Outfit(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    var name: String? = null,

    var notes: String? = null,

    // ¿Lo armó la IA o el usuario manualmente?
    @Column(name = "ai_generated", nullable = false)
    val aiGenerated: Boolean = false,

    // El prompt que usó el usuario si fue generado por IA
    @Column(name = "ai_prompt")
    val aiPrompt: String? = null,

    // Rating del usuario (1-5) para feedback loop
    val rating: Int? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDate = LocalDate.now(),

    // Relaciones
    @OneToMany(mappedBy = "outfit", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<OutfitItem> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "outfit_tags",
        joinColumns = [JoinColumn(name = "outfit_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: MutableSet<Tag> = mutableSetOf()

    ) : RepositoryElement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override var id: String? = null

    fun addOutfitItem(item: OutfitItem) {
        items.add(item)
    }

    fun deleteOutfitItem(item: OutfitItem) {
        items.remove(item)
    }

    fun addTag(tag: Tag) {
        if (tags.contains(tag)) throw IllegalArgumentException("Tag already exists")
        tags.add(tag)
    }

    fun deleteTag(tag: Tag) {
        tags.remove(tag)
    }

    override fun validate() {
        TODO("Not yet implemented")
    }
}

// Cada prenda dentro de un outfit, con orden de capa
@Entity
@Table(name = "outfit_items")
class OutfitItem (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outfit_id", nullable = false)
    val outfit: Outfit,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_id", nullable = false)
    val garment: Garment,

    // Orden de capas: 0 = base (remera), 1 = medio (camisa), 2 = exterior (campera)
    @Column(name = "layer_order", nullable = false)
    val layerOrder: Int = 0,

) : RepositoryElement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override var id: String? = null

    override fun validate() {
        TODO("Not yet implemented")
    }
}