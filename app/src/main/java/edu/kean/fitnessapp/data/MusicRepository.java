package edu.kean.fitnessapp.data;

import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.model.MusicTrack;
import edu.kean.fitnessapp.model.PlanSelection;

import java.util.Arrays;
import java.util.List;

public final class MusicRepository {
    private MusicRepository() { }

    public static List<MusicTrack> getTracks() {
        return Arrays.asList(
                new MusicTrack("focus", "Focus", 90, PlanSelection.INTENSITY_LOW, R.raw.focus_demo),
                new MusicTrack("energy", "Energy", 125, PlanSelection.INTENSITY_MODERATE, R.raw.energy_demo),
                new MusicTrack("power", "Power", 145, PlanSelection.INTENSITY_HIGH, R.raw.power_demo)
        );
    }
}
