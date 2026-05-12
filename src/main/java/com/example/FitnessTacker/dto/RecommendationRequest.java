package com.example.FitnessTacker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationRequest {

    private String userId;
    private String activityId;
    List<String> improvements;
    List<String> suggestions;
    List<String> safety;

}
