package com.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Feedback;
import com.demo.model.Product;
import com.demo.model.User;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    // Get all feedbacks for a product
    List<Feedback> findByProduct(Product product);

    // Get all feedbacks by a user
    List<Feedback> findByUser(User user);

    // Check if user already gave feedback for a product
    boolean existsByUserAndProduct(User user, Product product);
    
    Optional<Feedback> findByUserAndProduct(User user, Product product);

}
