# Integration Guide

This document describes the structure and shared contracts in the current Android prototype. It is the reference for combining Home, My Plan, and Rewards without inventing duplicate navigation, preference keys, or visual styles.

## Current Android configuration

| Setting | Current value |
|---|---|
| Root project | `FitnessApp3620` |
| Application ID / namespace | `edu.kean.fitnessapp` |
| Language | Java 8 |
| UI | XML + Material Components Views |
| Minimum Android version | API 21 |
| Compile / target SDK | API 32 |
| Main branch for integration | `main` |
| Tianrui's feature branch | `feature/plan-tianrui` |

Do not change the application ID, SDK levels, or package root in one feature branch without discussing the change with the full team. Those changes affect every source file and commonly cause avoidable merge conflicts.

## Actual package structure

```text
edu.kean.fitnessapp
├── MainActivity                         three-tab host
├── data
│   ├── AppPreferences                  shared local-data gateway
│   ├── MusicRepository                 three licensed offline music tracks
│   ├── PlanRepository                  five preset workout plans
│   └── TrainerRepository               three sample trainer plans
├── logic
│   ├── PlanInputValidator              validates all four builder steps
│   └── RecommendationEngine            deterministic 0–100 match scoring
├── model
│   ├── MusicTrack
│   ├── PlanRecommendation
│   ├── PlanSelection
│   ├── TrainerPlan
│   └── WorkoutPlan
└── ui
    ├── home/HomeFragment               Nikko's integration area
    ├── plan
    │   ├── MyPlanFragment              Plan landing/current-plan summary
    │   ├── PlanBuilderActivity         four-step plan builder
    │   ├── RecommendationActivity      result, schedule, and save action
    │   ├── TrainerPlansActivity        sample trainer-plan selection
    │   └── MusicActivity               local music play/pause/selection
    └── rewards/RewardsFragment         Jimena's integration area
```

`MainActivity` owns the bottom navigation and replaces one top-level fragment at a time. The three stable menu IDs are:

- `nav_home`
- `nav_my_plan`
- `nav_rewards`

Plan Builder, Recommendation, Trainer Plans, and Music are secondary activities. They are already declared as non-exported activities in `AndroidManifest.xml`. Teammates should not add duplicate plan destinations to the bottom navigation.

## SharedPreferences contract

All screens must access saved state through `AppPreferences`. Do not open the preference file directly and do not copy its key strings into Home or Rewards.

- Preference file: `fitness_app_preferences`
- Writes use `apply()`.
- Equipment is returned as a defensive copy of a `Set<String>`.
- Saving a new personal or trainer plan resets `current_workout_index` and weekly completions to `0`.
- Completing a workout is allowed once per calendar day, adds 10 points, advances the workout index, and updates the streak.
- Weekly progress rolls over on Monday while lifetime completions and points remain intact.

### Stored values

| Stored key | Type | Meaning |
|---|---|---|
| `selected_plan_source` | String | `PERSONAL` or `TRAINER` |
| `selected_plan_id` | String | Stable repository plan/trainer ID |
| `selected_plan_name` | String | Name displayed by Home and My Plan |
| `selected_trainer_name` | String | Present only for trainer plans |
| `selected_goal` | String | Goal constant from `PlanSelection` |
| `selected_focus` | String | Focus constant; removed for trainer plans |
| `selected_timeframe_weeks` | int | Personal-plan timeframe; removed for trainer plans |
| `selected_intensity` | String | Intensity constant from `PlanSelection` |
| `selected_days_per_week` | int | Planned sessions each week |
| `selected_session_minutes` | int | Planned minutes per session |
| `selected_equipment` | Set<String> | Equipment constants from `PlanSelection` |
| `selected_space` | String | Space constant from `PlanSelection` |
| `selected_music_id` | String | Stable local music ID |
| `selected_music_title` | String | Music title displayed by My Plan |
| `current_workout_index` | int | Zero-based progress through sessions |
| `completed_workouts` | int | Lifetime MVP completion count |
| `weekly_completed_workouts` | int | Completions since the current Monday |
| `weekly_progress_start` | String | Current Monday in `yyyy-MM-dd` format |
| `reward_points` | int | Ten points per accepted completion |
| `streak_days` | int | Consecutive calendar-day completion count |
| `last_completed_date` | String | Local date in `yyyy-MM-dd` format |

Home resolves `selected_plan_id` through the personal or trainer repository, cycles through the saved plan's real workout sequence, and displays weekly progress separately from the lifetime Rewards count.

### Allowed selection constants

Use the constants in `PlanSelection`, not user-visible labels:

- Goals: `FAT_LOSS`, `BUILD_MUSCLE`, `STAY_FIT`
- Focus: `FULL_BODY`, `UPPER_BODY`, `LOWER_BODY`, `CORE`, `CARDIO`
- Intensity: `LOW`, `MODERATE`, `HIGH`
- Equipment: `NONE`, `YOGA_MAT`, `RESISTANCE_BANDS`, `DUMBBELLS`
- Space: `DORM`, `SMALL_ROOM`, `LARGE_SPACE`

### Public integration methods

Home and Rewards should use these existing `AppPreferences` methods:

- `hasActivePlan()`
- `getActivePlanId()` / `getActivePlanName()`
- `getPlanSource()` / `getTrainerName()`
- `getDaysPerWeek()` / `getSessionMinutes()` / `getIntensity()`
- `getCurrentWorkoutIndex()`
- `getEquipment()`
- `getSelectedMusicId()` / `getSelectedMusicTitle()`
- `getWeeklyCompletedWorkouts()`
- `getCompletedWorkouts()` / `getRewardPoints()` / `getStreakDays()`
- `hasCompletedToday()`
- `completeWorkout()`

