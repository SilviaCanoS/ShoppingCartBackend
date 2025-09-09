package com.solera.shoppingcart.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solera.shoppingcart.demo.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(long productId); // Método para encontrar un producto por su ID
}
