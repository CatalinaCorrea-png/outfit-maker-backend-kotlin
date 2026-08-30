package ar.outfitmaker.repository

import ar.outfitmaker.domain.Outfit
import ar.outfitmaker.domain.OutfitFilters
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OutfitRepository : CrudRepository<Outfit, UUID> {

    // TODO: reemplazar por Specifications cuando OutfitFilters tenga criterios reales
    @EntityGraph(attributePaths = ["user", "items", "tags"])
    @Query("SELECT o FROM Outfit o")
    fun findByCriteria(outfitFilters: OutfitFilters, pageable: Pageable): Page<Outfit>
}