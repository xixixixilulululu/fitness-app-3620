package edu.kean.fitnessapp.ui.plan;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import edu.kean.fitnessapp.R;
import edu.kean.fitnessapp.data.AppPreferences;
import edu.kean.fitnessapp.data.MusicRepository;
import edu.kean.fitnessapp.model.MusicTrack;

public class MusicActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private String currentTrackId;
    private MaterialButton currentButton;
    private TextView status;
    private AppPreferences preferences;
    private List<MusicTrack> tracks;
    private MaterialButton[] trackButtons;
    private TextView[] trackBadges;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        preferences = new AppPreferences(this);
        tracks = MusicRepository.getTracks();
        status = findViewById(R.id.music_status);

        MaterialButton focusButton = findViewById(R.id.play_focus_button);
        MaterialButton energyButton = findViewById(R.id.play_energy_button);
        MaterialButton powerButton = findViewById(R.id.play_power_button);
        trackButtons = new MaterialButton[]{focusButton, energyButton, powerButton};
        trackBadges = new TextView[]{
                findViewById(R.id.music_focus_badge),
                findViewById(R.id.music_energy_badge),
                findViewById(R.id.music_power_badge)
        };

        focusButton.setOnClickListener(v -> toggleTrack(tracks.get(0), focusButton));
        energyButton.setOnClickListener(v -> toggleTrack(tracks.get(1), energyButton));
        powerButton.setOnClickListener(v -> toggleTrack(tracks.get(2), powerButton));
        findViewById(R.id.stop_music_button).setOnClickListener(v -> stopPlayback());
        findViewById(R.id.music_back_button).setOnClickListener(v -> finish());
        refreshSelectionState(false);
    }

    private void toggleTrack(MusicTrack track, MaterialButton button) {
        if (track.getId().equals(currentTrackId) && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                button.setText(R.string.play);
            } else {
                mediaPlayer.start();
                button.setText(R.string.pause);
            }
            return;
        }

        releasePlayer();
        resetCurrentButton();
        mediaPlayer = MediaPlayer.create(this, track.getRawResourceId());
        if (mediaPlayer == null) {
            Toast.makeText(this, R.string.music_unavailable, Toast.LENGTH_SHORT).show();
            return;
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        currentTrackId = track.getId();
        currentButton = button;
        currentButton.setText(R.string.pause);
        status.setText(getString(R.string.now_playing_format, track.getTitle()));
        preferences.saveSelectedMusic(track.getId(), track.getTitle());
        refreshBadges();
    }

    private void stopPlayback() {
        releasePlayer();
        resetCurrentButton();
        currentTrackId = null;
        refreshSelectionState(true);
    }

    private void resetCurrentButton() {
        if (currentButton != null) {
            currentButton.setText(R.string.play);
            currentButton = null;
        }
    }

    private void refreshSelectionState(boolean stopped) {
        refreshBadges();
        String title = preferences.getSelectedMusicTitle();
        if (title.isEmpty()) {
            status.setText(R.string.music_stopped);
        } else {
            status.setText(getString(
                    stopped ? R.string.music_selected_stopped_status : R.string.music_selected_status,
                    title
            ));
        }
    }

    private void refreshBadges() {
        String selectedId = preferences.getSelectedMusicId();
        String activeIntensity = preferences.hasActivePlan() ? preferences.getIntensity() : "";
        for (int i = 0; i < tracks.size(); i++) {
            MusicTrack track = tracks.get(i);
            boolean selected = track.getId().equals(selectedId);
            boolean recommended = track.getRecommendedIntensity().equals(activeIntensity);
            TextView badge = trackBadges[i];
            if (selected && recommended) {
                badge.setText(R.string.music_selected_recommended_badge);
                badge.setVisibility(View.VISIBLE);
            } else if (selected) {
                badge.setText(R.string.music_selected_badge);
                badge.setVisibility(View.VISIBLE);
            } else if (recommended) {
                badge.setText(R.string.music_recommended_badge);
                badge.setVisibility(View.VISIBLE);
            } else {
                badge.setVisibility(View.GONE);
            }
            if (trackButtons[i] != currentButton) {
                trackButtons[i].setText(R.string.play);
            }
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
        resetCurrentButton();
        currentTrackId = null;
        if (status != null) {
            refreshSelectionState(true);
        }
    }
}
