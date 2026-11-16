# UI Design System

## Direction

The app has two viable visual directions:

- **Plan A — Fresh Motion:** organic, energetic, and slightly more athletic.
- **Plan B — Airy Active:** bright, modern, and similar to current mainstream fitness and wellness apps.

**Recommendation: Airy Active.** It matches the preferred light interface, keeps every screen easy to combine, and is straightforward to build with Java, XML, and Material Components.

## Plan B — Airy Active

### Color palette

| Use | Color | Hex |
|---|---|---|
| App background | Soft cool white | #FBFCFE |
| Cards and surfaces | White | #FFFFFF |
| Primary actions | Active blue | #377CF6 |
| Selected backgrounds | Pale blue | #EAF2FF |
| Success and music accent | Soft mint | #DDF8EC |
| High-energy accent | Coral | #FF7358 |
| Rewards and streaks | Warm yellow | #FFF0B8 |
| Primary text | Charcoal navy | #18212C |
| Secondary text | Cool gray | #727C89 |

Use blue for buttons, active navigation, progress, links, and selected borders. Mint, coral, and yellow are supporting colors only.

### Visual character

- Large areas of light background and white cards
- Clear blue calls to action
- Rounded cards with subtle borders rather than heavy shadows
- Small color accents for music, rewards, plan categories, and progress
- Simple line icons from Android Studio Vector Assets
- No dark fitness theme, glass effects, neon colors, or large gradients

## Plan A — Fresh Motion

### Color palette

| Use | Color | Hex |
|---|---|---|
| App background | Warm off-white | #F4F7F1 |
| Cards and surfaces | White | #FFFFFF |
| Primary actions | Forest green | #193E2B |
| Activity accent | Lime | #B8E56B |
| Warm accent | Peach | #FFB08B |
| Primary text | Deep charcoal green | #172019 |
| Secondary text | Muted green-gray | #68746B |

Fresh Motion feels more athletic and distinctive. Keep the background light and use forest green for navigation, headings, and primary actions—not as a full-page background.

## Typography

Use Android's default Roboto family.

| Element | Size | Weight |
|---|---:|---|
| Main page title | 26sp | Bold |
| Section heading | 20sp | Bold |
| Card title | 17sp | Bold |
| Body text | 15–16sp | Regular |
| Button label | 15sp | Medium |
| Chip and metadata | 13–14sp | Medium |
| Navigation label | 12sp | Medium |

Use sentence case, one main title per screen, and no more than three text sizes on one card.

## Layout and spacing

Use a shared spacing scale: 4dp, 8dp, 12dp, 16dp, 20dp, 24dp, and 32dp.

- Page horizontal padding: 20dp
- Card corner radius: 20dp
- Button corner radius: 14dp
- Primary button height: at least 52dp
- Icon size: 24dp
- Minimum touch target: 48dp × 48dp

Cards should use a thin neutral border or very subtle elevation, not both strongly.

## Navigation

The bottom navigation has three top-level destinations:

1. **Home**
2. **My Plan**
3. **Rewards**

Profile or settings can remain in the upper-right. Secondary screens use a top-left back arrow.

**Recommended flow:** Home → My Plan → Plan Builder → Recommended Plan → Trainer Plan / Music → Save Plan → Home → Complete Workout → Rewards

## Core components

### Primary button

Use one full-width primary action on setup and confirmation screens. Airy Active uses blue; Fresh Motion uses forest green.

### Secondary button

Use an outline or text-only treatment for Back, Skip, or Change selections.

### Selection card

Selected state must include:

- Tinted background
- Colored border
- Check icon

Do not communicate selection with color alone.

### Chips

Use short chips for goals, days per week, duration, equipment, and space. Keep single-select and multi-select behavior consistent within each group.

### Progress indicator

Every Plan Builder step shows:

- Step number, such as Step 1 of 4
- A thin horizontal progress bar
- One clear question
- Back and Continue in the same location

### Workout card

Show only the information needed to choose: plan name, duration, intensity, trainer or category, and equipment when relevant.

## Screen guidance

### Home

Home should immediately answer:

- What is today's workout?
- How far along am I this week?
- What should I do next?

Suggested order: greeting, current-plan hero card, today's workout, weekly progress, reward/streak summary.

### Plan Builder

1. **Goal:** fat loss, build muscle, or stay fit; focus area and timeframe
2. **Intensity:** low, moderate, or high
3. **Schedule:** weekly days and session duration
4. **Equipment and space:** available equipment and workout location

### Recommended Plan

Show the plan name, match label, duration, weekly frequency, equipment, recommendation reason, schedule preview, and Save this plan button.

### Featured Trainer Plans

Trainer plans are a definite feature. Each card includes a sample trainer, goal, intensity, weekly frequency, equipment, and View plan action. Label fictional profiles as samples.

### Workout Music

Use three to five local tracks or playlists with name, BPM, recommended intensity, play/pause control, and a clear playing state. Airy Active can use mint music cards; do not switch to an unrelated dark player.

### Rewards

Use warm yellow for streaks, badges, points, and milestones while retaining the shared cards, typography, and navigation.

## Accessibility

- Keep strong text contrast and body text at least 15sp
- Use 48dp minimum interactive areas
- Add content descriptions to meaningful icons and images
- Pair color changes with borders, labels, or icons
- Use a logical top-to-bottom focus order
- Avoid fast or continuous animation
- Keep transitions around 150–200ms
- Explain errors in text

## Java and XML implementation

Use Java, XML, Material Components Views, ConstraintLayout or LinearLayout with ScrollView, MaterialCardView, ChipGroup, ProgressBar, SharedPreferences, MediaPlayer with local files in res/raw, and Android Studio Vector Assets.

Centralize reusable values:

- colors.xml — palette
- dimens.xml — spacing, radii, heights
- styles.xml — buttons, cards, chips, and text
- themes.xml — global Material theme
- drawable — selected borders and rounded backgrounds

Preset Java conditions can recommend a plan. The feature does not require AI, Firebase, a server, or a database.

## Team consistency rules

1. Use only the chosen palette.
2. Share one primary-button style.
3. Share one card radius and page margin.
4. Keep bottom navigation identical.
5. Reuse resource values instead of hard-coding them.
6. Use the same selected state everywhere.
7. Use one Vector Asset icon style.
8. Review screens together before merging.
9. Prefer reliable interactions over complex effects.
10. Treat trainer plans and music as core features.
