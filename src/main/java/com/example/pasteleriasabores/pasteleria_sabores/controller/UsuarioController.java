package com.example.pasteleriasabores.pasteleria_sabores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.pasteleriasabores.pasteleria_sabores.dto.PasswordChangeRequest;
import com.example.pasteleriasabores.pasteleria_sabores.dto.UsuarioResponse;
import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;
import com.example.pasteleriasabores.pasteleria_sabores.service.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired // ✅ NUEVO: Inyección de PasswordEncoder
    private PasswordEncoder passwordEncoder;
    
    // ===== ENDPOINTS PARA USUARIOS AUTENTICADOS =====
    
    // Obtener perfil del usuario actual
    @GetMapping("/perfil")
    public ResponseEntity<?> getPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Actualizar perfil
   @PutMapping("/perfil")
    public ResponseEntity<?> updatePerfil(@RequestBody Usuario datos) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            UsuarioResponse updated = usuarioService.updateProfileByEmail(email, datos);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Cambiar contraseña
    @PutMapping("/cambiar-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token,
                                           @RequestBody PasswordChangeRequest passwordRequest) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName(); // ✅ CORREGIDO: Obtener email del contexto
            Long userId = usuarioService.obtenerIdPorEmail(email);
            
            if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
            }
            
            usuarioService.changePassword(userId, passwordRequest.getCurrentPassword(), passwordRequest.getNewPassword());
            return ResponseEntity.ok("Contraseña actualizada correctamente");
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // ===== ✅ NUEVOS ENDPOINTS SOLO PARA ADMIN =====
    
    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAllUsers() {
        List<UsuarioResponse> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }
    
    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUserById(@PathVariable Long id) {
        try {
            // Buscar por ID usando el servicio
            List<UsuarioResponse> usuarios = usuarioService.findAll();
            UsuarioResponse usuario = usuarios.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Usuario usuario) {
        try {
            // Hashear la contraseña antes de guardar
            if (usuario.getPassword() != null) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                usuario.setPassword(passwordEncoder.encode("123456")); // Contraseña por defecto
            }
            
            // Establecer valores por defecto
            if (usuario.getRol() == null) {
                usuario.setRol("USER");
            }
            if (usuario.getEstado() == null) {
                usuario.setEstado("activo");
            }
            usuario.setFechaRegistro(LocalDateTime.now());
            
            UsuarioResponse created = usuarioService.register(usuario);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            UsuarioResponse updated = usuarioService.update(id, usuario);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Cambiar rol de usuario
    @PutMapping("/{id}/rol")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestBody String rol) {
        try {
            UsuarioResponse updated = usuarioService.changeRole(id, rol);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Cambiar estado de usuario
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> changeUserStatus(@PathVariable Long id, @RequestBody String estado) {
        try {
            UsuarioResponse updated = usuarioService.changeStatus(id, estado);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}