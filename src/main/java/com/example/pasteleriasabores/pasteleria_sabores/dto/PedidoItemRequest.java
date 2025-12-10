package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.Data;

@Data
public class PedidoItemRequest {

    private Long productoId;
    private int cantidad;
    private int precioUnitario;
}
