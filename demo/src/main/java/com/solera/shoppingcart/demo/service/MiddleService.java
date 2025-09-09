package com.solera.shoppingcart.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.solera.shoppingcart.demo.models.Cart;
import com.solera.shoppingcart.demo.models.Middle;
import com.solera.shoppingcart.demo.models.Product;
import com.solera.shoppingcart.demo.repository.CartRepository;
import com.solera.shoppingcart.demo.repository.MiddleRepository;
import com.solera.shoppingcart.demo.repository.ProductRepository;

@Service
public class MiddleService {
    private final MiddleRepository repository; // Repositorio para acceder a los datos 
    private final CartRepository cartRepository; // Repositorio para acceder a los datos de Cart
    private final ProductRepository productRepository; // Repositorio para acceder a los datos de Product
    public MiddleService(MiddleRepository middleRepository, CartRepository cartRepository, ProductRepository productRepository) { // Constructor que inyecta el repositorio
        repository = middleRepository; // Inicializa el repositorio
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    public ResponseEntity<String> newMiddle(Middle middle) {
        if  (middle.getCart()==null || middle.getCart().getId()==null || middle.getCart().getId()<=0 
            || middle.getProduct()==null || middle.getProduct().getId()==null || middle.getProduct().getId()<=0 
            || middle.getQuantity()==null || middle.getQuantity()<=0) //verificacion de campos nulos
            return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);

        Cart cart = cartRepository.findById(middle.getCart().getId()).orElse(null);
        Product product = productRepository.findById(middle.getProduct().getId()).orElse(null);
        if (cart==null || product==null) return new ResponseEntity<>("Relation does not exist", HttpStatus.NOT_FOUND);
        
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getCart().getId() != middle.getCart().getId() 
            || item.getProduct().getId() != middle.getProduct().getId());
        if (middles.isEmpty()) 
        {
            middle.setCart(cart); // Establece la relación con el carrito
            middle.setProduct(product); // Establece la relación con el producto
            repository.save(middle); //almacena el nuevo objeto creado

            middles = repository.findAll(); // Obtiene todas las relaciones del carrito
            middles.removeIf(item -> item.getCart().getId() != middle.getCart().getId());
            int quantity = 0;
            double price = 0.0;
            for(int i=0; i<middles.size(); i++) 
            { //Obtiene la cantidad y el precio total de los productos en el carrito
                quantity += middles.get(i).getQuantity();
                price += middles.get(i).getProduct().getPrice() * middles.get(i).getQuantity();
            }

            return new ResponseEntity<>("Total of products: " + quantity + "\nTotal price: " + price, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Relation already exists", HttpStatus.CONFLICT);
    }

    public ResponseEntity<List<Middle>> allMiddles() { 
        List<Middle> middles = repository.findAll();
        return new ResponseEntity<>(middles, middles.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);    
    }
    
    public ResponseEntity<List<Middle>> oneCart(long cart) { //Busca por el id del carrito
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getCart().getId() != cart); // Filtra por el carrito
        return new ResponseEntity<>(middles, middles.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);        
    }

    public ResponseEntity<List<Middle>> oneProduct(long product) { //Busca por el id del producto
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getProduct().getId() != product); // Filtra por el producto
        return new ResponseEntity<>(middles, middles.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);        
    }

    public ResponseEntity<Middle> oneMiddle(long idCart, long idProduct)
    {
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getCart().getId() != idCart || item.getProduct().getId() != idProduct);
        if (middles.isEmpty()) return new ResponseEntity<>((Middle) null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(middles.get(0), HttpStatus.OK);
    }

    public ResponseEntity<Middle> changeMiddle(long idCart, long idProduct, Middle middle) {
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getCart().getId() != idCart || item.getProduct().getId() != idProduct);
        if (middles.isEmpty()) return new ResponseEntity<>((Middle) null, HttpStatus.NOT_FOUND);

        Middle existMiddle = middles.get(0);
        if  (middle.getQuantity()==null || middle.getQuantity()<=0) 
            return new ResponseEntity<>((Middle) null, HttpStatus.BAD_REQUEST);

        existMiddle.setQuantity(middle.getQuantity());
        repository.save(existMiddle);
        return new ResponseEntity<>(existMiddle, HttpStatus.OK);
    }
    
    public ResponseEntity<String> offAllMiddles() {
        List<Middle> middles = repository.findAll();
        middles.forEach(repository::delete); // Elimina todas las relaciones
        return new ResponseEntity<>("Every relation has been removed", HttpStatus.OK);
    }

    public ResponseEntity<String> offAllCarts(long cart) {
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getCart().getId() != cart); // Filtra por el carrito
        middles.forEach(repository::delete); // Elimina todas las relaciones
        return new ResponseEntity<>("Every relation with cart id " + cart + " has been removed", HttpStatus.OK);
    }

    public ResponseEntity<String> offAllProducts(long product) {
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getProduct().getId() != product); // Filtra por el producto
        middles.forEach(repository::delete); // Elimina todas las relaciones
        return new ResponseEntity<>("Every relation with product id " + product + " has been removed", HttpStatus.OK);
    }

    public ResponseEntity<String> offMiddle(long idCart, long idProduct)
    {
        List<Middle> middles = repository.findAll();
        middles.removeIf(item -> item.getCart().getId() != idCart || item.getProduct().getId() != idProduct);
        if (middles.isEmpty()) return new ResponseEntity<>("Relation not found", HttpStatus.NOT_FOUND);
        
        middles.forEach(repository::delete);
        return new ResponseEntity<>("Relation removed", HttpStatus.OK);
    }
}
