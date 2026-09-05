package ar.outfitmaker.specification

import ar.outfitmaker.domain.Category
import ar.outfitmaker.domain.Fit
import ar.outfitmaker.domain.Garment
import ar.outfitmaker.domain.GarmentFilters
import ar.outfitmaker.domain.Pattern
import ar.outfitmaker.domain.Season
import ar.outfitmaker.domain.User
import org.springframework.data.jpa.domain.Specification

object GarmentSpecification {

    fun userIdEqual(userId: String?): Specification<Garment> =
        Specification { root, _, cb ->
            userId?.let {
                cb.equal(root.get<User>("user").get<String>("id"), it)
            }
        }

    fun categoryLike(category: String?): Specification<Garment> =
        Specification { root, _, cb ->
            category?.let {
                cb.like(cb.lower(root.get<Category>("category").get("name")), "%${it.lowercase()}%")
            }
        }

    fun nameLike(name: String?): Specification<Garment> =
        Specification { root, _, cb ->
            name?.let {
                cb.like(cb.lower(root.get("name")), "%${it.lowercase()}%")
            }
        }

    fun brandLike(brand: String?): Specification<Garment> =
        Specification { root, _, cb ->
            brand?.let {
                cb.like(cb.lower(root.get("brand")), "%${it.lowercase()}%")
            }
        }

    fun primaryColorLike(primaryColor: String?): Specification<Garment> =
        Specification { root, _, cb ->
            primaryColor?.let {
                cb.like(cb.lower(root.get("primaryColor")), "%${it.lowercase()}%")
            }
        }

    fun secondaryColorLike(secondaryColor: String?): Specification<Garment> =
        Specification { root, _, cb ->
            secondaryColor?.let {
                cb.like(cb.lower(root.get("secondaryColor")), "%${it.lowercase()}%")
            }
        }

    // pattern es un enum (@Enumerated(STRING)): match exacto, no LIKE
    fun patternEqual(pattern: Pattern?): Specification<Garment> =
        Specification { root, _, cb ->
            pattern?.let {
                cb.equal(root.get<Pattern>("pattern"), it)
            }
        }

    fun materialLike(material: String?): Specification<Garment> =
        Specification { root, _, cb ->
            material?.let {
                cb.like(cb.lower(root.get("material")), "%${it.lowercase()}%")
            }
        }

    // 1 = deportivo, 2 = casual, 3 = smart casual, 4 = semi formal, 5 = formal
    fun formalityEqual(formality: Int?): Specification<Garment> =
        Specification { root, _, cb ->
            formality?.let {
                cb.equal(root.get<Int>("formality"), it)
            }
        }

    fun fitEqual(fit: Fit?): Specification<Garment> =
        Specification { root, _, cb ->
            fit?.let {
                cb.equal(root.get<Fit>("fit"), it)
            }
        }

    // ALL_SEASONS entra siempre: sirve para cualquier temporada pedida
    fun seasonEqual(season: Season?): Specification<Garment> =
        Specification { root, _, cb ->
            season?.let {
                if (it == Season.ALL_SEASONS) {
                    cb.equal(root.get<Season>("season"), Season.ALL_SEASONS)
                } else {
                    cb.or(
                        cb.equal(root.get<Season>("season"), it),
                        cb.equal(root.get<Season>("season"), Season.ALL_SEASONS)
                    )
                }
            }
        }

    // Soft delete: active = false -> prenda donada/vendida
    fun activeEqual(active: Boolean?): Specification<Garment> =
        Specification { root, _, cb ->
            active?.let {
                cb.equal(root.get<Boolean>("active"), it)
            }
        }

    fun onlyActive(): Specification<Garment> =
        Specification { root, _, cb ->
            cb.isTrue(root.get("active"))
        }


    // Función builder de conveniencia que compone todas las spec
    fun byCriteria(
        criteria: GarmentFilters
    ): Specification<Garment> =
        Specification.where(userIdEqual(criteria.userId))
            .and(categoryLike(criteria.category))
            .and(nameLike(criteria.name))
            .and(brandLike(criteria.brand))
//            .and(primaryColorLike(criteria.primaryColor))
//            .and(secondaryColorLike(criteria.secondaryColor))
            .and(patternEqual(criteria.pattern))
//            .and(materialLike(criteria.material))
            .and(formalityEqual(criteria.formality))
//            .and(fitEqual(criteria.fit))
            .and(seasonEqual(criteria.season))
            // si no se pide explicitamente, solo se listan las prendas activas
            .and(criteria.active?.let { activeEqual(it) } ?: onlyActive())
}
