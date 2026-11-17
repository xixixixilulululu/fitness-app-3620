package edu.kean.fitnessapp.logic;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.kean.fitnessapp.data.PlanRepository;
import edu.kean.fitnessapp.model.PlanRecommendation;
import edu.kean.fitnessapp.model.PlanSelection;
import edu.kean.fitnessapp.model.WorkoutPlan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class RecommendationEngineTest {
    private final RecommendationEngine engine = new RecommendationEngine();

    @Test
    public void cardioInputsRecommendQuickStartCardio() {
        PlanRecommendation result = engine.recommend(selection(
                PlanSelection.GOAL_FAT_LOSS,
                PlanSelection.FOCUS_CARDIO,
                4,
                PlanSelection.INTENSITY_MODERATE,
                4,
                30,
                set(PlanSelection.EQUIPMENT_NONE),
                PlanSelection.SPACE_DORM
        ));
        assertEquals("quick_start_cardio", result.getPlan().getId());
        assertEquals(100, result.getMatchPercent());
    }

    @Test
    public void compactStrengthInputsRecommendDormStrength() {
        PlanRecommendation result = engine.recommend(selection(
                PlanSelection.GOAL_BUILD_MUSCLE,
                PlanSelection.FOCUS_UPPER_BODY,
                4,
                PlanSelection.INTENSITY_MODERATE,
                3,
                30,
                set(PlanSelection.EQUIPMENT_BANDS),
                PlanSelection.SPACE_DORM
        ));
        assertEquals("dorm_strength", result.getPlan().getId());
    }

    @Test
    public void balancedInputsRecommendFullBodyBalance() {
        PlanRecommendation result = engine.recommend(selection(
                PlanSelection.GOAL_STAY_FIT,
                PlanSelection.FOCUS_CORE,
                8,
                PlanSelection.INTENSITY_LOW,
                2,
                30,
                set(PlanSelection.EQUIPMENT_MAT),
                PlanSelection.SPACE_DORM
        ));
        assertEquals("full_body_balance", result.getPlan().getId());
    }

    @Test
    public void highIntensityDumbbellInputsRecommendPowerBuilder() {
        PlanRecommendation result = engine.recommend(selection(
                PlanSelection.GOAL_BUILD_MUSCLE,
                PlanSelection.FOCUS_UPPER_BODY,
                8,
                PlanSelection.INTENSITY_HIGH,
                5,
                60,
                set(PlanSelection.EQUIPMENT_DUMBBELLS),
                PlanSelection.SPACE_LARGE
        ));
        assertEquals("power_builder", result.getPlan().getId());
    }

    @Test
    public void lowImpactCoreInputsRecommendCoreMobility() {
        PlanRecommendation result = engine.recommend(selection(
                PlanSelection.GOAL_STAY_FIT,
                PlanSelection.FOCUS_CORE,
                2,
                PlanSelection.INTENSITY_LOW,
                2,
                15,
                set(PlanSelection.EQUIPMENT_NONE),
                PlanSelection.SPACE_DORM
        ));
        assertEquals("core_mobility", result.getPlan().getId());
    }

    @Test
    public void powerBuilderIsRejectedWithoutDumbbells() {
        PlanSelection selection = selection(
                PlanSelection.GOAL_BUILD_MUSCLE,
                PlanSelection.FOCUS_FULL_BODY,
                8,
                PlanSelection.INTENSITY_HIGH,
                5,
                60,
                set(PlanSelection.EQUIPMENT_NONE),
                PlanSelection.SPACE_LARGE
        );
        WorkoutPlan powerBuilder = PlanRepository.getPlans().get(3);
        assertEquals(-1, engine.score(selection, powerBuilder));
    }

    @Test
    public void everyRecommendationHasBoundedScoreAndSevenDaySchedule() {
        PlanRecommendation result = engine.recommend(selection(
                PlanSelection.GOAL_STAY_FIT,
                PlanSelection.FOCUS_FULL_BODY,
                6,
                PlanSelection.INTENSITY_MODERATE,
                4,
                45,
                set(PlanSelection.EQUIPMENT_BANDS, PlanSelection.EQUIPMENT_MAT),
                PlanSelection.SPACE_SMALL_ROOM
        ));
        assertTrue(result.getMatchPercent() >= 0);
        assertTrue(result.getMatchPercent() <= 100);
        assertEquals(7, result.getWeeklySchedule().size());
    }

    @Test
    public void reasonsDescribeScheduleTradeoffInsteadOfClaimingAnExactFit() {
        PlanRecommendation result = engine.recommend(selection(
                PlanSelection.GOAL_FAT_LOSS,
                PlanSelection.FOCUS_CARDIO,
                4,
                PlanSelection.INTENSITY_LOW,
                2,
                15,
                set(PlanSelection.EQUIPMENT_NONE),
                PlanSelection.SPACE_DORM
        ));
        String reasons = String.join(" ", result.getReasons());
        assertTrue(reasons.contains("Schedule adjusted"));
        assertFalse(reasons.contains("Matches 2 days per week"));
    }

    private PlanSelection selection(
            String goal,
            String focus,
            int weeks,
            String intensity,
            int days,
            int minutes,
            Set<String> equipment,
            String space
    ) {
        return new PlanSelection(goal, focus, weeks, intensity, days, minutes, equipment, space);
    }

    private Set<String> set(String... values) {
        return new LinkedHashSet<>(Arrays.asList(values));
    }
}
