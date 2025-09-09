package com.solera.shoppingcart.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solera.shoppingcart.demo.models.Middle;
import com.solera.shoppingcart.demo.service.MiddleService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1") // Define la ruta base para las solicitudes a este controlador
public class MiddleController {
    private MiddleService service;
    public MiddleController(MiddleService middleService){
        service = middleService;
    }

    @PostMapping("/relation")//Mapea la solicitud POST
    public ResponseEntity<String> newProduct(@RequestBody Middle middle) {
        return service.newMiddle(middle); // Llama al servicio para crear una nueva relación
    }

    @GetMapping("/relation") // Mapea las solicitudes GET para obtener todas las relaciones
    public ResponseEntity<List<Middle>> allMiddles() { // Devuelve una lista de todas las relaciones
        return service.allMiddles(); // Llama al servicio para obtener todas las relaciones
    }
    
    @GetMapping("/relation/cart/{id}") // Mapea las solicitudes GET par obtener las relaciones por carrito
    public ResponseEntity<List<Middle>> oneCart(@PathVariable long id) { //Extrae un valor de la URI
        return service.oneCart(id); 
    }

    @GetMapping("/relation/product/{id}") // Mapea las solicitudes GET par obtener las relaciones por producto
    public ResponseEntity<List<Middle>> oneProduct(@PathVariable long id) { //Extrae un valor de la URI
        return service.oneProduct(id); 
    }

    @GetMapping("/relation/{idCart}/{idProduct}") // Mapea las solicitudes GET par obtener las relaciones por producto
    public ResponseEntity<Middle> oneMiddle(@PathVariable long idCart, @PathVariable long idProduct) { //Extrae un valor de la URI
        return service.oneMiddle(idCart, idProduct); // Llama al servicio para obtener una relación específica
    }

    @PutMapping("/relation/{idCart}/{idProduct}") // Mapea las solicitudes GET par obtener las relaciones por producto
    public ResponseEntity<Middle> changeMiddle(@PathVariable long idCart, 
        @PathVariable long idProduct, @RequestBody Middle middle) { //Extrae un valor de la URI
        return service.changeMiddle(idCart, idProduct, middle); // Llama al servicio para obtener una relación específica
    }

    @DeleteMapping("/relation") // Mapea las solicitudes DELETE para eliminar todas las relaciones
    public ResponseEntity<String> offAllMiddles() {
        return service.offAllMiddles(); // Llama al servicio para eliminar todas las relaciones
    }

    @DeleteMapping("/relation/cart/{id}") // Mapea las solicitudes DELETE para eliminar una relación por ID
    public ResponseEntity<String> offAllCarts(@PathVariable Long id) {
        return service.offAllCarts(id); // Llama al servicio para eliminar una relación por su ID
    }

    @DeleteMapping("/relation/product/{id}") // Mapea las solicitudes DELETE para eliminar una relación por ID
    public ResponseEntity<String> offAllProducts(@PathVariable Long id) {
        return service.offAllProducts(id); // Llama al servicio para eliminar una relación por su ID
    }

    @DeleteMapping("/relation/{idCart}/{idProduct}") // Mapea las solicitudes GET par obtener las relaciones por producto
    public ResponseEntity<String> offMiddle(@PathVariable long idCart, @PathVariable long idProduct) { //Extrae un valor de la URI
        return service.offMiddle(idCart, idProduct); // Llama al servicio para obtener una relación específica
    }
}
