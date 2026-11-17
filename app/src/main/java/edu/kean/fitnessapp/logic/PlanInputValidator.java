package edu.kean.fitnessapp.logic;

import edu.kean.fitnessapp.model.PlanSelection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class PlanInputValidator {
    private static final Set<String> GOALS = new HashSet<>(Arrays.asList(
            PlanSelection.GOAL_FAT_LOSS,
            PlanSelection.GOAL_BUILD_MUSCLE,
            PlanSelection.GOAL_STAY_FIT
    ));
    private static final Set<String> FOCUS_AREAS = new HashSet<>(Arrays.asList(
            PlanSelection.FOCUS_FULL_BODY,
            PlanSelection.FOCUS_UPPER_BODY,
            PlanSelection.FOCUS_LOWER_BODY,
            PlanSelection.FOCUS_CORE,
            PlanSelection.FOCUS_CARDIO
    ));
    private static final Set<String> INTENSITIES = new HashSet<>(Arrays.asList(
            PlanSelection.INTENSITY_LOW,
            PlanSelection.INTENSITY_MODERATE,
            PlanSelection.INTENSITY_HIGH
    ));
    private static final Set<Integer> WEEKS = new HashSet<>(Arrays.asList(2, 4, 6, 8));
    private static final Set<Integer> MINUTES = new HashSet<>(Arrays.asList(15, 30, 45, 60));
    private static final Set<String> EQUIPMENT = new HashSet<>(Arrays.asList(
            PlanSelection.EQUIPMENT_NONE,
            PlanSelection.EQUIPMENT_MAT,
            PlanSelection.EQUIPMENT_BANDS,
            PlanSelection.EQUIPMENT_DUMBBELLS
    ));
    private static final Set<String> SPACES = new HashSet<>(Arrays.asList(
            PlanSelection.SPACE_DORM,
            PlanSelection.SPACE_SMALL_ROOM,
            PlanSelection.SPACE_LARGE
    ));

    private PlanInputValidator() { }

    public static boolean isValid(PlanSelection selection) {
        return selection != null
                && GOALS.contains(selection.getGoal())
                && FOCUS_AREAS.contains(selection.getFocusArea())
                && WEEKS.contains(selection.getTimeframeWeeks())
                && INTENSITIES.contains(selection.getIntensity())
                && selection.getDaysPerWeek() >= 2
                && selection.getDaysPerWeek() <= 6
                && MINUTES.contains(selection.getSessionMinutes())
                && selection.getEquipment() != null
                && !selection.getEquipment().isEmpty()
                && EQUIPMENT.containsAll(selection.getEquipment())
                && !(selection.getEquipment().size() > 1
                && selection.getEquipment().contains(PlanSelection.EQUIPMENT_NONE))
                && SPACES.contains(selection.getSpace());
    }
}
