package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductoDto{
    private Long id;
    private String nombre;
    
    // Para estructura actual (categoría como String)
    private String categoriaNombre;
    
    // Para nueva estructura (categoría como FK)
    private Long categoriaId;
    private String categoriaNombreFK;
    
    private String descripcion;
    private Boolean destacado;
    private Boolean oferta;
    private String imagen;
    private String imagenUrl;
    private Double precio;
    private String sku;
    private Integer stock;
    private String tamano;
    private String tipo;
    private Boolean disponible;
    private LocalDateTime fechaCreacion;
    
    // Método helper para obtener la imagen correcta
    public String getImagenParaMostrar() {
        return imagenUrl != null ? imagenUrl : imagen;
    }
    
    // Método helper para obtener la categoría correcta
    public String getCategoriaParaMostrar() {
        return categoriaNombreFK != null ? categoriaNombreFK : categoriaNombre;
    }
}