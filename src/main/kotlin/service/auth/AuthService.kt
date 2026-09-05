package ar.outfitmaker.service.auth

import ar.outfitmaker.config.JwtProperties
import ar.outfitmaker.dto.auth.AuthRequest
import ar.outfitmaker.dto.auth.AuthenticationResponse
import ar.outfitmaker.errors.BusinessException
import ar.outfitmaker.repository.RefreshTokenRepository
import ar.outfitmaker.service.CustomUserDetailsService
import ar.outfitmaker.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class AuthService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userService: UserService
) {
    fun authentication(request: AuthRequest): AuthenticationResponse {
        try {
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.email,
                    request.password
                )
            )
        } catch (ex: BadCredentialsException) {
            throw BusinessException("AUTH_INVALID_CREDENTIALS", "Credenciales invalidas")
        }

        val user = userDetailsService.loadUserByUsername(request.email)
        val userOK = userService.getUserByEmail(user.username)

        val accessToken = generateAccessToken(user)
        val refreshToken = generateRefreshToken(user)
        val expirationTime = jwtProperties.refreshTokenExpiration

        refreshTokenRepository.save(refreshToken, user)

        return AuthenticationResponse(accessToken, refreshToken, expirationTime, userOK.id.toString(), userOK.name)
    }

    fun refreshAccessToken(token: String): Pair<String, String>? {
        val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(token)
            ?: return null

        val extractedEmail = tokenService.extractEmail(token)

        if (tokenService.isExpired(token)) {
            refreshTokenRepository.deleteByToken(token)
            return null
        }

        // se fija si el mail de el token es el mismo que el refresh (IMPROBLABLE que no lo sean y genera otra CONSULTA a la DB)
        if (extractedEmail != refreshTokenUserDetails.username) {
            // Si alguien manipulo el token o no coincide, invalidamos por seguridad
            refreshTokenRepository.deleteByToken(token)
            return null
        }

        refreshTokenRepository.deleteByToken(token)

        val newAccessToken = generateAccessToken(refreshTokenUserDetails)
        val newRefreshToken = generateRefreshToken(refreshTokenUserDetails)

        refreshTokenRepository.save(newRefreshToken, refreshTokenUserDetails)

        return Pair(newAccessToken, newRefreshToken)
    }

    private fun generateRefreshToken(user: UserDetails): String = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
    )

    private fun generateAccessToken(user: UserDetails): String = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
        additionalClaims = mapOf("role" to user.authorities.map { it.authority }, "name" to user.username)
    )

    fun generateTokenForUser(email: String): String {
        val user = userDetailsService.loadUserByUsername(email)
        return generateAccessToken(user)
    }

}