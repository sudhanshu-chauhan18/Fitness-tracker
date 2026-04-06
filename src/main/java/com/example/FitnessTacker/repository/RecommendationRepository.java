package com.example.FitnessTacker.repository;

import com.example.FitnessTacker.Model.Recommendations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendations , String> {
    List<Recommendations> findByUserId(String userId);
    List<Recommendations> findByActivityId(String activityId);
}
