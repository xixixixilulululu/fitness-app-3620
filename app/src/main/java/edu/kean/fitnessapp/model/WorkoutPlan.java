package edu.kean.fitnessapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class WorkoutPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final Set<String> goals;
    private final Set<String> focusAreas;
    private final Set<String> intensities;
    private final Set<Integer> supportedWeeks;
    private final int minDays;
    private final int maxDays;
    private final int minMinutes;
    private final int maxMinutes;
    private final Set<String> supportedEquipment;
    private final boolean requiresDumbbells;
    private final String minimumSpace;
    private final List<String> workoutSequence;

    public WorkoutPlan(
            String id,
            String name,
            Set<String> goals,
            Set<String> focusAreas,
            Set<String> intensities,
            Set<Integer> supportedWeeks,
            int minDays,
            int maxDays,
            int minMinutes,
            int maxMinutes,
            Set<String> supportedEquipment,
            boolean requiresDumbbells,
            String minimumSpace,
            List<String> workoutSequence
    ) {
        this.id = id;
        this.name = name;
        this.goals = Collections.unmodifiableSet(new LinkedHashSet<>(goals));
        this.focusAreas = Collections.unmodifiableSet(new LinkedHashSet<>(focusAreas));
        this.intensities = Collections.unmodifiableSet(new LinkedHashSet<>(intensities));
        this.supportedWeeks = Collections.unmodifiableSet(new LinkedHashSet<>(supportedWeeks));
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.minMinutes = minMinutes;
        this.maxMinutes = maxMinutes;
        this.supportedEquipment = Collections.unmodifiableSet(new LinkedHashSet<>(supportedEquipment));
        this.requiresDumbbells = requiresDumbbells;
        this.minimumSpace = minimumSpace;
        this.workoutSequence = Collections.unmodifiableList(new ArrayList<>(workoutSequence));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Set<String> getGoals() { return goals; }
    public Set<String> getFocusAreas() { return focusAreas; }
    public Set<String> getIntensities() { return intensities; }
    public Set<Integer> getSupportedWeeks() { return supportedWeeks; }
    public int getMinDays() { return minDays; }
    public int getMaxDays() { return maxDays; }
    public int getMinMinutes() { return minMinutes; }
    public int getMaxMinutes() { return maxMinutes; }
    public Set<String> getSupportedEquipment() { return supportedEquipment; }
    public boolean requiresDumbbells() { return requiresDumbbells; }
    public String getMinimumSpace() { return minimumSpace; }
    public List<String> getWorkoutSequence() { return workoutSequence; }
}
