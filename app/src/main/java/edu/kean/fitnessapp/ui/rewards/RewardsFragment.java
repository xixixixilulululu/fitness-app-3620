package edu.kean.fitnessapp.ui.rewards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.data.AppPreferences;

public class RewardsFragment extends Fragment {
    private AppPreferences preferences;
    private TextView points;
    private TextView completed;
    private TextView streak;
    private TextView milestone;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_rewards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new AppPreferences(requireContext());
        points = view.findViewById(R.id.reward_points_value);
        completed = view.findViewById(R.id.completed_workouts_value);
        streak = view.findViewById(R.id.streak_value);
        milestone = view.findViewById(R.id.milestone_text);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences != null) {
            int completedCount = preferences.getCompletedWorkouts();
            points.setText(getString(R.string.points_format, preferences.getRewardPoints()));
            completed.setText(getString(R.string.count_format, completedCount));
            int streakDays = preferences.getStreakDays();
            streak.setText(getResources().getQuantityString(
                    R.plurals.days_format,
                    streakDays,
                    streakDays
            ));

            if (completedCount == 0) {
                milestone.setText(R.string.milestone_first);
            } else if (completedCount < 3) {
                milestone.setText(R.string.milestone_three);
            } else if (completedCount < 7) {
                milestone.setText(R.string.milestone_week);
            } else {
                milestone.setText(R.string.milestone_consistency);
            }
        }
    }
}
