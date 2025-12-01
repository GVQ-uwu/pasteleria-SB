package com.example.pasteleriasabores.pasteleria_sabores.controller;

import com.example.pasteleriasabores.pasteleria_sabores.model.Producto;
import com.example.pasteleriasabores.pasteleria_sabores.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    // 1. Todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoRepository.findAll());
    }
    
    // 2. Producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 3. Productos por categoría (NUEVO)
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> getProductosByCategoria(@PathVariable String categoria) {
        List<Producto> productos = productoRepository.findByCategoria(categoria);
        return ResponseEntity.ok(productos);
    }
    
    // 4. Productos destacados (NUEVO)
    @GetMapping("/destacados")
    public ResponseEntity<List<Producto>> getProductosDestacados() {
        List<Producto> productos = productoRepository.findByDestacadoTrue();
        return ResponseEntity.ok(productos);
    }
    
    // 5. Productos en oferta (NUEVO)
    @GetMapping("/ofertas")
    public ResponseEntity<List<Producto>> getProductosOferta() {
        List<Producto> productos = productoRepository.findByOfertaTrue();
        return ResponseEntity.ok(productos);
    }
    
    // 6. Crear producto
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        Producto saved = productoRepository.save(producto);
        return ResponseEntity.ok(saved);
    }
    
    // 7. Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        producto.setId(id);
        Producto updated = productoRepository.save(producto);
        return ResponseEntity.ok(updated);
    }
    
    // 8. Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}