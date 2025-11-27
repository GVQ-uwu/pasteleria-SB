package com.example.pasteleriasabores.pasteleria_sabores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;
import com.example.pasteleriasabores.pasteleria_sabores.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario detalles) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario != null) {
            usuario.setNombre(detalles.getNombre());
            usuario.setEmail(detalles.getEmail());
            usuario.setPassword(detalles.getPassword());
            usuario.setRoles(detalles.getRoles());

            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
