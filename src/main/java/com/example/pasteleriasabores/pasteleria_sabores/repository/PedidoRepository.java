package com.example.pasteleriasabores.pasteleria_sabores.repository;

import com.example.pasteleriasabores.pasteleria_sabores.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
}

