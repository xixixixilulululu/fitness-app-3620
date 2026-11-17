package edu.kean.fitnessapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import edu.kean.fitnessapp.model.PlanRecommendation;
import edu.kean.fitnessapp.model.PlanSelection;
import edu.kean.fitnessapp.model.TrainerPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public final class AppPreferences {
    private static final String FILE_NAME = "fitness_app_preferences";

    public static final String SOURCE_PERSONAL = "PERSONAL";
    public static final String SOURCE_TRAINER = "TRAINER";

    private static final String KEY_PLAN_SOURCE = "selected_plan_source";
    private static final String KEY_PLAN_ID = "selected_plan_id";
    private static final String KEY_PLAN_NAME = "selected_plan_name";
    private static final String KEY_TRAINER_NAME = "selected_trainer_name";
    private static final String KEY_GOAL = "selected_goal";
    private static final String KEY_FOCUS = "selected_focus";
    private static final String KEY_TIMEFRAME = "selected_timeframe_weeks";
    private static final String KEY_INTENSITY = "selected_intensity";
    private static final String KEY_DAYS = "selected_days_per_week";
    private static final String KEY_MINUTES = "selected_session_minutes";
    private static final String KEY_EQUIPMENT = "selected_equipment";
    private static final String KEY_SPACE = "selected_space";
    private static final String KEY_MUSIC_ID = "selected_music_id";
    private static final String KEY_MUSIC_TITLE = "selected_music_title";
    private static final String KEY_CURRENT_WORKOUT = "current_workout_index";
    private static final String KEY_COMPLETED = "completed_workouts";
    private static final String KEY_WEEKLY_COMPLETED = "weekly_completed_workouts";
    private static final String KEY_WEEK_START = "weekly_progress_start";
    private static final String KEY_POINTS = "reward_points";
    private static final String KEY_STREAK = "streak_days";
    private static final String KEY_LAST_COMPLETED = "last_completed_date";

    private final SharedPreferences preferences;

    public AppPreferences(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveRecommendation(PlanSelection selection, PlanRecommendation recommendation) {
        preferences.edit()
                .putString(KEY_PLAN_SOURCE, SOURCE_PERSONAL)
                .putString(KEY_PLAN_ID, recommendation.getPlan().getId())
                .putString(KEY_PLAN_NAME, recommendation.getPlan().getName())
                .remove(KEY_TRAINER_NAME)
                .putString(KEY_GOAL, selection.getGoal())
                .putString(KEY_FOCUS, selection.getFocusArea())
                .putInt(KEY_TIMEFRAME, selection.getTimeframeWeeks())
                .putString(KEY_INTENSITY, selection.getIntensity())
                .putInt(KEY_DAYS, selection.getDaysPerWeek())
                .putInt(KEY_MINUTES, selection.getSessionMinutes())
                .putStringSet(KEY_EQUIPMENT, new LinkedHashSet<>(selection.getEquipment()))
                .putString(KEY_SPACE, selection.getSpace())
                .putInt(KEY_CURRENT_WORKOUT, 0)
                .putInt(KEY_WEEKLY_COMPLETED, 0)
                .putString(KEY_WEEK_START, currentWeekStart())
                .apply();
    }

    public void saveTrainerPlan(TrainerPlan plan) {
        preferences.edit()
                .putString(KEY_PLAN_SOURCE, SOURCE_TRAINER)
                .putString(KEY_PLAN_ID, plan.getId())
                .putString(KEY_PLAN_NAME, plan.getPlanName())
                .putString(KEY_TRAINER_NAME, plan.getTrainerName())
                .putString(KEY_GOAL, plan.getGoal())
                .remove(KEY_FOCUS)
                .remove(KEY_TIMEFRAME)
                .putString(KEY_INTENSITY, plan.getIntensity())
                .putInt(KEY_DAYS, plan.getDaysPerWeek())
                .putInt(KEY_MINUTES, plan.getSessionMinutes())
                .putStringSet(KEY_EQUIPMENT, new LinkedHashSet<>(plan.getEquipment()))
                .putString(KEY_SPACE, plan.getSpace())
                .putInt(KEY_CURRENT_WORKOUT, 0)
                .putInt(KEY_WEEKLY_COMPLETED, 0)
                .putString(KEY_WEEK_START, currentWeekStart())
                .apply();
    }

    public boolean hasActivePlan() {
        return preferences.contains(KEY_PLAN_ID) && preferences.contains(KEY_PLAN_NAME);
    }

    public String getActivePlanName() {
        return preferences.getString(KEY_PLAN_NAME, "");
    }

    public String getActivePlanId() {
        return preferences.getString(KEY_PLAN_ID, "");
    }

    public String getPlanSource() {
        return preferences.getString(KEY_PLAN_SOURCE, SOURCE_PERSONAL);
    }

    public String getTrainerName() {
        return preferences.getString(KEY_TRAINER_NAME, "");
    }

    public int getDaysPerWeek() {
        return preferences.getInt(KEY_DAYS, 0);
    }

    public int getSessionMinutes() {
        return preferences.getInt(KEY_MINUTES, 0);
    }

    public String getIntensity() {
        return preferences.getString(KEY_INTENSITY, PlanSelection.INTENSITY_MODERATE);
    }

    public int getCurrentWorkoutIndex() {
        return preferences.getInt(KEY_CURRENT_WORKOUT, 0);
    }

    public Set<String> getEquipment() {
        Set<String> values = preferences.getStringSet(KEY_EQUIPMENT, null);
        return values == null ? new LinkedHashSet<String>() : new LinkedHashSet<>(values);
    }

    public void saveSelectedMusic(String id, String title) {
        preferences.edit()
                .putString(KEY_MUSIC_ID, id)
                .putString(KEY_MUSIC_TITLE, title)
                .apply();
    }

    public String getSelectedMusicTitle() {
        return preferences.getString(KEY_MUSIC_TITLE, "");
    }

    public String getSelectedMusicId() {
        return preferences.getString(KEY_MUSIC_ID, "");
    }

    public int getCompletedWorkouts() {
        return preferences.getInt(KEY_COMPLETED, 0);
    }

    public int getWeeklyCompletedWorkouts() {
        ensureCurrentWeek();
        return preferences.getInt(KEY_WEEKLY_COMPLETED, 0);
    }

    public int getRewardPoints() {
        return preferences.getInt(KEY_POINTS, 0);
    }

    public int getStreakDays() {
        return preferences.getInt(KEY_STREAK, 0);
    }

    public boolean hasCompletedToday() {
        return today().equals(preferences.getString(KEY_LAST_COMPLETED, ""));
    }

    public boolean completeWorkout() {
        if (!hasActivePlan() || hasCompletedToday()) {
            return false;
        }

        String previousDate = preferences.getString(KEY_LAST_COMPLETED, "");
        int nextStreak = isYesterday(previousDate) ? getStreakDays() + 1 : 1;
        int weeklyCompleted = getWeeklyCompletedWorkouts();

        preferences.edit()
                .putInt(KEY_COMPLETED, getCompletedWorkouts() + 1)
                .putInt(KEY_WEEKLY_COMPLETED, weeklyCompleted + 1)
                .putString(KEY_WEEK_START, currentWeekStart())
                .putInt(KEY_POINTS, getRewardPoints() + 10)
                .putInt(KEY_STREAK, nextStreak)
                .putInt(KEY_CURRENT_WORKOUT, getCurrentWorkoutIndex() + 1)
                .putString(KEY_LAST_COMPLETED, today())
                .apply();
        return true;
    }

    private void ensureCurrentWeek() {
        String currentStart = currentWeekStart();
        String savedStart = preferences.getString(KEY_WEEK_START, "");
        if (!currentStart.equals(savedStart)) {
            preferences.edit()
                    .putString(KEY_WEEK_START, currentStart)
                    .putInt(KEY_WEEKLY_COMPLETED, 0)
                    .apply();
        }
    }

    private String currentWeekStart() {
        Calendar calendar = Calendar.getInstance();
        int daysFromMonday = (calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7;
        calendar.add(Calendar.DAY_OF_YEAR, -daysFromMonday);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.getTime());
    }

    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    private boolean isYesterday(String dateValue) {
        if (dateValue == null || dateValue.isEmpty()) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setLenient(false);
        try {
            Date previous = format.parse(dateValue);
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_YEAR, -1);
            return previous != null && format.format(previous).equals(format.format(yesterday.getTime()));
        } catch (ParseException ignored) {
            return false;
        }
    }
}
