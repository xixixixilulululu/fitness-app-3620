package edu.kean.fitnessapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlanRecommendation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final WorkoutPlan plan;
    private final int matchPercent;
    private final List<String> reasons;
    private final List<String> weeklySchedule;

    public PlanRecommendation(
            WorkoutPlan plan,
            int matchPercent,
            List<String> reasons,
            List<String> weeklySchedule
    ) {
        this.plan = plan;
        this.matchPercent = Math.max(0, Math.min(100, matchPercent));
        this.reasons = Collections.unmodifiableList(new ArrayList<>(reasons));
        this.weeklySchedule = Collections.unmodifiableList(new ArrayList<>(weeklySchedule));
    }

    public WorkoutPlan getPlan() { return plan; }
    public int getMatchPercent() { return matchPercent; }
    public List<String> getReasons() { return reasons; }
    public List<String> getWeeklySchedule() { return weeklySchedule; }
}
