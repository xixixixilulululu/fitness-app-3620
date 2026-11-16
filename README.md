# Fitness App 3620

An Android fitness app for the **TECH 3620 group project**. It helps users build a practical workout plan around their goals, schedule, equipment, and space, then supports the plan with trainer routines, workout music, progress tracking, and rewards.

> **Status:** Planning and initial setup. Team decisions that still need confirmation are listed below.

## Core experience

**Home → My Plan → Goal → Intensity → Schedule → Equipment & Space → Recommended Plan → Trainer Plan / Music → Save Plan → Complete Workouts → Earn Rewards**

### Personalized plan selection

Users create a plan by choosing:

1. **Goal** — fat loss, build muscle, or stay fit, plus focus area and timeframe
2. **Intensity** — low, moderate, or high
3. **Schedule** — training days per week and session duration
4. **Equipment and space** — available equipment and workout location

The app matches these choices to a small set of preset plans with understandable Java rules. It does not need AI to produce a useful recommendation.

### Featured trainer plans

Trainer plans are a planned core feature, not a future-only add-on. Each sample trainer plan includes the goal, difficulty, weekly schedule, session length, equipment, and a full plan the user can preview and follow.

### Workout music

Music is also part of the planned implementation. Users can select local playlists such as **Focus**, **Energy**, and **Power**, view BPM and recommended intensity, and play or pause tracks during a workout.

### Home, progress, and rewards

- View the active plan and today's workout
- Mark workouts complete
- Track weekly progress and streaks
- Earn points, badges, or milestone rewards

## Navigation

The app has three consistent top-level destinations:

- **Home**
- **My Plan**
- **Rewards**

Secondary screens use a back button instead of adding more bottom-navigation items.

## Team responsibilities

| Area | Owner | Scope |
|---|---|---|
| Home Screen | Nikko | Active plan, today's workout, and progress summary |
| Fitness Plan Selection | Tianrui Liu | Plan builder, recommendations, trainer plans, and workout music |
| Motivation and Rewards | Jimena | Points, streaks, badges, and milestones |
| Additional role / integration | Ryan | To be confirmed with the team |

Shared decisions—navigation, data passed between screens, naming, colors, and reusable components—should be agreed on before individual screens are built.

## Design directions

### Plan B — Airy Active (recommended)

A bright, modern fitness interface with a light background, white cards, blue primary actions, mint progress states, and coral/yellow accents.

### Plan A — Fresh Motion

A stronger alternative using forest green, lime, and peach while keeping the same structure and navigation.

The team can choose either palette without changing the app architecture. See [UI Design System](docs/UI_DESIGN.md) for exact colors and rules.

## Technical approach

- **Language:** Java
- **Interface:** XML layouts
- **UI:** Material Components for Android
- **Local state:** SharedPreferences
- **Music:** MediaPlayer with permitted audio in res/raw
- **Version control:** Git and GitHub

The MVP does not require Firebase, accounts, cloud storage, AI recommendations, Spotify, or a remote database.

## Suggested architecture

- One main activity with Home, My Plan, and Rewards fragments
- A guided multi-step plan builder inside My Plan
- Preset workout-plan data stored locally
- SharedPreferences connecting the selected plan, completed workouts, points, and streaks
- Shared resources in colors.xml, dimens.xml, styles.xml, and themes.xml

## Project documents

- [Full feature specification](docs/FEATURES.md)
- [UI design system](docs/UI_DESIGN.md)
- [Team contribution guide](CONTRIBUTING.md)

## Roadmap

- [ ] Confirm Ryan's responsibility
- [ ] Choose Airy Active or Fresh Motion
- [ ] Agree on navigation and shared data keys
- [ ] Create the Android Studio project structure
- [ ] Build shared colors, spacing, buttons, cards, and navigation
- [ ] Implement Home
- [ ] Implement personalized plan selection
- [ ] Implement featured trainer plans
- [ ] Implement local workout music
- [ ] Implement progress, motivation, and rewards
- [ ] Connect all sections and test the full user journey
- [ ] Prepare the final demonstration and presentation

## Getting started

The Android Studio starter project has not been added yet. Once it is available:

    git clone https://github.com/xixixixilulululu/fitness-app-3620.git
    cd fitness-app-3620

Open the project in Android Studio, allow Gradle to finish syncing, and run it on an emulator or Android device.

## Collaboration

Work on focused feature branches, make small commits, and open pull requests into main. Coordinate before changing navigation, shared resources, Gradle files, the manifest, or SharedPreferences keys. See [CONTRIBUTING.md](CONTRIBUTING.md) for the team workflow.
