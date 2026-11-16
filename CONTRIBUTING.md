# Contributing to Fitness App 3620

## Team responsibilities

| Area | Owner |
|---|---|
| Home Screen | Nikko |
| Fitness Plan Selection, trainer plans, and music | Lu |
| Motivation and Rewards | Jimena |
| Remaining role or integration support | Ryan — confirm with the team |

Ownership identifies the main developer for an area, but every feature must follow the shared navigation, colors, spacing, and data agreements.

## Branch names

Do not build features directly on main.

Recommended branches:

- Nikko: feature/home-nikko
- Lu: feature/plan-lu
- Jimena: feature/rewards-jimena
- Ryan: feature/confirmed-area-ryan
- If Ryan handles integration: feature/integration-ryan

For other work, use names such as fix/plan-back-navigation or docs/update-feature-spec. Keep one main purpose per branch.

## Starting work

1. Confirm the task belongs to the correct feature area.
2. Discuss shared screen, navigation, or data changes with the group.
3. Update the local main branch.
4. Create or switch to the correct feature branch.
5. Avoid editing another teammate's screen unless the team agrees.

## Commits

Make small commits that describe one meaningful change.

Format: **type: short description**

Useful types:

- feat: new feature
- fix: bug fix
- style: visual or XML styling
- refactor: code restructuring
- docs: documentation
- test: test or verification
- chore: project configuration

Examples:

- feat: add goal selection step
- feat: display saved plan on home
- fix: preserve schedule when returning to previous step
- style: apply shared card radius
- docs: confirm rewards data keys

Avoid vague messages such as update, changes, or final.

## Pull-request workflow

1. Finish and test the feature on its branch.
2. Push the branch to GitHub.
3. Open a pull request into main.
4. Use a clear title.
5. Explain what changed, which screens are affected, how to test it, and any shared keys or navigation changes.
6. Add screenshots for visible UI changes.
7. Ask at least one teammate to review.
8. Resolve conflicts on the feature branch.
9. Merge only after the feature opens successfully and existing navigation still works.

Do not combine unrelated features in one pull request.

## Shared integration rules

### Navigation

The agreed top-level destinations are Home, My Plan, and Rewards. Discuss changes to navigation names, destinations, or IDs before making them.

### Shared resources

Reuse common resources:

- colors.xml
- dimens.xml
- styles.xml
- themes.xml
- Shared icons and drawables
- strings.xml

Do not hard-code repeated colors, text sizes, spacing, or user-facing text when a shared resource is appropriate.

### Shared data

Agree on SharedPreferences keys before implementation. Put shared key names in one constants class rather than typing the same strings independently.

If a pull request changes a shared key, it must update every screen that uses it.

### Avoiding merge conflicts

- Work mainly in the files for your own feature.
- Tell the group before editing MainActivity, navigation files, Gradle files, the manifest, themes, or shared resources.
- Do not reformat an entire file for a small change.
- Pull the latest main before integration work.
- Resolve shared-file changes together when two branches edit the same area.
- Never overwrite a teammate's code just to remove a conflict.

## Before requesting review

Verify that:

- The project builds.
- The new screen opens without crashing.
- Back and bottom navigation work.
- Buttons and selections have visible feedback.
- User choices remain available when expected.
- Shared data uses agreed keys.
- Text is readable and not cut off.
- The layout works on the chosen emulator size.
- No credentials, large unnecessary files, or generated build folders are committed.

## Definition of done

A feature is ready to merge when:

- Its required interaction works.
- It follows the shared design system.
- It integrates with the agreed navigation and data flow.
- The feature owner has tested it.
- Another teammate can follow the pull-request instructions and verify it.
- It does not break already merged features.
