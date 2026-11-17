package edu.kean.fitnessapp.data;

import edu.kean.fitnessapp.model.PlanSelection;
import edu.kean.fitnessapp.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class PlanRepository {
    private PlanRepository() { }

    public static List<WorkoutPlan> getPlans() {
        List<WorkoutPlan> plans = new ArrayList<>();

        plans.add(new WorkoutPlan(
                "quick_start_cardio",
                "Quick Start Cardio",
                set(PlanSelection.GOAL_FAT_LOSS, PlanSelection.GOAL_STAY_FIT),
                set(PlanSelection.FOCUS_CARDIO, PlanSelection.FOCUS_FULL_BODY),
                set(PlanSelection.INTENSITY_LOW, PlanSelection.INTENSITY_MODERATE),
                intSet(2, 4, 6),
                3, 5,
                15, 30,
                set(PlanSelection.EQUIPMENT_NONE, PlanSelection.EQUIPMENT_MAT),
                false,
                PlanSelection.SPACE_DORM,
                list("Low-Impact Cardio", "Full-Body Circuit", "Core & Mobility", "Interval Cardio", "Active Recovery")
        ));

        plans.add(new WorkoutPlan(
                "dorm_strength",
                "Dorm Strength",
                set(PlanSelection.GOAL_BUILD_MUSCLE, PlanSelection.GOAL_STAY_FIT),
                set(PlanSelection.FOCUS_FULL_BODY, PlanSelection.FOCUS_UPPER_BODY, PlanSelection.FOCUS_LOWER_BODY),
                set(PlanSelection.INTENSITY_MODERATE, PlanSelection.INTENSITY_HIGH),
                intSet(4, 6, 8),
                3, 4,
                30, 45,
                set(PlanSelection.EQUIPMENT_NONE, PlanSelection.EQUIPMENT_BANDS, PlanSelection.EQUIPMENT_DUMBBELLS),
                false,
                PlanSelection.SPACE_DORM,
                list("Upper Body Strength", "Lower Body Strength", "Full-Body Strength", "Core & Recovery")
        ));

        plans.add(new WorkoutPlan(
                "full_body_balance",
                "Full-Body Balance",
                set(PlanSelection.GOAL_STAY_FIT, PlanSelection.GOAL_FAT_LOSS),
                set(PlanSelection.FOCUS_FULL_BODY, PlanSelection.FOCUS_CORE),
                set(PlanSelection.INTENSITY_LOW, PlanSelection.INTENSITY_MODERATE),
                intSet(4, 6, 8),
                2, 4,
                30, 45,
                set(PlanSelection.EQUIPMENT_NONE, PlanSelection.EQUIPMENT_MAT, PlanSelection.EQUIPMENT_BANDS),
                false,
                PlanSelection.SPACE_DORM,
                list("Full-Body Balance", "Cardio & Core", "Strength Circuit", "Mobility Flow")
        ));

        plans.add(new WorkoutPlan(
                "power_builder",
                "Power Builder",
                set(PlanSelection.GOAL_BUILD_MUSCLE),
                set(PlanSelection.FOCUS_UPPER_BODY, PlanSelection.FOCUS_LOWER_BODY, PlanSelection.FOCUS_FULL_BODY),
                set(PlanSelection.INTENSITY_HIGH),
                intSet(6, 8),
                4, 6,
                45, 60,
                set(PlanSelection.EQUIPMENT_DUMBBELLS),
                true,
                PlanSelection.SPACE_SMALL_ROOM,
                list("Upper Power", "Lower Power", "Push", "Pull", "Legs", "Recovery")
        ));

        plans.add(new WorkoutPlan(
                "core_mobility",
                "Core & Mobility",
                set(PlanSelection.GOAL_STAY_FIT),
                set(PlanSelection.FOCUS_CORE, PlanSelection.FOCUS_FULL_BODY),
                set(PlanSelection.INTENSITY_LOW, PlanSelection.INTENSITY_MODERATE),
                intSet(2, 4, 6, 8),
                2, 4,
                15, 30,
                set(PlanSelection.EQUIPMENT_NONE, PlanSelection.EQUIPMENT_MAT),
                false,
                PlanSelection.SPACE_DORM,
                list("Core Stability", "Mobility Flow", "Core Endurance", "Stretch & Recovery")
        ));

        return plans;
    }

    public static WorkoutPlan findById(String id) {
        if (id == null) {
            return null;
        }
        for (WorkoutPlan plan : getPlans()) {
            if (id.equals(plan.getId())) {
                return plan;
            }
        }
        return null;
    }

    @SafeVarargs
    private static <T> Set<T> set(T... values) {
        return new LinkedHashSet<>(Arrays.asList(values));
    }

    private static Set<Integer> intSet(Integer... values) {
        return new LinkedHashSet<>(Arrays.asList(values));
    }

    private static List<String> list(String... values) {
        return new ArrayList<>(Arrays.asList(values));
    }
}
