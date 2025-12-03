package com.example.pasteleriasabores.pasteleria_sabores.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "tu_clave_secreta_super_segura";

    // =======================
    // GENERAR TOKEN
    // =======================
    public String generarToken(String email, String rol) {

        return Jwts.builder()
                .setSubject(email)
                .claim("authorities", List.of(rol))   // <<⭐ AQUÍ guardamos tu rol
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // =======================
    // OBTENER EMAIL
    // =======================
    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // =======================
    // OBTENER ROL (authority)
    // =======================
    public String extraerRol(String token) {
        Claims claims = extraerTodosLosClaims(token);
        List<String> roles = claims.get("authorities", List.class);

        if (roles != null && !roles.isEmpty()) {
            return roles.get(0);  // ADMIN o CLIENTE o TEST
        }

        return null;
    }

    // =======================
    // VALIDACIÓN TOKEN
    // =======================
    public Boolean validarToken(String token, String emailUsuario) {
        final String email = extraerEmail(token);
        return (email.equals(emailUsuario) && !estaExpirado(token));
    }

    // =======================
    // MÉTODOS INTERNOS
    // =======================
    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean estaExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }
}
