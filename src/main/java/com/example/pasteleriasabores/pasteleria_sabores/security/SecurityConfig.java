package com.example.pasteleriasabores.pasteleria_sabores.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // Asegúrate que esta clase exista

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ⭐ RUTAS PÚBLICAS (LOGIN Y REGISTER)
                        .requestMatchers("/api/auth/**").permitAll()

                        // ⭐ ✅ CORREGIDO: RUTAS PÚBLICAS DE PRODUCTOS (solo lectura)
                        .requestMatchers("/api/productos", "/api/productos/*", "/api/productos/categoria/*", 
                                        "/api/productos/destacados", "/api/productos/ofertas").permitAll()

                        // ⭐ ✅ CORREGIDO: RUTAS PÚBLICAS DE CATEGORÍAS (solo lectura)
                        .requestMatchers("/api/categorias", "/api/categorias/*").permitAll()

                        // ⭐ RUTAS PROTEGIDAS PARA USUARIOS AUTENTICADOS
                        .requestMatchers("/api/usuarios/perfil", "/api/usuarios/perfil/**", "/api/usuarios/cambiar-password").authenticated()
                        .requestMatchers("/api/pedidos/mis-pedidos", "/api/pedidos/crear").authenticated()

                        // ⭐ ✅ CORREGIDO: RUTAS SOLO PARA ADMIN (soporte para ADMIN y admin)
                        .requestMatchers("/api/usuarios", "/api/usuarios/**").hasAnyAuthority("ADMIN", "admin")
                        .requestMatchers("/api/productos/**").hasAnyAuthority("ADMIN", "admin") // ✅ NUEVO: CRUD productos
                        .requestMatchers("/api/categorias/**").hasAnyAuthority("ADMIN", "admin") // ✅ NUEVO: CRUD categorías
                        .requestMatchers("/api/pedidos/todos", "/api/pedidos/*/estado").hasAnyAuthority("ADMIN", "admin")

                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .anyRequest().authenticated());

        // ⭐ ACTIVAR JWT
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}