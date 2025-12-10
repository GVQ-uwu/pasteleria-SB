package com.example.pasteleriasabores.pasteleria_sabores.repository;

import com.example.pasteleriasabores.pasteleria_sabores.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}