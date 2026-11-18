package edu.kean.fitnessapp.data;

import edu.kean.fitnessapp.model.PlanSelection;
import edu.kean.fitnessapp.model.TrainerPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class TrainerRepository {
    private TrainerRepository() { }

    public static List<TrainerPlan> getPlans() {
        List<TrainerPlan> plans = new ArrayList<>();
        plans.add(new TrainerPlan(
                "maya_lean_energy",
                "Maya Chen",
                "Lean & Energized",
                PlanSelection.GOAL_FAT_LOSS,
                PlanSelection.INTENSITY_MODERATE,
                4,
                30,
                new LinkedHashSet<>(Arrays.asList(PlanSelection.EQUIPMENT_MAT, PlanSelection.EQUIPMENT_BANDS)),
                PlanSelection.SPACE_SMALL_ROOM,
                Arrays.asList("Cardio Circuit", "Full-Body Strength", "Low-Impact Intervals", "Mobility & Core"),
                165,
                75,
                68,
                84
        ));
        plans.add(new TrainerPlan(
                "jordan_dorm_strength",
                "Jordan Brooks",
                "Dorm Strength",
                PlanSelection.GOAL_BUILD_MUSCLE,
                PlanSelection.INTENSITY_MODERATE,
                3,
                35,
                new LinkedHashSet<>(Arrays.asList(PlanSelection.EQUIPMENT_BANDS, PlanSelection.EQUIPMENT_DUMBBELLS)),
                PlanSelection.SPACE_DORM,
                Arrays.asList("Upper-Body Strength", "Lower-Body Strength", "Full-Body Circuit"),
                178,
                64,
                70,
                90
        ));
        plans.add(new TrainerPlan(
                "alex_power_builder",
                "Alex Rivera",
                "Power Builder",
                PlanSelection.GOAL_BUILD_MUSCLE,
                PlanSelection.INTENSITY_HIGH,
                5,
                50,
                new LinkedHashSet<>(Arrays.asList(PlanSelection.EQUIPMENT_DUMBBELLS)),
                PlanSelection.SPACE_SMALL_ROOM,
                Arrays.asList("Upper Power", "Lower Power", "Core & Mobility", "Push & Pull", "Full-Body Challenge"),
                183,
                86,
                90,
                112
        ));
        return plans;
    }

    public static TrainerPlan findById(String id) {
        if (id == null) {
            return null;
        }
        for (TrainerPlan plan : getPlans()) {
            if (id.equals(plan.getId())) {
                return plan;
            }
        }
        return null;
    }
}
