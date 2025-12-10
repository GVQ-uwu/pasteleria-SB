package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {

    private String direccionEntrega;
    private int envio;
    private int descuento;

    private List<PedidoItemRequest> items;
}
