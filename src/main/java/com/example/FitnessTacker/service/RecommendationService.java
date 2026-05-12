package com.example.FitnessTacker.service;

import com.example.FitnessTacker.Model.Activity;
import com.example.FitnessTacker.Model.Recommendations;
import com.example.FitnessTacker.Model.User;
import com.example.FitnessTacker.dto.RecommendationRequest;
import com.example.FitnessTacker.dto.RecommendationResponse;
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

    public RecommendationResponse generateRecommendation(RecommendationRequest request) {

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

        Recommendations saved = recommendationRepository.save(recommendations);
        return mapToResponse(saved);

    }

    public List<RecommendationResponse> getAllRecommendations(String userId) {
        return recommendationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RecommendationResponse> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RecommendationResponse mapToResponse(Recommendations rec) {
        RecommendationResponse response = new RecommendationResponse();
        response.setId(rec.getId());
        response.setActivityId(rec.getActivity().getId());
        response.setActivityType(rec.getActivity().getType() != null
                ? rec.getActivity().getType().name()
                : null);
        response.setType(rec.getType());
        response.setRecommendation(rec.getRecommendation());
        response.setImprovements(rec.getImprovements());
        response.setSuggestions(rec.getSuggestions());
        response.setSafety(rec.getSafety());
        response.setCreatedAt(rec.getCreatedAt());
        response.setUpdatedAt(rec.getUpdatedAt());
        return response;
    }
}
