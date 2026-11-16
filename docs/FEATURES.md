# Fitness App Feature Specification

## Product goal

The TECH 3620 Fitness App helps users choose a realistic workout plan, follow it from Home, stay motivated through rewards, and use trainer-designed plans and workout music.

The app should feel complete while remaining achievable with Java, XML, Material Components, local resources, and SharedPreferences.

## Main navigation

The app has three top-level destinations:

1. **Home**
2. **My Plan**
3. **Rewards**

**Core loop:** Build a plan → Save the recommendation → Follow workouts from Home → Complete workouts → Earn progress and rewards

## 1. Fitness Plan Builder

The Plan Builder uses four focused steps rather than one crowded form.

### Step 1: Goal

Choose one primary goal:

- Fat loss
- Build muscle
- Stay fit

Also choose a focus area—full body, upper body, lower body, core, or cardio—and a plan timeframe such as 2, 4, 6, or 8 weeks.

### Step 2: Intensity

Choose low, moderate, or high intensity. Each option includes a short explanation.

### Step 3: Schedule

Choose workout days per week and a session duration such as 15, 30, 45, or 60 minutes.

### Step 4: Equipment and space

Choose available equipment:

- No equipment
- Yoga mat
- Resistance bands
- Dumbbells

Then choose a location:

- Dorm or very small space
- Small room
- Large indoor or outdoor space

### Required behavior

- Display the current step and overall progress
- Preserve previous choices when moving backward
- Require necessary selections before continuing
- Show a clear selected state
- Match the answers to a preset recommendation
- Save the final choice locally after confirmation

## 2. Preset plan recommendations

Java conditions can match the user's answers to several prepared plans.

Examples:

- **Quick Start Cardio** — fat loss, little equipment, 15–30 minute sessions
- **Dorm Strength** — muscle building, dumbbells or bands, limited space
- **Full-Body Balance** — general fitness, moderate intensity
- **Power Builder** — high intensity, longer sessions, more equipment

Each result shows:

- Plan name and duration
- Match label or percentage
- Short recommendation reason
- Weekly schedule and session duration
- Intensity
- Required equipment and space
- Save this plan button

A match percentage is a presentation device based on preset rules; it is not AI-generated.

## 3. Featured Trainer Plans

Featured Trainer Plans are a required feature.

Each card shows:

- Sample trainer name and image or avatar
- Training goal and intensity
- Days per week and session duration
- Required equipment
- Short description
- View plan action

The detail screen lets the user review the schedule, see who the plan is for, and follow or save it. Local sample data is enough; accounts, social features, and online trainer data are not required.

## 4. Workout Music

Workout Music is a required feature.

Suggested local playlists:

- **Focus** — lower BPM for stretching or light workouts
- **Energy** — medium BPM for regular cardio and strength
- **Power** — higher BPM for intense workouts

Each option shows the name, BPM or energy level, recommended intensity, play/pause controls, and current playing state.

Store permitted audio files in res/raw and use Android MediaPlayer. Release playback appropriately when leaving the screen. The user may choose a different playlist from the recommendation.

The MVP does not require Spotify, YouTube, streaming, authorization, automatic beat detection, or online search.

## 5. Home integration

After a user saves a personal or trainer plan, Home shows:

- Current plan
- Current week or day
- Today's workout and duration
- Weekly progress
- Begin or complete workout action
- Shortcut back to My Plan

Completing a workout updates locally stored progress and reward information.

## 6. Rewards and progress

Rewards can show:

- Points earned
- Completed workouts
- Current streak
- Weekly completion
- Simple badges or milestones
- Encouraging message

Example milestones include first workout, three workouts, one full week, a seven-day streak, and reaching the plan halfway point.

The MVP does not need a leaderboard, social feed, cloud synchronization, or real-world prizes.

## Shared data

The group should agree on these values before connecting screens:

- Selected plan name
- Plan source: personal or trainer
- Goal and intensity
- Days per week and session duration
- Equipment and space
- Current workout or day
- Completed workout count
- Points and streak

Use one agreed set of SharedPreferences keys so every screen reads and writes the same values.

## MVP completion criteria

The MVP is complete when a user can:

1. Move through every Plan Builder step.
2. Receive a preset recommendation.
3. Save either the recommendation or a trainer plan.
4. See the saved plan on Home.
5. Play and pause at least one local track.
6. Complete a workout.
7. See progress or rewards update.
8. Navigate among Home, My Plan, and Rewards without broken links.

## Out of scope for the MVP

- AI-generated plans
- Firebase or cloud databases
- Login and account management
- Social networking or chat
- Spotify or YouTube integration
- Online video streaming
- Real-time synchronization
- Advanced charts
- Complex animations
- Live trainer profiles
- Automatic BPM analysis

These can be future improvements. The group should finish and integrate the local MVP first.
