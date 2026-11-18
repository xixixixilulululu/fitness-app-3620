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
    private final int heightCm;
    private final double startingWeightKg;
    private final double currentWeightKg;
    private final int exampleJourneyDays;

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
            List<String> workoutSequence,
            int heightCm,
            double startingWeightKg,
            double currentWeightKg,
            int exampleJourneyDays
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
        this.heightCm = heightCm;
        this.startingWeightKg = startingWeightKg;
        this.currentWeightKg = currentWeightKg;
        this.exampleJourneyDays = exampleJourneyDays;
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
    public int getHeightCm() { return heightCm; }
    public double getStartingWeightKg() { return startingWeightKg; }
    public double getCurrentWeightKg() { return currentWeightKg; }
    public int getExampleJourneyDays() { return exampleJourneyDays; }
}
