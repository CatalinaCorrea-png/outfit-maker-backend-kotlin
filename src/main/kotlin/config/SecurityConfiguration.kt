package ar.outfitmaker.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@Profile("!test")
class SecurityConfiguration(
    private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): DefaultSecurityFilterChain =
        http
            .cors(Customizer.withDefaults())
            .csrf {
                // Access token viaja en header Authorization (no cookie), inmune a CSRF
                // Refresh token en cookie SameSite=Strict, el browser no la manda cross-site
                it.disable()
                //it.ignoringRequestMatchers("/api/auth", "/refresh")

                // esto genera la cookie XSRF-TOKEN
                //it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //it.csrfTokenRequestHandler(SpaCsrfTokenRequestHandler())
            }
            .authorizeHttpRequests {
                it
                    // Endpoints publicos
                    .requestMatchers("/auth", "/auth/refresh", "/error").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS)
                    .permitAll() // esto es para react pregunta antes de hacer la request real
                    .anyRequest().fullyAuthenticated() // el resto esta bloqueado si no se autentica
            }

            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // la session es stateless por que estamos usando rest api paaaa
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // esto manda 401 si no tenes token o es invalido
            }
            .build()

    @Bean // Spring Security (antes de los filtros de seguridad)
    fun corsConfigurationSource(
        // Orígenes permitidos por CORS, separados por coma. Local: Vite dev (5173).
        // En la nube se agrega la URL del front con la env var CORS_ALLOWED_ORIGINS.
        @Value("\${cors.allowed-origins:http://localhost:5173}") allowedOriginsCsv: String
    ): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = allowedOriginsCsv.split(",").map { it.trim() }
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            exposedHeaders = listOf("WWW-Authenticate")
            allowCredentials = true
            maxAge = 3600 // esto cachea el options
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }

}