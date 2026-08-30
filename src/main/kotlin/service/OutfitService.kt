package ar.outfitmaker.service

import ar.outfitmaker.domain.Outfit
import ar.outfitmaker.domain.OutfitFilters
import ar.outfitmaker.dto.PageResponse
import ar.outfitmaker.repository.OutfitRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OutfitService(
    @Autowired
    val outfitRepository: OutfitRepository,
) {
//    @Transactional(readOnly = true)
    fun getOutfits(outfitFilters: OutfitFilters, pageable: Pageable): PageResponse<Outfit> {
        val page : Page<Outfit> = outfitRepository.findByCriteria(outfitFilters, pageable)

        return PageResponse(
            content = page.content,
            page = page.number,
            pageSize = page.size,
            totalElements = page.totalElements.toInt(),
            totalPages = page.totalPages
        )
    }
}