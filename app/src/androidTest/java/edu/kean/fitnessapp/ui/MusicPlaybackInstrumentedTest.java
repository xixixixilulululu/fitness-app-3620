package edu.kean.fitnessapp.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.kean.fitnessapp.data.MusicRepository;
import edu.kean.fitnessapp.model.MusicTrack;

@RunWith(AndroidJUnit4.class)
public class MusicPlaybackInstrumentedTest {

    @Test
    public void everyBundledTrackIsLongFormAndPlayable() {
        Context context = ApplicationProvider.getApplicationContext();

        for (MusicTrack track : MusicRepository.getTracks()) {
            MediaPlayer player = MediaPlayer.create(context, track.getRawResourceId());
            assertNotNull("Could not load " + track.getTitle(), player);
            try {
                assertTrue(track.getTitle() + " is still a short placeholder", player.getDuration() > 60_000);
                player.start();
                assertTrue(track.getTitle() + " did not start playback", player.isPlaying());
                player.pause();
            } finally {
                player.release();
            }
        }
    }
}
