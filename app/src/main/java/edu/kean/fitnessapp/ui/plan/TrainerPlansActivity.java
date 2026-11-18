package edu.kean.fitnessapp.ui.plan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.kean.fitnessapp.MainActivity;
import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.data.AppPreferences;
import edu.kean.fitnessapp.data.TrainerRepository;
import edu.kean.fitnessapp.model.TrainerPlan;

public class TrainerPlansActivity extends AppCompatActivity {
    private List<TrainerPlan> trainerPlans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_plans);

        trainerPlans = TrainerRepository.getPlans();
        bindProfileMetrics(R.id.maya_profile_metrics, trainerPlans.get(0));
        bindProfileMetrics(R.id.jordan_profile_metrics, trainerPlans.get(1));
        bindProfileMetrics(R.id.alex_profile_metrics, trainerPlans.get(2));
        findViewById(R.id.trainer_back_button).setOnClickListener(v -> finish());
        findViewById(R.id.follow_maya_button).setOnClickListener(v -> savePlan(trainerPlans.get(0)));
        findViewById(R.id.follow_jordan_button).setOnClickListener(v -> savePlan(trainerPlans.get(1)));
        findViewById(R.id.follow_alex_button).setOnClickListener(v -> savePlan(trainerPlans.get(2)));
    }

    private void bindProfileMetrics(int textViewId, TrainerPlan plan) {
        int totalInches = (int) Math.round(plan.getHeightCm() / 2.54);
        int feet = totalInches / 12;
        int inches = totalInches % 12;
        int startingWeightLb = (int) Math.round(plan.getStartingWeightKg() * 2.2046226218);
        int currentWeightLb = (int) Math.round(plan.getCurrentWeightKg() * 2.2046226218);

        TextView metrics = findViewById(textViewId);
        metrics.setText(getString(
                R.string.trainer_profile_metrics_format,
                plan.getHeightCm(),
                feet,
                inches,
                plan.getStartingWeightKg(),
                startingWeightLb,
                plan.getCurrentWeightKg(),
                currentWeightLb,
                plan.getExampleJourneyDays()
        ));
    }

    private void savePlan(TrainerPlan plan) {
        new AppPreferences(this).saveTrainerPlan(plan);
        Toast.makeText(this, R.string.trainer_plan_saved, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MainActivity.EXTRA_DESTINATION, MainActivity.DESTINATION_HOME);
        startActivity(intent);
        finish();
    }
}
