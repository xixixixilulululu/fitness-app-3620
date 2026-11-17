# Fitness App 3620

An Android fitness app for the **TECH 3620 group project**. The working MVP lets a user build or follow a workout plan, select workout music, complete daily workouts, and see progress and rewards—all with local data and no account or network connection.

> **Current implementation branch:** `feature/plan-tianrui`
>
> **MVP status:** The Debug build, all 14 JVM unit tests, and all 5 on-device instrumentation tests pass. The branch is ready for team review, physical-device/accessibility QA, and integration.

## Implemented user journey

**Home → My Plan → Build a Plan → Recommended Plan → Save → Home → Complete Workout → Rewards**

Users can also choose a featured trainer plan or select music from **My Plan**.

### Home

- Shows the saved plan, schedule, intensity, and next workout session
- Displays weekly progress toward the selected number of training days
- Records one workout completion per day
- Advances the workout count and awards 10 points after a completion

### Personalized plan builder

The four-step builder validates each step before continuing:

1. **Goal** — fat loss, build muscle, or stay fit; focus area; and a 2–8 week timeframe
2. **Intensity** — low, moderate, or high
3. **Schedule** — 2–6 days per week and 15, 30, 45, or 60 minute sessions
4. **Equipment and space** — no equipment, mat, bands, or dumbbells; dorm, small room, or larger space

A local rule-based recommendation engine scores five preset plans, produces a match percentage and reasons, and creates a seven-day schedule. The selected recommendation is saved with `SharedPreferences`.

### Featured trainer plans

Three sample trainer plans are available. Each card previews the full weekly schedule before the user follows it, then saves its trainer, schedule, intensity, equipment, and space across the app.

### Workout music

The MVP includes three local demo tracks:

- **Focus** — 90 BPM / low intensity
- **Energy** — 125 BPM / moderate intensity
- **Power** — 145 BPM / high intensity

`MediaPlayer` provides play, pause, looping, and stop controls. The selected track is restored when the screen reopens, and the track matching the active plan intensity is labeled as recommended.

### Rewards

- Tracks completed workouts, points, and consecutive-day streaks
- Shows milestone guidance based on completion count
- Refreshes from the same local state used by Home and My Plan

## Navigation and design

The app uses three top-level destinations:

- **Home**
- **My Plan**
- **Rewards**

The current preview uses **Fresh Motion**: a light warm background, white cards, forest-green primary actions, lime/mint selection states, and restrained peach/yellow accents. This is not recorded as the team's final choice. Fresh Motion and Airy Active remain documented options, and all colors are centralized in Android resources so the team can switch without rewriting screens or features.

See [UI Design System](docs/UI_DESIGN.md) for the full design rules.

## Technical stack

- **Language:** Java 8
- **Layouts:** XML and Material Components
- **Architecture:** one `MainActivity` with Home, My Plan, and Rewards fragments; focused activities for plan building, recommendations, trainers, and music
- **Local state:** `SharedPreferences`
- **Music:** `MediaPlayer` with three WAV files in `res/raw`
- **Build:** Gradle 7.4 and Android Gradle Plugin 7.3.0
- **Android:** minimum SDK 21; compile/target SDK 32
- **Tests:** JUnit 4, AndroidX Test, and Espresso

The MVP intentionally avoids Firebase, accounts, cloud storage, AI services, Spotify integration, and a remote database.

## Project structure

```text
app/src/main/java/edu/kean/fitnessapp/
├── data/          Local repositories and SharedPreferences wrapper
├── logic/         Input validation and recommendation engine
├── model/         Plan, trainer, music, and recommendation models
└── ui/
    ├── home/      Home and workout completion
    ├── plan/      Plan builder, recommendation, trainers, and music
    └── rewards/   Points, streaks, and milestones
```

## Build and test

### Android Studio

1. Clone the repository and switch to the current feature branch:

   ```bash
   git clone https://github.com/xixixixilulululu/fitness-app-3620.git
   cd fitness-app-3620
   git switch feature/plan-tianrui
   ```

2. Open the repository root in Android Studio.
3. Allow Gradle to sync and install Android SDK 32 if Android Studio requests it.
4. Select the `app` run configuration and launch an emulator or connected device running Android 5.0 or newer.

### Command line

On macOS or Linux:

```bash
./gradlew testDebugUnitTest assembleDebug assembleDebugAndroidTest lintDebug
```

On Windows:

```powershell
gradlew.bat testDebugUnitTest assembleDebug assembleDebugAndroidTest lintDebug
```

The generated APK is located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### Verified result

The current branch was verified with 14 JVM unit tests:

```text
BUILD SUCCESSFUL
14 tests, 0 failures, 0 errors, 0 skipped
```

Those tests cover required plan input, invalid/contradictory equipment, representative recommendations, honest tradeoff explanations, match-score bounds, and seven-day schedule generation.

Five Android instrumentation tests also pass on an API 36 emulator. They cover preference persistence and rollover behavior plus two complete UI journeys: saving and completing a personal plan, and following a trainer plan with music playback controls. The verified direct runner result is:

```text
OK (5 tests)
```

The API 36 verification used the test APKs and `AndroidJUnitRunner` directly:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb shell am instrument -w -r \
  edu.kean.fitnessapp.test/androidx.test.runner.AndroidJUnitRunner
```

With this older Android Gradle Plugin 7.3 setup, `connectedDebugAndroidTest` may report an aborted zero-test run on an API 36 image because its bundled device tooling predates that image. That orchestration issue is separate from the test APK: the same five tests pass through the direct runner above. A physical team device should still be checked before the final demo.

## Team responsibilities

| Area | Owner | Scope |
|---|---|---|
| Home Screen | Nikko | Active plan, today's workout, and progress summary |
| Fitness Plan Selection | Tianrui Liu | Plan builder, recommendations, trainer plans, and workout music |
| Motivation and Rewards | Jimena | Points, streaks, badges, and milestones |
| Additional role / integration | Ryan | To be confirmed with the team |

The current branch supplies an integrated MVP for demonstration and team review. It remains separate from `main` until the team reviews and approves a merge. Feature ownership still applies when the team divides or revises the final implementation.

## Roadmap

- [ ] Confirm Fresh Motion or Airy Active with the full team
- [x] Create the Android Studio project and shared resources
- [x] Add Home, My Plan, and Rewards navigation
- [x] Implement Home and daily workout completion
- [x] Implement the four-step personalized plan builder
- [x] Implement validation and rule-based recommendations
- [x] Implement featured trainer plans
- [x] Implement local workout music
- [x] Persist plans, music, progress, points, and streaks
- [x] Connect the complete MVP user journey
- [x] Add 14 JVM unit tests and verify a Debug APK build
- [x] Pass 5 instrumentation tests on an emulator
- [ ] Confirm Ryan's responsibility
- [ ] Review feature ownership and shared files with the full team
- [ ] Complete physical-device and accessibility QA
- [ ] Replace or approve final demo content and audio assets
- [ ] Merge approved work into `main`
- [ ] Prepare the final demonstration and presentation

## Project documents

- [Full feature specification](docs/FEATURES.md)
- [UI design system](docs/UI_DESIGN.md)
- [Integration guide](docs/INTEGRATION.md)
- [Team contribution guide](CONTRIBUTING.md)

## Collaboration

Work on focused feature branches, make small commits, and open pull requests into `main`. Coordinate before changing navigation, shared resources, Gradle files, the manifest, or `SharedPreferences` keys. See [CONTRIBUTING.md](CONTRIBUTING.md) for the team workflow.
