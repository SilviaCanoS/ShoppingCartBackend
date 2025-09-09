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

import com.solera.shoppingcart.demo.models.Cart;
import com.solera.shoppingcart.demo.service.CartService;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1") // Define la ruta base para las solicitudes a este controlador
public class CartController {
    private CartService service; // Inyecta el servicio de productos
    public CartController(CartService cartService) { // Constructor que inyecta el repositorio
        service = cartService; // Inicializa el repositorio
    }

    @PostMapping("/cart")//Mapea la solicitud POST
    public ResponseEntity<Cart> newCart(@RequestBody Cart cart) {
        return service.newCart(cart); // Llama al servicio para crear un nuevo carrito
    }

    @GetMapping("/cart") // Mapea las solicitudes GET para obtener todos los carritos
    public ResponseEntity<List<Cart>> allCarts() { // Devuelve una lista de todos los carritos
        return service.allCarts(); // Llama al servicio para obtener todos los carritos
    }
    
    @GetMapping("/cart/{id}") // Mapea las solicitudes GET par obtener un carrito por su ID
    public ResponseEntity<Cart> oneCart(@PathVariable long id) { //Extrae un valor de la URI
        return service.oneCart(id); // Llama al servicio para obtener un carrito por su ID
    }
 
    @PutMapping("/cart/{id}")// Mapea las solicitudes Put para actualizar un carrito existente
    public ResponseEntity<Cart> changedCart(@PathVariable long id, @RequestBody Cart cart) {
        return service.changedCart(id, cart); // Llama al servicio para actualizar un carrito
    }
    
    @DeleteMapping("/cart")
    public ResponseEntity<String> offAllCarts() {
        return service.offAllCarts(); // Llama al servicio para eliminar todos los carritos
    }
 
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> offCart(@PathVariable Long id) {
        return service.offCart(id); // Llama al servicio para eliminar un carrito por su ID
    }
}
