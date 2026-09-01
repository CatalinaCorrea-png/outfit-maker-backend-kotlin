package ar.outfitmaker.bootstrap

import ar.outfitmaker.domain.User
import ar.outfitmaker.repository.UserRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ProjectBootstrap : InitializingBean {
    // ─── Repositories ──────────────────────────────────────────────────────
    @Autowired
    private lateinit var userRepository: UserRepository

    // ─── Encoder ──────────────────────────────────────────────────────
    @Autowired
    private lateinit var encoder: PasswordEncoder

    // ─── Users ─────────────────────────────────────────────────────────────
    private lateinit var cher: User
    private lateinit var dionne: User


    // ═════════════════════════════════════════════════════════════════════════
    // Creation Methods
    // ═════════════════════════════════════════════════════════════════════════

    fun createUser(user: User) {
        val userEnRepo = userRepository.findByEmail(user.email)
        if (userEnRepo.isPresent) {
            user.id = userEnRepo.get().id
        } else {
            userRepository.save(user)
            println("User ${user.name} creado")
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Initialization Methods
    // ═════════════════════════════════════════════════════════════════════════

    fun initUsers() {
        cher = User(
            email = "cher@gmail.com",
            name = "Cher",
            avatarUrl = "https://media.vogue.mx/photos/5c7732b041573ab604c65dc1/2:3/w_2560%2Cc_limit/GettyImages-159835758.jpg",
            password = encoder.encode("123")
        )

        dionne = User(
            email = "dionne@gmail.com",
            name = "Dionne",
            avatarUrl = "https://mx.pinterest.com/pin/646055509080216998/",
            password = encoder.encode("123")
        )
        listOf<User>(cher, dionne).forEach { user -> createUser(user) }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // InitializingBean
    // ═════════════════════════════════════════════════════════════════════════
    override fun afterPropertiesSet() {
        println("************************************************************************")
        println("Running initialization")
        println("************************************************************************")

        this.initUsers()

        println("------------------------------------------------------------------------")
    }
}