package com.example.FitnessTacker.service;

import com.example.FitnessTacker.Model.Activity;
import com.example.FitnessTacker.Model.User;
import com.example.FitnessTacker.dto.ActivityRequest;
import com.example.FitnessTacker.dto.ActivityResponse;
import com.example.FitnessTacker.repository.ActivityRepository;
import com.example.FitnessTacker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        User user = userRepository.findById(activityRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid User Id : " + activityRequest.getUserId())
                );
        Activity activity = Activity.builder()
                .user(user)
                .type(activityRequest.getType())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .duration(activityRequest.getDuration())
                .startTime(activityRequest.getStartTime())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        return mapToResponse(savedActivity);
    }


    private ActivityResponse mapToResponse(Activity savedActivity) {

        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(savedActivity.getId());
        activityResponse.setUserId(savedActivity.getUser().getId());
        activityResponse.setType(savedActivity.getType());
        activityResponse.setDuration(savedActivity.getDuration());
        activityResponse.setCaloriesBurned(savedActivity.getCaloriesBurned());
        activityResponse.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        activityResponse.setStartTime(savedActivity.getStartTime());
        activityResponse.setCreatedAt(savedActivity.getCreatedAt());
        activityResponse.setUpdatedAt(savedActivity.getUpdatedAt());

        return activityResponse;
    }


    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> ActivityList = activityRepository.findByUserId(userId);
        return  ActivityList.stream()            // first convert activity to activityResponse then convert to list.
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
