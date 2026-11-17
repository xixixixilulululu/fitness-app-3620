package edu.kean.fitnessapp.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import edu.kean.fitnessapp.logic.RecommendationEngine;
import edu.kean.fitnessapp.model.PlanRecommendation;
import edu.kean.fitnessapp.model.PlanSelection;
import edu.kean.fitnessapp.model.TrainerPlan;

@RunWith(AndroidJUnit4.class)
public class AppPreferencesInstrumentedTest {
    private static final String PREFERENCE_FILE = "fitness_app_preferences";

    private Context context;
    private AppPreferences preferences;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        clearPreferences();
        preferences = new AppPreferences(context);
    }

    @After
    public void tearDown() {
        clearPreferences();
    }

    @Test
    public void recommendationCompletionUpdatesSharedHomeAndRewardsStateOncePerDay() {
        PlanSelection selection = new PlanSelection(
                PlanSelection.GOAL_FAT_LOSS,
                PlanSelection.FOCUS_CARDIO,
                4,
                PlanSelection.INTENSITY_MODERATE,
                4,
                30,
                Collections.singleton(PlanSelection.EQUIPMENT_NONE),
                PlanSelection.SPACE_DORM
        );
        PlanRecommendation recommendation = new RecommendationEngine().recommend(selection);

        preferences.saveRecommendation(selection, recommendation);

        assertTrue(preferences.hasActivePlan());
        assertEquals(recommendation.getPlan().getId(), preferences.getActivePlanId());
        assertEquals(0, preferences.getWeeklyCompletedWorkouts());
        assertTrue(preferences.completeWorkout());
        assertEquals(1, preferences.getWeeklyCompletedWorkouts());
        assertEquals(1, preferences.getCompletedWorkouts());
        assertEquals(10, preferences.getRewardPoints());
        assertEquals(1, preferences.getCurrentWorkoutIndex());

        assertFalse(preferences.completeWorkout());
        assertEquals(1, preferences.getCompletedWorkouts());
        assertEquals(10, preferences.getRewardPoints());
    }

    @Test
    public void trainerPlanRoundTripUsesStableSourceAndId() {
        TrainerPlan trainerPlan = TrainerRepository.getPlans().get(1);

        preferences.saveTrainerPlan(trainerPlan);

        assertEquals(AppPreferences.SOURCE_TRAINER, preferences.getPlanSource());
        assertEquals(trainerPlan.getId(), preferences.getActivePlanId());
        assertEquals(trainerPlan.getPlanName(), preferences.getActivePlanName());
        assertEquals(trainerPlan.getTrainerName(), preferences.getTrainerName());
        assertEquals(trainerPlan.getDaysPerWeek(), preferences.getDaysPerWeek());
    }

    @Test
    public void selectedMusicRoundTripRestoresIdAndTitle() {
        preferences.saveSelectedMusic("energy", "Energy");

        assertEquals("energy", preferences.getSelectedMusicId());
        assertEquals("Energy", preferences.getSelectedMusicTitle());
    }

    private void clearPreferences() {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();
    }
}
