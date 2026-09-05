package ar.outfitmaker.bootstrap

import ar.outfitmaker.domain.Category
import ar.outfitmaker.domain.Fit
import ar.outfitmaker.domain.Garment
import ar.outfitmaker.domain.GarmentImage
import ar.outfitmaker.domain.Pattern
import ar.outfitmaker.domain.Season
import ar.outfitmaker.domain.Slot
import ar.outfitmaker.domain.User
import ar.outfitmaker.repository.CategoryRepository
import ar.outfitmaker.repository.GarmentRepository
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

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var garmentRepository: GarmentRepository

    // ─── Encoder ──────────────────────────────────────────────────────
    @Autowired
    private lateinit var encoder: PasswordEncoder

    // ─── Users ─────────────────────────────────────────────────────────────
    private lateinit var cher: User
    private lateinit var dionne: User

    // ─── Categories ────────────────────────────────────────────────────────
    private lateinit var remera: Category
    private lateinit var pantalon: Category
    private lateinit var campera: Category
    private lateinit var zapatillas: Category

    // ─── Garments ──────────────────────────────────────────────────────────
    private lateinit var remeraRayada: Garment
    private lateinit var jeanNegro: Garment
    private lateinit var camperaDeJean: Garment
    private lateinit var zapatillasBlancas: Garment


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

    fun createCategory(category: Category) {
        val categoryEnRepo = categoryRepository.findByName(category.name)
        if (categoryEnRepo.isPresent) {
            category.id = categoryEnRepo.get().id
        } else {
            categoryRepository.save(category)
            println("Category ${category.name} creada")
        }
    }

    fun createGarment(garment: Garment) {
        val garmentEnRepo = garmentRepository.findByUserAndName(garment.user, garment.name)
        if (garmentEnRepo.isPresent) {
            garment.id = garmentEnRepo.get().id
        } else {
            garmentRepository.save(garment)
            println("Garment ${garment.name} creada")
        }
    }

    // Las imágenes se persisten solas por el cascade de Garment.images,
    // así que hay que colgarlas antes del createGarment.
    // El orden de los parámetros es el sortOrder: la primera es la portada.
    fun addImages(garment: Garment, vararg imageUrls: String) {
        imageUrls.forEachIndexed { index, imageUrl ->
            garment.addImage(
                GarmentImage(
                    garment = garment,
                    imageUrl = imageUrl,
                    sortOrder = index
                )
            )
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

    fun initCategories() {
        remera = Category(
            name = "Remera",
            slot = Slot.UPPER
        )

        pantalon = Category(
            name = "Pantalón",
            slot = Slot.LOWER
        )

        campera = Category(
            name = "Campera",
            slot = Slot.OUTERWEAR
        )

        zapatillas = Category(
            name = "Zapatillas",
            slot = Slot.FOOTWEAR
        )
        listOf<Category>(remera, pantalon, campera, zapatillas).forEach { category -> createCategory(category) }
    }

    fun initGarments() {
        remeraRayada = Garment(
            user = cher,
            category = remera,
            name = "Remera rayada",
            brand = "Zara",
            primaryColor = "#ffffff",
            secondaryColor = "navy",
            pattern = Pattern.STRIPED,
            material = "algodón",
            formality = 2,
            fit = Fit.REGULAR,
            season = Season.SUMMER,
            careNotes = "Lavar con agua fría"
        )

        jeanNegro = Garment(
            user = dionne,
            category = pantalon,
            name = "Jean negro",
            brand = "Levi's",
            primaryColor = "#1c1c1c",
            pattern = Pattern.SOLID,
            material = "denim",
            formality = 3,
            fit = Fit.SLIM,
            season = Season.ALL_SEASONS
        )

        camperaDeJean = Garment(
            user = cher,
            category = campera,
            name = "Campera de jean",
            brand = "Levi's",
            primaryColor = "#5b7ba6",
            pattern = Pattern.SOLID,
            material = "denim",
            formality = 2,
            fit = Fit.OVERSIZED,
            season = Season.MID_SEASON,
            careNotes = "No usar secarropas"
        )

        zapatillasBlancas = Garment(
            user = dionne,
            category = zapatillas,
            name = "Zapatillas blancas",
            brand = "Adidas",
            primaryColor = "#ffffff",
            pattern = Pattern.SOLID,
            material = "cuero",
            formality = 2,
            fit = Fit.REGULAR,
            season = Season.ALL_SEASONS
        )
        addImages(
            remeraRayada,
            "https://picsum.photos/seed/remera-rayada-1/600/750",
            "https://picsum.photos/seed/remera-rayada-2/600/750"
        )
        addImages(jeanNegro, "https://picsum.photos/seed/jean-negro-1/600/750")
        addImages(
            camperaDeJean,
            "https://picsum.photos/seed/campera-jean-1/600/750",
            "https://picsum.photos/seed/campera-jean-2/600/750",
            "https://picsum.photos/seed/campera-jean-3/600/750"
        )
        // zapatillasBlancas queda sin fotos a propósito: es el caso de la card sin imagen

        listOf<Garment>(remeraRayada, jeanNegro, camperaDeJean, zapatillasBlancas)
            .forEach { garment -> createGarment(garment) }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // InitializingBean
    // ═════════════════════════════════════════════════════════════════════════
    override fun afterPropertiesSet() {
        println("************************************************************************")
        println("Running initialization")
        println("************************************************************************")

        this.initUsers()
        this.initCategories()
        this.initGarments()

        println("------------------------------------------------------------------------")
    }
}
