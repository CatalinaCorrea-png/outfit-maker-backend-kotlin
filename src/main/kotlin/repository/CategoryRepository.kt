package ar.outfitmaker.repository

import ar.outfitmaker.domain.Category
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : CrudRepository<Category, UUID> {

    fun findByName(name: String): Optional<Category>
}
