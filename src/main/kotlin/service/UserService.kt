package ar.outfitmaker.service

import ar.outfitmaker.domain.User
import ar.outfitmaker.errors.ConflictException
import ar.outfitmaker.errors.NotFoundException
import ar.outfitmaker.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class UserService(
    @Autowired
    val userRepository: UserRepository,

    private val encoder: PasswordEncoder
) {

    @Transactional
    fun create(user: User): User {
        val existingUser: Optional<User> = userRepository.findByEmail(user.email)
        if (existingUser.isEmpty) {
            val userCopy = User(
                name = user.name,
                email = user.email,
                password = encoder.encode(user.password)
            )
            userCopy.validate()
            return userRepository.save(userCopy)
        } else {
            throw ConflictException("USER_EMAIL_ALREADY_EXISTS", "Email '${user.email}' ya se encuentra registrado")
        }
    }

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): User {
        val persistedUser = userRepository
            .findByEmail(email)
            .orElseThrow {
                NotFoundException("USER_NOT_FOUND", "No se encuentra un usuario registrado con este email: $email")
            }
        return persistedUser
    }
}