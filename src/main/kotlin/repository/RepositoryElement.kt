package ar.outfitmaker.repository

import java.util.UUID

interface RepositoryElement {
    var id: String?

    fun validate()
}