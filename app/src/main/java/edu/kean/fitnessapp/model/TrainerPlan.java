package edu.kean.fitnessapp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public final class TrainerPlan {
    private final String id;
    private final String trainerName;
    private final String planName;
    private final String goal;
    private final String intensity;
    private final int daysPerWeek;
    private final int sessionMinutes;
    private final Set<String> equipment;
    private final String space;
    private final List<String> workoutSequence;

    public TrainerPlan(
            String id,
            String trainerName,
            String planName,
            String goal,
            String intensity,
            int daysPerWeek,
            int sessionMinutes,
            Set<String> equipment,
            String space,
            List<String> workoutSequence
    ) {
        this.id = id;
        this.trainerName = trainerName;
        this.planName = planName;
        this.goal = goal;
        this.intensity = intensity;
        this.daysPerWeek = daysPerWeek;
        this.sessionMinutes = sessionMinutes;
        this.equipment = Collections.unmodifiableSet(new LinkedHashSet<>(equipment));
        this.space = space;
        this.workoutSequence = Collections.unmodifiableList(new ArrayList<>(workoutSequence));
    }

    public String getId() { return id; }
    public String getTrainerName() { return trainerName; }
    public String getPlanName() { return planName; }
    public String getGoal() { return goal; }
    public String getIntensity() { return intensity; }
    public int getDaysPerWeek() { return daysPerWeek; }
    public int getSessionMinutes() { return sessionMinutes; }
    public Set<String> getEquipment() { return equipment; }
    public String getSpace() { return space; }
    public List<String> getWorkoutSequence() { return workoutSequence; }
}
