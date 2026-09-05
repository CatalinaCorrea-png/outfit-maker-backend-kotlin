package ar.outfitmaker.service

import ar.outfitmaker.domain.Garment
import ar.outfitmaker.domain.GarmentFilters
import ar.outfitmaker.dto.GarmentDTO
import ar.outfitmaker.dto.PageResponse
import ar.outfitmaker.dto.toDTO
import ar.outfitmaker.repository.GarmentRepository
import ar.outfitmaker.specification.GarmentSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GarmentService(
    @Autowired
    private val garmentRepository: GarmentRepository,
) {

    //    @Transactional(readOnly = true)
    fun getGarments(garmentFilters: GarmentFilters, pageable: Pageable): PageResponse<GarmentDTO> {
        // Query w/ Specification Filters and Paging
        val spec = GarmentSpecification.byCriteria(garmentFilters)
        val page : Page<Garment> = garmentRepository.findAll(spec, pageable)

        return PageResponse(
            content = page.content.map { it.toDTO(it) },
            page = page.number,
            pageSize = page.size,
            totalElements = page.totalElements.toInt(),
            totalPages = page.totalPages
        )
    }


}