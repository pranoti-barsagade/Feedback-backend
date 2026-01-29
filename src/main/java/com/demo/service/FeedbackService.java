package com.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.dto.FeedbackRequestDTO;
import com.demo.dto.FeedbackResponseDTO;
import com.demo.model.Feedback;
import com.demo.model.Order;
import com.demo.model.Product;
import com.demo.model.User;
import com.demo.repository.FeedbackRepository;
import com.demo.repository.OrderRepository;
import com.demo.repository.ProductRepository;
import com.demo.repository.UserRepository;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    /* ======================
       CREATE FEEDBACK
       ====================== */
    public FeedbackResponseDTO addFeedback(Integer userId, FeedbackRequestDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Feedback feedback;
        Optional<Feedback> existingFeedback =
                feedbackRepository.findByUserAndProduct(user, product);

        if (existingFeedback.isPresent()) {
            // 🔄 UPDATE EXISTING FEEDBACK
            feedback = existingFeedback.get();
            feedback.setRating(dto.getRating());
            feedback.setComment(dto.getComment());

        }
        else {
        Order order = null;
        if (dto.getOrderId() != null) {
            order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            if (!order.getUser().getUserId().equals(userId)) {
                throw new RuntimeException("Order does not belong to this user");
            }
        }

        feedback = new Feedback();
        feedback.setUser(user);
        feedback.setProduct(product);
        feedback.setOrder(order);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        }
        Feedback saved = feedbackRepository.save(feedback);
        return mapToDTO(saved);
    }

    /* ======================
       GET FEEDBACK BY ID
       ====================== */
    public FeedbackResponseDTO getFeedbackById(Integer id) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        return mapToDTO(feedback);
    }

    /* ======================
       GET ALL FEEDBACKS
       ====================== */
    public List<FeedbackResponseDTO> getAllFeedbacks() {

        return feedbackRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* ======================
       GET FEEDBACK BY PRODUCT
       ====================== */
    public List<FeedbackResponseDTO> getFeedbacksByProduct(Integer productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return feedbackRepository.findByProduct(product)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* ======================
       GET FEEDBACK BY USER
       ====================== */
    public List<FeedbackResponseDTO> getFeedbacksByUser(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return feedbackRepository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* ======================
       DELETE FEEDBACK
       ====================== */
    public void deleteFeedback(Integer feedbackId, Integer userId) {

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (!feedback.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this feedback");
        }

        feedbackRepository.delete(feedback);
    }

    /* ======================
       ENTITY → DTO MAPPER
       ====================== */
    private FeedbackResponseDTO mapToDTO(Feedback feedback) {

        FeedbackResponseDTO dto = new FeedbackResponseDTO();
        dto.setFeedbackId(feedback.getFeedbackId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setUserName(feedback.getUser().getUserName());
        dto.setCreatedAt(feedback.getCreatedAt());
       
        dto.setProductName(feedback.getProduct().getProductName()); 
        // ✅ FIX: map Order → orderId safely
        if (feedback.getOrder() != null) {
            dto.setOrderId(feedback.getOrder().getOrderId());
        } else {
            dto.setOrderId(null);
        }

        return dto;
    }

}
