package edu.kean.fitnessapp.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

import edu.kean.fitnessapp.model.TrainerPlan;

public class TrainerRepositoryTest {

    @Test
    public void everySampleProfileHasUsableComparisonMetrics() {
        List<TrainerPlan> plans = TrainerRepository.getPlans();

        assertEquals(3, plans.size());
        for (TrainerPlan plan : plans) {
            assertTrue(plan.getHeightCm() > 0);
            assertTrue(plan.getStartingWeightKg() > 0);
            assertTrue(plan.getCurrentWeightKg() > 0);
            assertTrue(plan.getExampleJourneyDays() > 0);
        }
    }

    @Test
    public void findByIdPreservesJordanComparisonMetrics() {
        TrainerPlan plan = TrainerRepository.findById("jordan_dorm_strength");

        assertNotNull(plan);
        assertEquals(178, plan.getHeightCm());
        assertEquals(64, plan.getStartingWeightKg(), 0.0);
        assertEquals(70, plan.getCurrentWeightKg(), 0.0);
        assertEquals(90, plan.getExampleJourneyDays());
    }
}
