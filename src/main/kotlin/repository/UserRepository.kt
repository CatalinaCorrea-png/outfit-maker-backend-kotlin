package ar.outfitmaker.repository

import ar.outfitmaker.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, UUID> {

    fun findByEmail(email: String): Optional<User>
}