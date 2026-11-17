package edu.kean.fitnessapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.List;
import java.util.Collections;

import edu.kean.fitnessapp.MainActivity;
import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.data.AppPreferences;
import edu.kean.fitnessapp.data.PlanRepository;
import edu.kean.fitnessapp.data.TrainerRepository;
import edu.kean.fitnessapp.model.TrainerPlan;
import edu.kean.fitnessapp.model.WorkoutPlan;

public class HomeFragment extends Fragment {
    private AppPreferences preferences;
    private TextView planName;
    private TextView planSummary;
    private TextView workoutName;
    private TextView progressText;
    private ProgressBar progressBar;
    private MaterialButton completeButton;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new AppPreferences(requireContext());
        planName = view.findViewById(R.id.home_plan_name);
        planSummary = view.findViewById(R.id.home_plan_summary);
        workoutName = view.findViewById(R.id.home_workout_name);
        progressText = view.findViewById(R.id.home_progress_text);
        progressBar = view.findViewById(R.id.home_progress_bar);
        completeButton = view.findViewById(R.id.home_complete_button);

        view.findViewById(R.id.home_open_plan_button).setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).selectNavigationItem(R.id.nav_my_plan);
            }
        });

        completeButton.setOnClickListener(v -> {
            boolean completed = preferences.completeWorkout();
            Toast.makeText(
                    requireContext(),
                    completed ? R.string.workout_completed_message : R.string.already_completed_today,
                    Toast.LENGTH_SHORT
            ).show();
            refresh();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences != null) {
            refresh();
        }
    }

    private void refresh() {
        if (!preferences.hasActivePlan()) {
            planName.setText(R.string.no_active_plan);
            planSummary.setText(R.string.no_active_plan_detail);
            workoutName.setText(R.string.workout_ready);
            completeButton.setEnabled(false);
            completeButton.setText(R.string.complete_workout);
            progressText.setText(getString(R.string.weekly_progress_format, 0, 0));
            progressBar.setMax(1);
            progressBar.setProgress(0);
            return;
        }

        int days = preferences.getDaysPerWeek();
        int minutes = preferences.getSessionMinutes();
        String intensity = displayIntensity(preferences.getIntensity());
        int weeklyCompleted = Math.min(
                preferences.getWeeklyCompletedWorkouts(),
                Math.max(1, days)
        );

        planName.setText(preferences.getActivePlanName());
        planSummary.setText(getString(R.string.plan_summary_format, days, minutes, intensity));
        workoutName.setText(resolveWorkoutTitle(minutes));
        progressText.setText(getString(R.string.weekly_progress_format, weeklyCompleted, days));
        progressBar.setMax(Math.max(1, days));
        progressBar.setProgress(weeklyCompleted);

        boolean completedToday = preferences.hasCompletedToday();
        completeButton.setEnabled(!completedToday);
        completeButton.setText(completedToday ? R.string.completed_today : R.string.complete_workout);
    }

    private String resolveWorkoutTitle(int minutes) {
        List<String> sequence = Collections.emptyList();
        String planId = preferences.getActivePlanId();

        if (AppPreferences.SOURCE_TRAINER.equals(preferences.getPlanSource())) {
            TrainerPlan plan = TrainerRepository.findById(planId);
            if (plan != null) {
                sequence = plan.getWorkoutSequence();
            }
        } else {
            WorkoutPlan plan = PlanRepository.findById(planId);
            if (plan != null) {
                sequence = plan.getWorkoutSequence();
            }
        }

        if (sequence.isEmpty()) {
            return getString(
                    R.string.workout_session_format,
                    preferences.getActivePlanName(),
                    preferences.getCurrentWorkoutIndex() + 1
            );
        }

        int index = preferences.getCurrentWorkoutIndex() % sequence.size();
        return getString(R.string.workout_detail_format, sequence.get(index), minutes);
    }

    private String displayIntensity(String value) {
        String lower = value == null ? "" : value.toLowerCase(Locale.US);
        if (lower.isEmpty()) {
            return "Moderate";
        }
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
