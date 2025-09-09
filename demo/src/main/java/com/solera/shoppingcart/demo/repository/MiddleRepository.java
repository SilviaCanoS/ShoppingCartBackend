package com.solera.shoppingcart.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solera.shoppingcart.demo.models.Middle;

@Repository
public interface MiddleRepository extends JpaRepository<Middle, Long> {
    Middle findById(long middleId);
}