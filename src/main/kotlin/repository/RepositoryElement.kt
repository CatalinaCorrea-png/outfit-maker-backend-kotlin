package ar.outfitmaker.repository

import java.util.UUID

interface RepositoryElement {
    val id: UUID

    fun validate()
}