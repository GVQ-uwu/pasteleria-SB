package com.example.pasteleriasabores.pasteleria_sabores.repository;

import com.example.pasteleriasabores.pasteleria_sabores.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}