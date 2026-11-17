package edu.kean.fitnessapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.kean.fitnessapp.ui.home.HomeFragment;
import edu.kean.fitnessapp.ui.plan.MyPlanFragment;
import edu.kean.fitnessapp.ui.rewards.RewardsFragment;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_DESTINATION = "extra_destination";
    public static final String DESTINATION_HOME = "HOME";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            showDestination(item.getItemId());
            return true;
        });

        if (savedInstanceState == null) {
            navigateTo(destinationFromIntent(getIntent()));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        navigateTo(destinationFromIntent(intent));
    }

    public void selectNavigationItem(int itemId) {
        navigateTo(itemId);
    }

    private int destinationFromIntent(Intent intent) {
        if (intent != null
                && DESTINATION_HOME.equals(intent.getStringExtra(EXTRA_DESTINATION))) {
            return R.id.nav_home;
        }
        return R.id.nav_home;
    }

    private void navigateTo(int itemId) {
        if (bottomNavigationView.getSelectedItemId() == itemId) {
            showDestination(itemId);
        } else {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    private void showDestination(int itemId) {
        Fragment fragment;
        if (itemId == R.id.nav_my_plan) {
            fragment = new MyPlanFragment();
        } else if (itemId == R.id.nav_rewards) {
            fragment = new RewardsFragment();
        } else {
            fragment = new HomeFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
}
