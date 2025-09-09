package com.solera.shoppingcart.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.solera.shoppingcart.demo.models.Cart;
import com.solera.shoppingcart.demo.models.Middle;
import com.solera.shoppingcart.demo.repository.CartRepository;
import com.solera.shoppingcart.demo.repository.MiddleRepository;

@Service
public class CartService {
    private final CartRepository repository; // Repositorio para acceder a los datos de Cart
    private final MiddleRepository middleRepository; // Repositorio para acceder a los datos de Middle
    public CartService(CartRepository cartRepository, MiddleRepository middleRepository) { // Constructor que inyecta el repositorio
        repository = cartRepository; // Inicializa el repositorio
        this.middleRepository = middleRepository;
    }
    
    public ResponseEntity<Cart> newCart(Cart cart) {
        if  (cart.getUser()==null || cart.getUser().trim().equals("")) //verificacion de campos nulos
            return new ResponseEntity<>((Cart) null, HttpStatus.BAD_REQUEST);

        repository.save(cart); return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    public ResponseEntity<List<Cart>> allCarts() { // Devuelve una lista de todos los carritos
        List<Cart> carts = repository.findAll();
        return new ResponseEntity<>(carts, carts.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);    
    }
    
    public ResponseEntity<Cart> oneCart(long id) { //Extrae un valor de la URI // Busca el producto por su ID
        Cart cart = repository.findById(id);
        return new ResponseEntity<>(cart, cart==null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    public ResponseEntity<Cart> changedCart(long id, Cart cart) {
        Cart existCart = repository.findById(id);
        if (existCart == null)
            return new ResponseEntity<>((Cart) null, HttpStatus.NOT_FOUND);

        if  (cart.getUser()==null || cart.getUser().trim().equals("")) // Verifica si hay campos nulos
            return new ResponseEntity<>((Cart) null, HttpStatus.BAD_REQUEST);

        existCart.setUser(cart.getUser());
        repository.save(existCart);
        return new ResponseEntity<>(existCart, HttpStatus.OK);
    }
    
    public ResponseEntity<String> offAllCarts() {
        List<Middle> middles = middleRepository.findAll();
        List<Cart> carts = repository.findAll();
        carts.removeIf(cart -> !middles.stream().noneMatch(middle -> middle.getCart().getId().equals(cart.getId())));
        carts.forEach(repository::delete);
        return new ResponseEntity<>("Only empty carts have been removed", HttpStatus.OK);
    }
 
    public ResponseEntity<String> offCart(Long id) {
        Cart cart = repository.findById(id).orElse(null);
        if (cart == null) return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);

        List<Middle> middles = middleRepository.findAll();
        middles.removeIf(item -> !item.getCart().getId().equals(id));
        if (middles.isEmpty()) 
        {
            repository.deleteById(id); // Elimina el carrito por su ID
            return new ResponseEntity<>("Cart deleted successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Cart cannot be deleted", HttpStatus.CONFLICT);
    }
}
