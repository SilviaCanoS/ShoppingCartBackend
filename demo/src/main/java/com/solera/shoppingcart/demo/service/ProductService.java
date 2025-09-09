package com.solera.shoppingcart.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.solera.shoppingcart.demo.models.Middle;
import com.solera.shoppingcart.demo.models.Product;
import com.solera.shoppingcart.demo.repository.MiddleRepository;
import com.solera.shoppingcart.demo.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository repository; // Repositorio para acceder a los datos de Product
    private final MiddleRepository middleRepository; // Repositorio para acceder a los datos de Middle
    public ProductService(ProductRepository productRepository, MiddleRepository middleRepository) { // Constructor que inyecta el repositorio
        repository = productRepository; // Inicializa el repositorio
        this.middleRepository = middleRepository;
    }
    
    public ResponseEntity<Product> newProduct(Product product) {
        if  (product.getName()==null || product.getName().trim().equals("") || product.getDescription()==null
            || product.getDescription().trim().equals("") || product.getPrice()==null || product.getPrice()<=0) //verificacion de campos nulos
            return new ResponseEntity<>((Product) null, org.springframework.http.HttpStatus.BAD_REQUEST);
        
        repository.save(product); //almacena el nuevo objeto product creado
        return new ResponseEntity<>(product, org.springframework.http.HttpStatus.CREATED);
    }

    public ResponseEntity<List<Product>> allProducts() { // Devuelve una lista de todos los productos
        List<Product> products = repository.findAll();
        return new ResponseEntity<>(products, products.isEmpty() ? 
            org.springframework.http.HttpStatus.NOT_FOUND : org.springframework.http.HttpStatus.OK);    
    }
    
    public ResponseEntity<Product> oneProduct(long id) { //Extrae un valor de la URI // Busca el producto por su ID
        Product product = repository.findById(id);
        return new ResponseEntity<>(product, product==null ? 
            org.springframework.http.HttpStatus.NOT_FOUND : org.springframework.http.HttpStatus.OK);        
    }
 
    public ResponseEntity<Product> changedProduct(long id, Product product) {
        Product existProduct = repository.findById(id);
        if (existProduct == null) 
            return new ResponseEntity<>((Product) null, org.springframework.http.HttpStatus.NOT_FOUND);

        if  (product.getName()==null || product.getName().trim().equals("") || product.getDescription()==null
            || product.getDescription().trim().equals("") || product.getPrice()==null || product.getPrice()<=0) // Verifica si hay campos nulos
            return new ResponseEntity<>((Product) null, org.springframework.http.HttpStatus.BAD_REQUEST);

        existProduct.setName(product.getName());
        existProduct.setDescription(product.getDescription());
        existProduct.setPrice(product.getPrice());
        repository.save(existProduct);
        return new ResponseEntity<>(existProduct, org.springframework.http.HttpStatus.OK);
    }
    
    public ResponseEntity<String> offAllProducts() {
        List<Middle> middles = middleRepository.findAll();
        List<Product> products = repository.findAll();
        products.removeIf(product -> !middles.stream().noneMatch(middle -> middle.getProduct().getId().equals(product.getId())));
        products.forEach(repository::delete);
        return new ResponseEntity<>("Only products that are not in any cart have been removed", HttpStatus.OK);
    }
 
    public ResponseEntity<String> offProduct(Long id) {
        Product product = repository.findById(id).orElse(null);
        if (product == null) return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);

        List<Middle> middles = middleRepository.findAll();
        middles.removeIf(item -> !item.getCart().getId().equals(id));
        if (middles.isEmpty()) {
            repository.deleteById(id); // Elimina el producto por su ID
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Product cannot be deleted", HttpStatus.CONFLICT);
    }
}
