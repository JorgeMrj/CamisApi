package srangeldev.camisapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utilidad para manejar tokens JWT de manera simple
 * Implementación básica para estudiantes de DAW
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:miClaveSecretaSuperSegura123}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private long expiration; // 24 horas en segundos

    /**
     * Genera un token JWT para el usuario
     */
    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS)))
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Extrae el username del token
     */
    public String extractUsername(String token) {
        return getDecodedJWT(token).getSubject();
    }

    /**
     * Verifica si el token es válido
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        return getDecodedJWT(token).getExpiresAt().before(new Date());
    }

    /**
     * Decodifica el JWT
     */
    private DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

    /**
     * Valida el token básicamente
     */
    public boolean validateToken(String token) {
        try {
            getDecodedJWT(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
