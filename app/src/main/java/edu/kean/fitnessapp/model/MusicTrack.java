package edu.kean.fitnessapp.model;

public final class MusicTrack {
    private final String id;
    private final String title;
    private final int bpm;
    private final String recommendedIntensity;
    private final int rawResourceId;

    public MusicTrack(String id, String title, int bpm, String recommendedIntensity, int rawResourceId) {
        this.id = id;
        this.title = title;
        this.bpm = bpm;
        this.recommendedIntensity = recommendedIntensity;
        this.rawResourceId = rawResourceId;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getBpm() { return bpm; }
    public String getRecommendedIntensity() { return recommendedIntensity; }
    public int getRawResourceId() { return rawResourceId; }
}
