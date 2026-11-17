package edu.kean.fitnessapp.ui.plan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import edu.kean.fitnessapp.MainActivity;
import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.data.AppPreferences;
import edu.kean.fitnessapp.model.PlanRecommendation;
import edu.kean.fitnessapp.model.PlanSelection;

public class RecommendationActivity extends AppCompatActivity {
    private PlanSelection selection;
    private PlanRecommendation recommendation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        selection = (PlanSelection) getIntent().getSerializableExtra(PlanBuilderActivity.EXTRA_SELECTION);
        recommendation = (PlanRecommendation) getIntent().getSerializableExtra(PlanBuilderActivity.EXTRA_RECOMMENDATION);

        if (selection == null || recommendation == null) {
            Toast.makeText(this, R.string.missing_recommendation, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bindRecommendation();
        findViewById(R.id.recommendation_back_button).setOnClickListener(v -> finish());
        findViewById(R.id.change_answers_button).setOnClickListener(v -> finish());
        findViewById(R.id.recommendation_trainers_button).setOnClickListener(v ->
                startActivity(new Intent(this, TrainerPlansActivity.class)));
        findViewById(R.id.recommendation_music_button).setOnClickListener(v ->
                startActivity(new Intent(this, MusicActivity.class)));
        findViewById(R.id.save_plan_button).setOnClickListener(v -> savePlan());
    }

    private void bindRecommendation() {
        TextView match = findViewById(R.id.recommendation_match);
        TextView planName = findViewById(R.id.recommendation_plan_name);
        TextView details = findViewById(R.id.recommendation_details);
        TextView reasons = findViewById(R.id.recommendation_reasons);
        TextView schedule = findViewById(R.id.recommendation_schedule);

        int score = recommendation.getMatchPercent();
        match.setText(score >= 60
                ? getString(R.string.match_format, score)
                : getString(R.string.closest_match_format, score));
        planName.setText(recommendation.getPlan().getName());
        details.setText(getString(
                R.string.recommendation_details_format,
                selection.getTimeframeWeeks(),
                selection.getDaysPerWeek(),
                selection.getSessionMinutes(),
                displayIntensity(selection.getIntensity())
        ));
        reasons.setText(joinLines(recommendation.getReasons(), "• "));
        schedule.setText(joinLines(recommendation.getWeeklySchedule(), ""));
    }

    private void savePlan() {
        new AppPreferences(this).saveRecommendation(selection, recommendation);
        Toast.makeText(this, R.string.plan_saved, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MainActivity.EXTRA_DESTINATION, MainActivity.DESTINATION_HOME);
        startActivity(intent);
        finish();
    }

    private String joinLines(List<String> lines, String prefix) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) builder.append('\n');
            builder.append(prefix).append(lines.get(i));
        }
        return builder.toString();
    }

    private String displayIntensity(String value) {
        String lower = value.toLowerCase(Locale.US);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
