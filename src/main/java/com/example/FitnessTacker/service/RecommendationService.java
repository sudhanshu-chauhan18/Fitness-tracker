package com.example.FitnessTacker.service;

import com.example.FitnessTacker.Model.Activity;
import com.example.FitnessTacker.Model.Recommendations;
import com.example.FitnessTacker.Model.User;
import com.example.FitnessTacker.dto.RecommendationRequest;
import com.example.FitnessTacker.repository.ActivityRepository;
import com.example.FitnessTacker.repository.RecommendationRepository;
import com.example.FitnessTacker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public Recommendations generateRecommendation(RecommendationRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not found with this ID : " + request.getUserId()));

        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new RuntimeException("Activity Not found with this ID : " + request.getActivityId()));

        Recommendations recommendations = Recommendations.builder()
                .user(user)
                .activity(activity)
                .improvements(request.getImprovements())
                .safety(request.getSafety())
                .suggestions(request.getSuggestions())
                .build();

        return recommendationRepository.save(recommendations);

    }

    public List<Recommendations> getAllRecommendations(String userId) {
        List<Recommendations> recommendationsList = recommendationRepository.findByUserId(userId);
        return recommendationsList;
    }

    public List<Recommendations> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}
