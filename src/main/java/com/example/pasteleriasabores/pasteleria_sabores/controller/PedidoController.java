package com.example.pasteleriasabores.pasteleria_sabores.controller;

import com.example.pasteleriasabores.pasteleria_sabores.dto.PedidoDto;
import com.example.pasteleriasabores.pasteleria_sabores.dto.PedidoRequest;
import com.example.pasteleriasabores.pasteleria_sabores.model.Pedido;
import com.example.pasteleriasabores.pasteleria_sabores.security.JwtUtil;
import com.example.pasteleriasabores.pasteleria_sabores.service.PedidoService;
import com.example.pasteleriasabores.pasteleria_sabores.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:3000")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    // ===========================
    // CLIENTE: Crear pedido
    // ===========================
    @PostMapping("/crear")
    public PedidoDto crearPedido(
            @RequestHeader("Authorization") String token,
            @RequestBody PedidoRequest pedidoRequest) {
        if (token == null || !token.startsWith("Bearer "))
            throw new RuntimeException("Token inválido");

        String email = jwtUtil.extraerEmail(token.substring(7));
        Long usuarioId = usuarioService.obtenerIdPorEmail(email);

        Pedido nuevo = pedidoService.crearPedido(usuarioId, pedidoRequest);
        return pedidoService.toDto(nuevo);
    }
}
