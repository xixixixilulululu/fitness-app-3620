package edu.kean.fitnessapp.logic;

import edu.kean.fitnessapp.data.PlanRepository;
import edu.kean.fitnessapp.model.PlanRecommendation;
import edu.kean.fitnessapp.model.PlanSelection;
import edu.kean.fitnessapp.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class RecommendationEngine {

    public PlanRecommendation recommend(PlanSelection selection) {
        if (!PlanInputValidator.isValid(selection)) {
            throw new IllegalArgumentException("A complete, valid plan selection is required.");
        }

        List<WorkoutPlan> plans = PlanRepository.getPlans();
        WorkoutPlan bestPlan = null;
        int bestScore = -1;
        int bestIntensityDistance = Integer.MAX_VALUE;
        int bestMinuteDistance = Integer.MAX_VALUE;
        int bestDayDistance = Integer.MAX_VALUE;

        for (WorkoutPlan plan : plans) {
            int score = score(selection, plan);
            if (score < 0) {
                continue;
            }
            int intensityDistance = intensityDistance(selection.getIntensity(), plan.getIntensities());
            int minuteDistance = distanceToRange(selection.getSessionMinutes(), plan.getMinMinutes(), plan.getMaxMinutes());
            int dayDistance = distanceToRange(selection.getDaysPerWeek(), plan.getMinDays(), plan.getMaxDays());

            boolean betterTieBreak = score == bestScore
                    && (intensityDistance < bestIntensityDistance
                    || (intensityDistance == bestIntensityDistance && minuteDistance < bestMinuteDistance)
                    || (intensityDistance == bestIntensityDistance
                    && minuteDistance == bestMinuteDistance
                    && dayDistance < bestDayDistance));

            if (score > bestScore || betterTieBreak) {
                bestPlan = plan;
                bestScore = score;
                bestIntensityDistance = intensityDistance;
                bestMinuteDistance = minuteDistance;
                bestDayDistance = dayDistance;
            }
        }

        if (bestPlan == null) {
            bestPlan = plans.get(0);
            bestScore = Math.max(0, scoreWithoutHardRequirements(selection, bestPlan));
        }

        return new PlanRecommendation(
                bestPlan,
                bestScore,
                buildReasons(selection, bestPlan),
                buildSchedule(selection, bestPlan)
        );
    }

    public int score(PlanSelection selection, WorkoutPlan plan) {
        if (plan.requiresDumbbells()
                && !selection.getEquipment().contains(PlanSelection.EQUIPMENT_DUMBBELLS)) {
            return -1;
        }
        if (spaceRank(selection.getSpace()) < spaceRank(plan.getMinimumSpace())) {
            return -1;
        }
        return scoreWithoutHardRequirements(selection, plan);
    }

    private int scoreWithoutHardRequirements(PlanSelection selection, WorkoutPlan plan) {
        int score = 0;

        if (plan.getGoals().contains(selection.getGoal())) {
            score += 24;
        }

        if (plan.getFocusAreas().contains(selection.getFocusArea())) {
            score += 14;
        } else if (plan.getFocusAreas().contains(PlanSelection.FOCUS_FULL_BODY)
                || PlanSelection.FOCUS_FULL_BODY.equals(selection.getFocusArea())) {
            score += 7;
        }

        int intensityDistance = intensityDistance(selection.getIntensity(), plan.getIntensities());
        if (intensityDistance == 0) {
            score += 14;
        } else if (intensityDistance == 1) {
            score += 7;
        }

        score += rangeScore(selection.getDaysPerWeek(), plan.getMinDays(), plan.getMaxDays(), 1, 10);
        score += rangeScore(selection.getSessionMinutes(), plan.getMinMinutes(), plan.getMaxMinutes(), 15, 10);
        score += equipmentScore(selection.getEquipment(), plan.getSupportedEquipment());
        score += selection.getSpace().equals(plan.getMinimumSpace()) ? 8 : 6;

        if (plan.getSupportedWeeks().contains(selection.getTimeframeWeeks())) {
            score += 8;
        } else if (nearestWeekDistance(selection.getTimeframeWeeks(), plan.getSupportedWeeks()) <= 2) {
            score += 4;
        }

        return Math.max(0, Math.min(100, score));
    }

    private int equipmentScore(Set<String> selected, Set<String> supported) {
        for (String item : selected) {
            if (supported.contains(item)) {
                return 12;
            }
        }
        return supported.contains(PlanSelection.EQUIPMENT_NONE) ? 8 : 4;
    }

    private int rangeScore(int value, int min, int max, int tolerance, int points) {
        int distance = distanceToRange(value, min, max);
        if (distance == 0) {
            return points;
        }
        return distance <= tolerance ? points / 2 : 0;
    }

    private int distanceToRange(int value, int min, int max) {
        if (value < min) {
            return min - value;
        }
        if (value > max) {
            return value - max;
        }
        return 0;
    }

    private int intensityDistance(String selected, Set<String> supported) {
        int selectedRank = intensityRank(selected);
        int best = Integer.MAX_VALUE;
        for (String value : supported) {
            best = Math.min(best, Math.abs(selectedRank - intensityRank(value)));
        }
        return best;
    }

    private int intensityRank(String intensity) {
        if (PlanSelection.INTENSITY_LOW.equals(intensity)) {
            return 0;
        }
        if (PlanSelection.INTENSITY_MODERATE.equals(intensity)) {
            return 1;
        }
        return 2;
    }

    private int spaceRank(String space) {
        if (PlanSelection.SPACE_DORM.equals(space)) {
            return 0;
        }
        if (PlanSelection.SPACE_SMALL_ROOM.equals(space)) {
            return 1;
        }
        return 2;
    }

    private int nearestWeekDistance(int selectedWeeks, Set<Integer> supportedWeeks) {
        int best = Integer.MAX_VALUE;
        for (int weeks : supportedWeeks) {
            best = Math.min(best, Math.abs(selectedWeeks - weeks));
        }
        return best;
    }

    private List<String> buildReasons(PlanSelection selection, WorkoutPlan plan) {
        List<String> reasons = new ArrayList<>();

        if (plan.getGoals().contains(selection.getGoal())) {
            reasons.add("Designed for " + goalLabel(selection.getGoal()));
        } else {
            reasons.add("Closest available preset to your " + goalLabel(selection.getGoal()) + " goal");
        }

        if (plan.getFocusAreas().contains(selection.getFocusArea())) {
            reasons.add("Includes your " + focusLabel(selection.getFocusArea()) + " focus");
        } else if (plan.getFocusAreas().contains(PlanSelection.FOCUS_FULL_BODY)) {
            reasons.add("Uses full-body sessions to support your "
                    + focusLabel(selection.getFocusArea()) + " focus");
        }

        if (intensityDistance(selection.getIntensity(), plan.getIntensities()) == 0) {
            reasons.add("Matches your " + intensityLabel(selection.getIntensity()) + " intensity");
        } else {
            reasons.add("Closest available intensity; the preset supports "
                    + intensitySetLabel(plan.getIntensities()));
        }

        boolean daysMatch = distanceToRange(selection.getDaysPerWeek(), plan.getMinDays(), plan.getMaxDays()) == 0;
        boolean minutesMatch = distanceToRange(selection.getSessionMinutes(), plan.getMinMinutes(), plan.getMaxMinutes()) == 0;
        if (daysMatch && minutesMatch) {
            reasons.add("Matches " + selection.getDaysPerWeek() + " days per week and "
                    + selection.getSessionMinutes() + "-minute sessions");
        } else {
            reasons.add("Schedule adjusted to your " + selection.getDaysPerWeek() + " days and "
                    + selection.getSessionMinutes() + "-minute sessions");
        }

        reasons.add("Works in " + spaceLabel(selection.getSpace()));

        if (selection.getEquipment().contains(PlanSelection.EQUIPMENT_NONE)) {
            reasons.add("No equipment required");
        } else if (selection.getEquipment().contains(PlanSelection.EQUIPMENT_DUMBBELLS)
                && plan.getSupportedEquipment().contains(PlanSelection.EQUIPMENT_DUMBBELLS)) {
            reasons.add("Uses your available dumbbells");
        } else if (selection.getEquipment().contains(PlanSelection.EQUIPMENT_BANDS)
                && plan.getSupportedEquipment().contains(PlanSelection.EQUIPMENT_BANDS)) {
            reasons.add("Uses your resistance bands");
        } else if (selection.getEquipment().contains(PlanSelection.EQUIPMENT_MAT)
                && plan.getSupportedEquipment().contains(PlanSelection.EQUIPMENT_MAT)) {
            reasons.add("Uses your yoga mat");
        }

        if (plan.getSupportedWeeks().contains(selection.getTimeframeWeeks())) {
            reasons.add("Fits your " + selection.getTimeframeWeeks() + "-week timeframe");
        } else {
            reasons.add("Closest available timeframe to your "
                    + selection.getTimeframeWeeks() + "-week target");
        }
        return reasons.subList(0, Math.min(4, reasons.size()));
    }

    private String intensityLabel(String value) {
        return value.toLowerCase(Locale.US);
    }

    private String intensitySetLabel(Set<String> values) {
        StringBuilder labels = new StringBuilder();
        for (String value : values) {
            if (labels.length() > 0) {
                labels.append(" or ");
            }
            labels.append(intensityLabel(value));
        }
        return labels.toString();
    }

    private List<String> buildSchedule(PlanSelection selection, WorkoutPlan plan) {
        List<String> trainingDays = trainingDays(selection.getDaysPerWeek());
        List<String> schedule = new ArrayList<>();
        int workoutIndex = 0;
        for (String day : Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")) {
            if (trainingDays.contains(day)) {
                String workout = plan.getWorkoutSequence().get(workoutIndex % plan.getWorkoutSequence().size());
                schedule.add(day + " — " + workout + " (" + selection.getSessionMinutes() + " min)");
                workoutIndex++;
            } else {
                schedule.add(day + " — Rest / light mobility");
            }
        }
        return schedule;
    }

    private List<String> trainingDays(int daysPerWeek) {
        switch (daysPerWeek) {
            case 2:
                return Arrays.asList("Monday", "Thursday");
            case 3:
                return Arrays.asList("Monday", "Wednesday", "Friday");
            case 4:
                return Arrays.asList("Monday", "Tuesday", "Thursday", "Saturday");
            case 5:
                return Arrays.asList("Monday", "Tuesday", "Wednesday", "Friday", "Saturday");
            case 6:
                return Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
            default:
                return Collections.emptyList();
        }
    }

    private String goalLabel(String value) {
        if (PlanSelection.GOAL_FAT_LOSS.equals(value)) return "fat loss";
        if (PlanSelection.GOAL_BUILD_MUSCLE.equals(value)) return "building muscle";
        return "staying fit";
    }

    private String focusLabel(String value) {
        return value.toLowerCase(Locale.US).replace('_', ' ');
    }

    private String spaceLabel(String value) {
        if (PlanSelection.SPACE_DORM.equals(value)) return "a dorm or very small space";
        if (PlanSelection.SPACE_SMALL_ROOM.equals(value)) return "a small room";
        return "a large indoor or outdoor space";
    }
}
