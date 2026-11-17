package edu.kean.fitnessapp.logic;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Arrays;

import edu.kean.fitnessapp.model.PlanSelection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlanInputValidatorTest {
    @Test
    public void completeSelectionIsValid() {
        assertTrue(PlanInputValidator.isValid(new PlanSelection(
                PlanSelection.GOAL_FAT_LOSS,
                PlanSelection.FOCUS_CARDIO,
                4,
                PlanSelection.INTENSITY_MODERATE,
                3,
                30,
                Collections.singleton(PlanSelection.EQUIPMENT_NONE),
                PlanSelection.SPACE_DORM
        )));
    }

    @Test
    public void unsupportedDurationIsInvalid() {
        assertFalse(PlanInputValidator.isValid(new PlanSelection(
                PlanSelection.GOAL_STAY_FIT,
                PlanSelection.FOCUS_FULL_BODY,
                4,
                PlanSelection.INTENSITY_LOW,
                3,
                25,
                Collections.singleton(PlanSelection.EQUIPMENT_MAT),
                PlanSelection.SPACE_SMALL_ROOM
        )));
    }

    @Test
    public void missingGoalIsInvalid() {
        assertFalse(PlanInputValidator.isValid(new PlanSelection(
                null,
                PlanSelection.FOCUS_CORE,
                4,
                PlanSelection.INTENSITY_LOW,
                2,
                15,
                Collections.singleton(PlanSelection.EQUIPMENT_NONE),
                PlanSelection.SPACE_DORM
        )));
    }

    @Test
    public void missingEquipmentIsInvalid() {
        PlanSelection selection = new PlanSelection(
                PlanSelection.GOAL_STAY_FIT,
                PlanSelection.FOCUS_CORE,
                4,
                PlanSelection.INTENSITY_LOW,
                2,
                15,
                Collections.emptySet(),
                PlanSelection.SPACE_DORM
        );
        assertTrue(selection.getEquipment().isEmpty());
        assertFalse(PlanInputValidator.isValid(selection));
    }

    @Test
    public void noEquipmentCannotBeCombinedWithEquipment() {
        assertFalse(PlanInputValidator.isValid(new PlanSelection(
                PlanSelection.GOAL_STAY_FIT,
                PlanSelection.FOCUS_CORE,
                4,
                PlanSelection.INTENSITY_LOW,
                2,
                15,
                new LinkedHashSet<>(Arrays.asList(
                        PlanSelection.EQUIPMENT_NONE,
                        PlanSelection.EQUIPMENT_MAT
                )),
                PlanSelection.SPACE_DORM
        )));
    }

    @Test
    public void unknownEquipmentIsInvalid() {
        assertFalse(PlanInputValidator.isValid(new PlanSelection(
                PlanSelection.GOAL_STAY_FIT,
                PlanSelection.FOCUS_CORE,
                4,
                PlanSelection.INTENSITY_LOW,
                2,
                15,
                Collections.singleton("TREADMILL"),
                PlanSelection.SPACE_DORM
        )));
    }
}
