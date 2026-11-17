package edu.kean.fitnessapp.ui.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.logic.PlanInputValidator;
import edu.kean.fitnessapp.logic.RecommendationEngine;
import edu.kean.fitnessapp.model.PlanRecommendation;
import edu.kean.fitnessapp.model.PlanSelection;

public class PlanBuilderActivity extends AppCompatActivity {
    public static final String EXTRA_SELECTION = "extra_plan_selection";
    public static final String EXTRA_RECOMMENDATION = "extra_plan_recommendation";
    private static final String STATE_STEP = "current_step";

    private final View[] stepContainers = new View[4];
    private int currentStep = 0;
    private TextView stepLabel;
    private ProgressBar progressBar;
    private MaterialButton backButton;
    private MaterialButton continueButton;
    private ScrollView scrollView;
    private boolean updatingEquipment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_builder);

        stepContainers[0] = findViewById(R.id.step_goal_container);
        stepContainers[1] = findViewById(R.id.step_intensity_container);
        stepContainers[2] = findViewById(R.id.step_schedule_container);
        stepContainers[3] = findViewById(R.id.step_equipment_container);
        stepLabel = findViewById(R.id.builder_step_label);
        progressBar = findViewById(R.id.builder_progress);
        backButton = findViewById(R.id.builder_back_button);
        continueButton = findViewById(R.id.builder_continue_button);
        scrollView = findViewById(R.id.builder_scroll);

        setupEquipmentRules();

        if (savedInstanceState != null) {
            currentStep = savedInstanceState.getInt(STATE_STEP, 0);
        }
        updateStep();

        findViewById(R.id.builder_close_button).setOnClickListener(v -> handleBack());
        backButton.setOnClickListener(v -> goToPreviousStep());
        continueButton.setOnClickListener(v -> handleContinue());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_STEP, currentStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        if (currentStep > 0) {
            goToPreviousStep();
        } else {
            finish();
        }
    }

    private void goToPreviousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateStep();
        }
    }

    private void handleContinue() {
        if (!validateCurrentStep()) {
            return;
        }
        if (currentStep < 3) {
            currentStep++;
            updateStep();
            return;
        }

        PlanSelection selection = collectSelection();
        if (!PlanInputValidator.isValid(selection)) {
            Toast.makeText(this, R.string.error_choose_equipment, Toast.LENGTH_SHORT).show();
            return;
        }
        PlanRecommendation recommendation = new RecommendationEngine().recommend(selection);
        Intent intent = new Intent(this, RecommendationActivity.class);
        intent.putExtra(EXTRA_SELECTION, selection);
        intent.putExtra(EXTRA_RECOMMENDATION, recommendation);
        startActivity(intent);
    }

    private void updateStep() {
        for (int i = 0; i < stepContainers.length; i++) {
            stepContainers[i].setVisibility(i == currentStep ? View.VISIBLE : View.GONE);
        }
        stepLabel.setText(getString(R.string.step_format, currentStep + 1));
        progressBar.setProgress(currentStep + 1);
        backButton.setEnabled(currentStep > 0);
        continueButton.setText(currentStep == 3
                ? R.string.see_recommendation
                : R.string.continue_label);
        scrollView.post(() -> scrollView.scrollTo(0, 0));
    }

    private boolean validateCurrentStep() {
        if (currentStep == 0) {
            if (((RadioGroup) findViewById(R.id.goal_group)).getCheckedRadioButtonId() == -1) {
                return showError(R.string.error_choose_goal);
            }
            if (((ChipGroup) findViewById(R.id.focus_group)).getCheckedChipId() == View.NO_ID) {
                return showError(R.string.error_choose_focus);
            }
            if (((ChipGroup) findViewById(R.id.timeframe_group)).getCheckedChipId() == View.NO_ID) {
                return showError(R.string.error_choose_timeframe);
            }
        } else if (currentStep == 1) {
            if (((RadioGroup) findViewById(R.id.intensity_group)).getCheckedRadioButtonId() == -1) {
                return showError(R.string.error_choose_intensity);
            }
        } else if (currentStep == 2) {
            if (((ChipGroup) findViewById(R.id.days_group)).getCheckedChipId() == View.NO_ID) {
                return showError(R.string.error_choose_days);
            }
            if (((ChipGroup) findViewById(R.id.duration_group)).getCheckedChipId() == View.NO_ID) {
                return showError(R.string.error_choose_duration);
            }
        } else {
            if (((ChipGroup) findViewById(R.id.equipment_group)).getCheckedChipIds().isEmpty()) {
                return showError(R.string.error_choose_equipment);
            }
            if (((ChipGroup) findViewById(R.id.space_group)).getCheckedChipId() == View.NO_ID) {
                return showError(R.string.error_choose_space);
            }
        }
        return true;
    }

    private boolean showError(int messageRes) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show();
        return false;
    }

    private PlanSelection collectSelection() {
        String goal = goalFromId(((RadioGroup) findViewById(R.id.goal_group)).getCheckedRadioButtonId());
        String focus = focusFromId(((ChipGroup) findViewById(R.id.focus_group)).getCheckedChipId());
        int weeks = weeksFromId(((ChipGroup) findViewById(R.id.timeframe_group)).getCheckedChipId());
        String intensity = intensityFromId(((RadioGroup) findViewById(R.id.intensity_group)).getCheckedRadioButtonId());
        int days = daysFromId(((ChipGroup) findViewById(R.id.days_group)).getCheckedChipId());
        int minutes = minutesFromId(((ChipGroup) findViewById(R.id.duration_group)).getCheckedChipId());
        Set<String> equipment = equipmentFromCheckedIds(
                ((ChipGroup) findViewById(R.id.equipment_group)).getCheckedChipIds());
        String space = spaceFromId(((ChipGroup) findViewById(R.id.space_group)).getCheckedChipId());
        return new PlanSelection(goal, focus, weeks, intensity, days, minutes, equipment, space);
    }

    private void setupEquipmentRules() {
        Chip none = findViewById(R.id.equipment_none);
        Chip mat = findViewById(R.id.equipment_mat);
        Chip bands = findViewById(R.id.equipment_bands);
        Chip dumbbells = findViewById(R.id.equipment_dumbbells);

        none.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked || updatingEquipment) return;
            updatingEquipment = true;
            mat.setChecked(false);
            bands.setChecked(false);
            dumbbells.setChecked(false);
            updatingEquipment = false;
        });

        View.OnClickListener actualEquipmentClick = v -> {
            Chip chip = (Chip) v;
            if (chip.isChecked() && !updatingEquipment) {
                updatingEquipment = true;
                none.setChecked(false);
                updatingEquipment = false;
            }
        };
        mat.setOnClickListener(actualEquipmentClick);
        bands.setOnClickListener(actualEquipmentClick);
        dumbbells.setOnClickListener(actualEquipmentClick);
    }

    private String goalFromId(int id) {
        if (id == R.id.goal_fat_loss) return PlanSelection.GOAL_FAT_LOSS;
        if (id == R.id.goal_build_muscle) return PlanSelection.GOAL_BUILD_MUSCLE;
        return PlanSelection.GOAL_STAY_FIT;
    }

    private String focusFromId(int id) {
        if (id == R.id.focus_upper_body) return PlanSelection.FOCUS_UPPER_BODY;
        if (id == R.id.focus_lower_body) return PlanSelection.FOCUS_LOWER_BODY;
        if (id == R.id.focus_core) return PlanSelection.FOCUS_CORE;
        if (id == R.id.focus_cardio) return PlanSelection.FOCUS_CARDIO;
        return PlanSelection.FOCUS_FULL_BODY;
    }

    private int weeksFromId(int id) {
        if (id == R.id.weeks_2) return 2;
        if (id == R.id.weeks_6) return 6;
        if (id == R.id.weeks_8) return 8;
        return 4;
    }

    private String intensityFromId(int id) {
        if (id == R.id.intensity_low) return PlanSelection.INTENSITY_LOW;
        if (id == R.id.intensity_high) return PlanSelection.INTENSITY_HIGH;
        return PlanSelection.INTENSITY_MODERATE;
    }

    private int daysFromId(int id) {
        if (id == R.id.days_2) return 2;
        if (id == R.id.days_3) return 3;
        if (id == R.id.days_5) return 5;
        if (id == R.id.days_6) return 6;
        return 4;
    }

    private int minutesFromId(int id) {
        if (id == R.id.minutes_15) return 15;
        if (id == R.id.minutes_45) return 45;
        if (id == R.id.minutes_60) return 60;
        return 30;
    }

    private Set<String> equipmentFromCheckedIds(java.util.List<Integer> ids) {
        Set<String> values = new LinkedHashSet<>();
        for (int id : ids) {
            if (id == R.id.equipment_none) values.add(PlanSelection.EQUIPMENT_NONE);
            if (id == R.id.equipment_mat) values.add(PlanSelection.EQUIPMENT_MAT);
            if (id == R.id.equipment_bands) values.add(PlanSelection.EQUIPMENT_BANDS);
            if (id == R.id.equipment_dumbbells) values.add(PlanSelection.EQUIPMENT_DUMBBELLS);
        }
        return values;
    }

    private String spaceFromId(int id) {
        if (id == R.id.space_small_room) return PlanSelection.SPACE_SMALL_ROOM;
        if (id == R.id.space_large) return PlanSelection.SPACE_LARGE;
        return PlanSelection.SPACE_DORM;
    }
}
