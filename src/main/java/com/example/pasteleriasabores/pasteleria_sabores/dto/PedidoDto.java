package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDto {
    private Long id;
    private LocalDateTime fechaCreacion;
    private String estado;
    private int subtotal;
    private int envio;
    private int descuento;
    private int total;
    private String direccionEntrega;
    private List<PedidoItemDto> items;
}
