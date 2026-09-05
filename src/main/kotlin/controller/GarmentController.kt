package ar.outfitmaker.controller

import ar.outfitmaker.domain.Garment
import ar.outfitmaker.domain.GarmentFilters
import ar.outfitmaker.dto.GarmentDTO
import ar.outfitmaker.dto.PageResponse
import ar.outfitmaker.service.GarmentService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/garments")
class GarmentController(
    private val garmentService: GarmentService,
) {
    // get filtered garments or ALL when there are no filters
    @GetMapping("/filtered-garments")
    fun getFilteredGarments(
        @ModelAttribute garmentFilters: GarmentFilters,
    ): PageResponse<GarmentDTO> {
        val direction = if (garmentFilters.ascending) Sort.Direction.ASC else Sort.Direction.DESC
        val pageable = PageRequest.of(garmentFilters.page, garmentFilters.pageSize, Sort.by(direction, garmentFilters.sortBy))

        return garmentService.getGarments(garmentFilters, pageable)
    }
}