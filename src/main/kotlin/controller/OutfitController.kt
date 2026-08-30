package ar.outfitmaker.controller

import ar.outfitmaker.domain.Outfit
import ar.outfitmaker.domain.OutfitFilters
import ar.outfitmaker.dto.PageResponse
import ar.outfitmaker.service.OutfitService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/outfit")
class OutfitController(
    private val outfitService: OutfitService,
) {
    // get filtered outfits or ALL when there are no filters
    @GetMapping("/filtered-outfits")
    fun getFilteredOutfits(
        @ModelAttribute outfitFilters: OutfitFilters,
    ): PageResponse<Outfit> {
        val direction = if (outfitFilters.ascending) Sort.Direction.ASC else Sort.Direction.DESC
        val pageable = PageRequest.of(outfitFilters.page, outfitFilters.pageSize, Sort.by(direction, outfitFilters.sortBy))

        return outfitService.getOutfits(outfitFilters, pageable)
    }
}