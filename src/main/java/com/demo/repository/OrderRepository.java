package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Order;
import com.demo.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Get all orders of a user
    List<Order> findByUser(User user);

    // Check if a user has placed any order
    boolean existsByUser(User user);

}
