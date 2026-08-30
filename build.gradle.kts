plugins {
    kotlin("jvm") version "2.3.10"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.serialization") version "1.9.0"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
}

group = "ar.outfitmaker"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.data:spring-data-commons")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    //para que jackson pueda deserializar los data class
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //para posgres y springboot
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("com.h2database:h2")
    // Validación de DTOs (@NotBlank, @Size, etc.)
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Cliente HTTP para llamar a API externa (de un modelo)
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Spring Security (base necesaria para JWT)
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // JWT - JJWT (librería más popular para Java/Kotlin)
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // testing
    val kotestVersion = "5.8.0"
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}