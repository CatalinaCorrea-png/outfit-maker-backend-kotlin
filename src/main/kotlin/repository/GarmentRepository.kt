package ar.outfitmaker.repository

import ar.outfitmaker.domain.Garment
import ar.outfitmaker.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface GarmentRepository : CrudRepository<Garment, UUID> {

    // TODO: reemplazar por Specifications cuando GarmentFilters tenga criterios reales
    @EntityGraph(attributePaths = ["user", "category", "images"])
    fun findAll(spec: Specification<Garment>, pageable: Pageable): Page<Garment>

    fun findByUserAndName(user: User, name: String): Optional<Garment>
}