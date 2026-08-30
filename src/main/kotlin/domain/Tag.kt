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

enum class TagType {
    OCCASION,
    STYLE,
    MOOD
}

@Entity
@Table(name = "tags")
class Tag(
    @Column(nullable = false, unique = true)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: TagType,
) : RepositoryElement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override var id: UUID = UUID.randomUUID()

    override fun validate() {
        TODO("Not yet implemented")
    }
}