package edu.kean.fitnessapp.ui.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.data.AppPreferences;

public class MyPlanFragment extends Fragment {
    private AppPreferences preferences;
    private TextView planName;
    private TextView planSummary;
    private TextView selectedMusic;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_my_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new AppPreferences(requireContext());
        planName = view.findViewById(R.id.my_plan_name);
        planSummary = view.findViewById(R.id.my_plan_summary);
        selectedMusic = view.findViewById(R.id.selected_music_text);

        view.findViewById(R.id.build_plan_button).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), PlanBuilderActivity.class)));
        view.findViewById(R.id.trainer_plans_button).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), TrainerPlansActivity.class)));
        view.findViewById(R.id.music_button).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), MusicActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences != null) {
            refresh();
        }
    }

    private void refresh() {
        if (preferences.hasActivePlan()) {
            planName.setText(preferences.getActivePlanName());
            String summary = getString(
                    R.string.plan_summary_format,
                    preferences.getDaysPerWeek(),
                    preferences.getSessionMinutes(),
                    displayIntensity(preferences.getIntensity())
            );
            if (AppPreferences.SOURCE_TRAINER.equals(preferences.getPlanSource())
                    && !preferences.getTrainerName().isEmpty()) {
                summary = getString(R.string.trainer_summary_format, preferences.getTrainerName(), summary);
            }
            planSummary.setText(summary);
        } else {
            planName.setText(R.string.no_active_plan);
            planSummary.setText(R.string.no_active_plan_detail);
        }

        String musicTitle = preferences.getSelectedMusicTitle();
        if (musicTitle.isEmpty()) {
            selectedMusic.setVisibility(View.GONE);
        } else {
            selectedMusic.setText(getString(R.string.selected_music_format, musicTitle));
            selectedMusic.setVisibility(View.VISIBLE);
        }
    }

    private String displayIntensity(String value) {
        String lower = value == null ? "" : value.toLowerCase(Locale.US);
        if (lower.isEmpty()) return "Moderate";
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
