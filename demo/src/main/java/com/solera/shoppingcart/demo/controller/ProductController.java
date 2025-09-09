package com.solera.shoppingcart.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solera.shoppingcart.demo.models.Product;
import com.solera.shoppingcart.demo.service.ProductService;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1") // Define la ruta base para las solicitudes a este controlador
public class ProductController {

    private ProductService service; // Inyecta el servicio de productos
    public ProductController(ProductService productService) { // Constructor que inyecta el repositorio
        service = productService; // Inicializa el repositorio
    }

    @PostMapping("/product")//Mapea la solicitud POST
    public ResponseEntity<Product> newProduct(@RequestBody Product product) {
        return service.newProduct(product); // Llama al servicio para crear un nuevo producto
    }

    @GetMapping("/product") // Mapea las solicitudes GET para obtener todos los productos
    public ResponseEntity<List<Product>> allProducts() { // Devuelve una lista de todos los productos
        return service.allProducts(); // Llama al servicio para obtener todos los productos
    }
    
    @GetMapping("/product/{id}") // Mapea las solicitudes GET par obtener un producto por su ID
    public ResponseEntity<Product> oneProduct(@PathVariable long id) { //Extrae un valor de la URI
        return service.oneProduct(id); // Llama al servicio para obtener un producto por su ID
    }
 
    @PutMapping("/product/{id}")// Mapea las solicitudes Put para actualizar un producto existente
    public ResponseEntity<Product> changedProduct(@PathVariable long id, @RequestBody Product product) {
        return service.changedProduct(id, product); // Llama al servicio para actualizar un producto
    }
    
    @DeleteMapping("/product")
    public ResponseEntity<String> offAllProducts() {
        return service.offAllProducts(); // Llama al servicio para eliminar todos los productos
    }
 
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> offProduct(@PathVariable Long id) {
        return service.offProduct(id); // Llama al servicio para eliminar un producto por su ID
    }
}
