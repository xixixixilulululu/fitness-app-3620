package edu.kean.fitnessapp.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.kean.fitnessapp.MainActivity;
import edu.kean.fitnessapp.R;

@RunWith(AndroidJUnit4.class)
public class CompletePlanFlowInstrumentedTest {
    private static final String PREFERENCE_FILE = "fitness_app_preferences";

    private Context context;
    private Activity launchedActivity;

    @Before
    public void clearStateBeforeTest() {
        context = ApplicationProvider.getApplicationContext();
        clearPreferences();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchedActivity = InstrumentationRegistry.getInstrumentation().startActivitySync(intent);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @After
    public void clearStateAfterTest() {
        if (launchedActivity != null) {
            InstrumentationRegistry.getInstrumentation().runOnMainSync(
                    () -> launchedActivity.finishAndRemoveTask()
            );
        }
        clearPreferences();
    }

    @Test
    public void buildSaveCompleteAndRewardFlowWorksEndToEnd() {
        onView(withId(R.id.nav_my_plan)).perform(click());
        onView(withId(R.id.build_plan_button)).perform(scrollTo(), click());

        onView(withId(R.id.goal_fat_loss)).perform(scrollTo(), click());
        onView(withId(R.id.focus_cardio)).perform(scrollTo(), click());
        onView(withId(R.id.weeks_4)).perform(scrollTo(), click());
        onView(withId(R.id.builder_continue_button)).perform(scrollTo(), click());

        onView(withId(R.id.intensity_moderate)).perform(scrollTo(), click());
        onView(withId(R.id.builder_continue_button)).perform(scrollTo(), click());

        onView(withId(R.id.days_4)).perform(scrollTo(), click());
        onView(withId(R.id.minutes_30)).perform(scrollTo(), click());
        onView(withId(R.id.builder_continue_button)).perform(scrollTo(), click());

        onView(withId(R.id.equipment_none)).perform(scrollTo(), click());
        onView(withId(R.id.space_dorm)).perform(scrollTo(), click());
        onView(withId(R.id.builder_continue_button)).perform(scrollTo(), click());

        onView(withId(R.id.recommendation_plan_name))
                .check(matches(withText("Quick Start Cardio")));
        onView(withId(R.id.save_plan_button)).perform(scrollTo(), click());

        onView(withId(R.id.home_plan_name))
                .check(matches(withText("Quick Start Cardio")));
        onView(withId(R.id.home_workout_name))
                .check(matches(withText("Low-Impact Cardio • 30 min")));
        onView(withId(R.id.home_complete_button)).perform(scrollTo(), click());

        onView(withId(R.id.nav_rewards)).perform(click());
        onView(withId(R.id.reward_points_value))
                .check(matches(withText("10 pts")));
        onView(withId(R.id.completed_workouts_value))
                .check(matches(withText("1")));
        onView(withId(R.id.rewards_page_title)).check(matches(isDisplayed()));
    }

    @Test
    public void trainerPlanAndMusicFlowWorksEndToEnd() {
        onView(withId(R.id.nav_my_plan)).perform(click());
        onView(withId(R.id.trainer_plans_button)).perform(scrollTo(), click());

        onView(withText(R.string.trainer_jordan_schedule))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
        onView(withId(R.id.jordan_profile_image))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(withContentDescription(R.string.trainer_jordan_image_description)));
        onView(withId(R.id.jordan_profile_metrics))
                .perform(scrollTo())
                .check(matches(withText(containsString("Height: 178 cm"))))
                .check(matches(withText(containsString("Example journey: 90 days"))));
        onView(withId(R.id.follow_jordan_button)).perform(scrollTo(), click());

        onView(withId(R.id.home_plan_name))
                .check(matches(withText("Dorm Strength")));
        onView(withId(R.id.home_workout_name))
                .check(matches(withText("Upper-Body Strength • 35 min")));

        onView(withId(R.id.nav_my_plan)).perform(click());
        onView(withId(R.id.music_button)).perform(scrollTo(), click());
        onView(withId(R.id.music_energy_badge))
                .check(matches(withText(R.string.music_recommended_badge)));

        onView(withId(R.id.play_energy_button)).perform(scrollTo(), click());
        onView(withId(R.id.music_status))
                .check(matches(withText("Now playing: Brain Dance")));
        onView(withId(R.id.play_energy_button)).perform(click());
        onView(withId(R.id.play_energy_button)).check(matches(withText(R.string.play)));

        onView(withId(R.id.stop_music_button)).perform(scrollTo(), click());
        onView(withId(R.id.music_status))
                .check(matches(withText("Selected: Brain Dance • Playback stopped")));
    }

    private void clearPreferences() {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();
    }
}
