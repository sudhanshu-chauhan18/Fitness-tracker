package com.example.FitnessTacker.controller;

import com.example.FitnessTacker.dto.RecommendationRequest;
import com.example.FitnessTacker.dto.RecommendationResponse;
import com.example.FitnessTacker.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/recommendation/")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("generate")
    public ResponseEntity<RecommendationResponse> generateRecommendation(
            @RequestBody RecommendationRequest request
    )
    {
        RecommendationResponse recommendation = recommendationService.generateRecommendation(request);
        return ResponseEntity.ok(recommendation);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendation(@PathVariable String userId){
        List<RecommendationResponse> recommendationsList = recommendationService.getAllRecommendations(userId);
        return ResponseEntity.ok(recommendationsList);
    }

    @GetMapping("activity/{activityId}")
    public ResponseEntity<List<RecommendationResponse>> getActivityRecommendation(@PathVariable String activityId){
        List<RecommendationResponse> recommendationsList = recommendationService.getActivityRecommendation(activityId);
        return ResponseEntity.ok(recommendationsList);
    }

}