Plan screens own these writes:

- `saveRecommendation(selection, recommendation)`
- `saveTrainerPlan(plan)`
- `saveSelectedMusic(id, title)`

If another screen needs new shared data, add one method and one key in `AppPreferences`, document it here, and update every consumer in the same pull request.

## Semantic design resources

Fresh Motion is a temporary implementation preview, not a final or team-approved decision. Layouts must not contain copied hex colors or one-off spacing values, so switching to Airy Active later remains a centralized resource change.

### Colors

Use the semantic resources in `res/values/colors.xml`:

- `color_app_background`
- `color_surface`
- `color_primary` / `color_primary_dark` / `color_on_primary`
- `color_selection`
- `color_mint`
- `color_coral`
- `color_reward`
- `color_text_primary` / `color_text_secondary`
- `color_border`

### Dimensions

Use the shared scale in `dimens.xml`: `space_xs`, `space_sm`, `space_md`, `space_lg`, `space_page`, `space_section`, and `space_xl`. Reuse `card_radius`, `button_radius`, `button_height`, `touch_target`, and the shared text sizes.

### Styles and theme

Prefer the reusable styles already defined in `styles.xml`:

- `Widget.Fitness.Card`
- `Widget.Fitness.PrimaryButton`
- `Widget.Fitness.SecondaryButton`
- `TextAppearance.Fitness.PageTitle`
- `TextAppearance.Fitness.SectionTitle`
- `TextAppearance.Fitness.CardTitle`
- `TextAppearance.Fitness.Body`
- `TextAppearance.Fitness.Metadata`

All user-visible text belongs in `strings.xml`. Shared icons belong in `drawable/`. Do not create a second theme for one feature screen.

## Licensed offline music

The app does not use Spotify, YouTube, streaming, or network playback. It bundles three complete MP3 tracks from Kevin MacLeod’s official Incompetech catalog under Creative Commons Attribution 4.0:

| Resource | Label | BPM | Recommended intensity |
|---|---|---:|---|
| `deliberate_thought.mp3` | Focus — Deliberate Thought | 69 | Low |
| `brain_dance.mp3` | Energy — Brain Dance | 124 | Moderate |
| `cloud_dancer.mp3` | Power — Cloud Dancer | 214 | High |

Keep these filenames stable because `MusicRepository` references their Android resource IDs. Required attribution, official track pages, and license links are recorded in `THIRD_PARTY_NOTICES.md`. Any replacement must have equally clear redistribution rights and update the repository, user-facing credit, and notice file together.

`MusicActivity` owns one `MediaPlayer`, releases it in `onStop()`, restores the selected track label, recommends the track matching the active-plan intensity, and saves only the selected track ID/title. It does not autoplay or attempt background playback.

## Team workflow and conflict avoidance

Recommended ownership boundaries:

| Area | Primary owner | Main files |
|---|---|---|
| Home | Nikko | `ui/home/HomeFragment.java`, `fragment_home.xml` |
| My Plan, recommendations, trainers, music | Tianrui Liu | `ui/plan/`, plan models/repositories/logic, plan layouts |
| Rewards | Jimena | `ui/rewards/RewardsFragment.java`, `fragment_rewards.xml` |
| Remaining role / integration | Ryan | Confirm with the team before creating a branch |

The following files are shared integration points. Announce changes before editing them:

- `MainActivity.java`
- `AndroidManifest.xml`
- `app/build.gradle` and root Gradle files
- `bottom_navigation.xml`
- `AppPreferences.java`
- `colors.xml`, `dimens.xml`, `styles.xml`, `themes.xml`, and shared `strings.xml`

Workflow:

1. Pull the latest `main` before starting a task.
2. Work on one focused feature branch, such as `feature/home-nikko`, `feature/plan-tianrui`, or `feature/rewards-jimena`.
3. Keep changes inside the owned area unless the group agrees on a shared-file change.
4. Make small commits and do not reformat unrelated files.
5. Run the verification commands below.
6. Open a pull request into `main`, explain shared-contract changes, and include screenshots for UI changes.
7. Resolve conflicts on the feature branch; never overwrite another teammate's implementation to make a conflict disappear.

Keep the current implementation on `feature/plan-tianrui` until review. `main` should remain unchanged until the team approves and performs the merge.

Do not commit `local.properties`, `.gradle/`, `.idea/`, `build/`, APKs, keystores, credentials, or generated IDE metadata. The repository `.gitignore` already covers these items.

## Verification before integration

From the repository root, run:

```bash
./gradlew testDebugUnitTest assembleDebug assembleDebugAndroidTest lintDebug
```

The verified automated baseline is 16 JVM unit tests and 6 Android instrumentation tests. On the API 36 emulator used for this branch, the older Android Gradle Plugin 7.3 connected-device orchestration can abort with zero discovered tests. Build and install both APKs, then run the instrumentation APK directly instead:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb shell am instrument -w -r \
  edu.kean.fitnessapp.test/androidx.test.runner.AndroidJUnitRunner
```

Expected result: `OK (6 tests)`. This direct result confirms the UI flows, persistence behavior, and that every bundled track is longer than one minute and starts through `MediaPlayer`; it does not replace final physical-device and accessibility checks.

Also verify manually that:

- all four Plan Builder steps validate and retain prior choices;
- recommendation scores remain between 0 and 100;
- saving a personal or trainer plan updates My Plan and Home;
- one workout can be completed per day and Rewards updates immediately;
- all three local tracks can play, pause, switch, and release cleanly;
- Home, My Plan, and Rewards remain reachable through the shared bottom navigation;
- no screen introduces hard-coded colors, duplicate preference keys, or network-only behavior.
