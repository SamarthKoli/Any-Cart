package com.anycart.anycart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anycart.anycart.entities.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
