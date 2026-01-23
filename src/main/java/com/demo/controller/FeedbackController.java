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

    /* ======================
       CREATE FEEDBACK
       ====================== */
    @PostMapping("/dd")
    public ResponseEntity<FeedbackResponseDTO> createFeedback(
            @RequestParam Integer userId,
            @Valid @RequestBody FeedbackRequestDTO dto) {

        FeedbackResponseDTO savedFeedback =
                feedbackService.addFeedback(userId, dto);

        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }

    /* ======================
       GET ALL FEEDBACKS
       ====================== */
    @GetMapping({"","/"})   // ✅ ONLY THIS
    public ResponseEntity<List<FeedbackResponseDTO>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }
    /* ======================
       GET FEEDBACK BY ID
       ====================== */
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(
            @PathVariable Integer id) {

        FeedbackResponseDTO feedback =
                feedbackService.getFeedbackById(id);

        return ResponseEntity.ok(feedback);
    }

    /* ======================
       GET FEEDBACK BY PRODUCT
       ====================== */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByProduct(
            @PathVariable Integer productId) {

        List<FeedbackResponseDTO> feedbacks =
                feedbackService.getFeedbacksByProduct(productId);

        return ResponseEntity.ok(feedbacks);
    }

    /* ======================
       GET FEEDBACK BY USER
       ====================== */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByUser(
            @PathVariable Integer userId) {

        List<FeedbackResponseDTO> feedbacks =
                feedbackService.getFeedbacksByUser(userId);

        return ResponseEntity.ok(feedbacks);
    }

    /* ======================
       DELETE FEEDBACK
       ====================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(
            @PathVariable Integer id,
            @RequestParam Integer userId) {

        feedbackService.deleteFeedback(id, userId);
        return ResponseEntity.ok("Feedback deleted successfully");
    }
}
