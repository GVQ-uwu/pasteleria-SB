package com.example.pasteleriasabores.pasteleria_sabores.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(length = 100)
    private String categoria;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Boolean destacado = false;
    
    @Column(length = 500)
    private String imagen;
    
    @Column(nullable = false)
    private Boolean oferta = false;
    
    @Column(nullable = false)
    private Double precio;
    
    @Column(length = 50)
    private String sku;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Column(length = 50)
    private String tamano;
    
    @Column(length = 50)
    private String tipo;
    
    @Column(nullable = false)
    private Boolean disponible = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (disponible == null) {
            disponible = true;
        }
    }
}