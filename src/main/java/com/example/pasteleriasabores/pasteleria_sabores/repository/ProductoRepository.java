package com.example.pasteleriasabores.pasteleria_sabores.repository;

import com.example.pasteleriasabores.pasteleria_sabores.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByDestacadoTrue();
    List<Producto> findByOfertaTrue();
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}