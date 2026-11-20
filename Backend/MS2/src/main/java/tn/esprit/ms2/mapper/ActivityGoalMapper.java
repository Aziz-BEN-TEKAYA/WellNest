package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.ActivityGoalRequest;
import tn.esprit.ms2.dto.ActivityGoalResponse;
import tn.esprit.ms2.entity.ActivityGoal;

@Component
public class ActivityGoalMapper {

    public ActivityGoal toEntity(ActivityGoalRequest request) {
        ActivityGoal goal = new ActivityGoal();
        updateEntity(goal, request);
        return goal;
    }

    public void updateEntity(ActivityGoal goal, ActivityGoalRequest request) {
        goal.setUserId(request.userId());
        goal.setGoalType(request.goalType());
        goal.setTargetValue(request.targetValue());
        goal.setStartDate(request.startDate());
        goal.setEndDate(request.endDate());
        goal.setActive(request.active() == null || request.active());
    }

    public ActivityGoalResponse toResponse(ActivityGoal goal) {
        return new ActivityGoalResponse(
                goal.getId(),
                goal.getUserId(),
                goal.getGoalType(),
                goal.getTargetValue(),
                goal.getStartDate(),
                goal.getEndDate(),
                goal.isActive());
    }
}
