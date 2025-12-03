package com.example.pasteleriasabores.pasteleria_sabores.security;

import com.example.pasteleriasabores.pasteleria_sabores.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            // 1. EXTRAER EMAIL DESDE JWT
            String email = jwtUtil.extraerEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 2. VALIDAR TOKEN
                if (jwtUtil.validarToken(token, email)) {

                    // 3. OBTENER ROL DEL JWT
                    String rol = jwtUtil.extraerRol(token); // ADMIN / CLIENTE / TEST

                    // 4. CREAR AUTHORITIES CORRECTAS (SIN ROLE_)
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rol);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(authority)
                            );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 5. REGISTRAR AUTENTICACIÓN
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
