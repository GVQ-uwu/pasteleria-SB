// UsuarioDTO.java
package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String fechaNacimiento;
    private String rol;
    private String estado;
    private LocalDateTime fechaRegistro;
}