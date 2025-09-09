package com.solera.shoppingcart.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solera.shoppingcart.demo.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findById(long cartId); // Método para encontrar un carrito por su ID
}
