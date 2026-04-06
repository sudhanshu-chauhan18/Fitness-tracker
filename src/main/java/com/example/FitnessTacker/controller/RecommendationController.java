package com.example.FitnessTacker.controller;

import com.example.FitnessTacker.Model.Recommendations;
import com.example.FitnessTacker.dto.RecommendationRequest;
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
    public ResponseEntity<Recommendations> generateRecommendation(
            @RequestBody RecommendationRequest request
    )
    {
        Recommendations recommendation = recommendationService.generateRecommendation(request);
        return ResponseEntity.ok(recommendation);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<Recommendations>> getRecommendation(@PathVariable String userId){
        List<Recommendations> recommendationsList = recommendationService.getAllRecommendations(userId);
        return ResponseEntity.ok(recommendationsList);
    }

    @GetMapping("activity/{activityId}")
    public ResponseEntity<List<Recommendations>> getActivityRecommendation(@PathVariable String activityId){
        List<Recommendations> recommendationsList = recommendationService.getActivityRecommendation(activityId);
        return ResponseEntity.ok(recommendationsList);
    }


}
