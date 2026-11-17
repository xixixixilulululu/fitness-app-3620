package edu.kean.fitnessapp.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class PlanSelection implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String GOAL_FAT_LOSS = "FAT_LOSS";
    public static final String GOAL_BUILD_MUSCLE = "BUILD_MUSCLE";
    public static final String GOAL_STAY_FIT = "STAY_FIT";

    public static final String FOCUS_FULL_BODY = "FULL_BODY";
    public static final String FOCUS_UPPER_BODY = "UPPER_BODY";
    public static final String FOCUS_LOWER_BODY = "LOWER_BODY";
    public static final String FOCUS_CORE = "CORE";
    public static final String FOCUS_CARDIO = "CARDIO";

    public static final String INTENSITY_LOW = "LOW";
    public static final String INTENSITY_MODERATE = "MODERATE";
    public static final String INTENSITY_HIGH = "HIGH";

    public static final String EQUIPMENT_NONE = "NONE";
    public static final String EQUIPMENT_MAT = "YOGA_MAT";
    public static final String EQUIPMENT_BANDS = "RESISTANCE_BANDS";
    public static final String EQUIPMENT_DUMBBELLS = "DUMBBELLS";

    public static final String SPACE_DORM = "DORM";
    public static final String SPACE_SMALL_ROOM = "SMALL_ROOM";
    public static final String SPACE_LARGE = "LARGE_SPACE";

    private final String goal;
    private final String focusArea;
    private final int timeframeWeeks;
    private final String intensity;
    private final int daysPerWeek;
    private final int sessionMinutes;
    private final Set<String> equipment;
    private final String space;

    public PlanSelection(
            String goal,
            String focusArea,
            int timeframeWeeks,
            String intensity,
            int daysPerWeek,
            int sessionMinutes,
            Set<String> equipment,
            String space
    ) {
        this.goal = goal;
        this.focusArea = focusArea;
        this.timeframeWeeks = timeframeWeeks;
        this.intensity = intensity;
        this.daysPerWeek = daysPerWeek;
        this.sessionMinutes = sessionMinutes;
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        if (equipment != null) {
            normalized.addAll(equipment);
        }
        this.equipment = Collections.unmodifiableSet(normalized);
        this.space = space;
    }

    public String getGoal() {
        return goal;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public int getTimeframeWeeks() {
        return timeframeWeeks;
    }

    public String getIntensity() {
        return intensity;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public int getSessionMinutes() {
        return sessionMinutes;
    }

    public Set<String> getEquipment() {
        return equipment;
    }

    public String getSpace() {
        return space;
    }
}
