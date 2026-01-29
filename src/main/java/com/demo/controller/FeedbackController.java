package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.dto.FeedbackRequestDTO;
import com.demo.dto.FeedbackResponseDTO;
import com.demo.service.FeedbackService;

import jakarta.validation.Valid;
// use javax.validation.Valid if Spring Boot 2.x


@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/dd")
    public ResponseEntity<FeedbackResponseDTO> createFeedback(
            @RequestParam Integer userId,
            @Valid @RequestBody FeedbackRequestDTO dto) {

        return new ResponseEntity<>(
                feedbackService.addFeedback(userId, dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<FeedbackResponseDTO>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByProduct(
            @PathVariable Integer productId) {
        return ResponseEntity.ok(
                feedbackService.getFeedbacksByProduct(productId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(
                feedbackService.getFeedbacksByUser(userId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(
            @PathVariable Integer id,
            @RequestParam Integer userId) {

        feedbackService.deleteFeedback(id, userId);
        return ResponseEntity.ok("Feedback deleted successfully");
    }
}
