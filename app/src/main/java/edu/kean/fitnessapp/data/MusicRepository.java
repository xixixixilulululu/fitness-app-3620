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
                new MusicTrack("focus", "Deliberate Thought", 69, PlanSelection.INTENSITY_LOW, R.raw.deliberate_thought),
                new MusicTrack("energy", "Brain Dance", 124, PlanSelection.INTENSITY_MODERATE, R.raw.brain_dance),
                new MusicTrack("power", "Cloud Dancer", 214, PlanSelection.INTENSITY_HIGH, R.raw.cloud_dancer)
        );
    }
}
