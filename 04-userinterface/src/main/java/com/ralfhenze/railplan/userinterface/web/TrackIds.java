package com.ralfhenze.railplan.userinterface.web;

import java.util.List;

public class TrackIds {

    private List<Integer> trackIds;

    public TrackIds() {}

    public TrackIds(List<Integer> trackIds) {
        this.trackIds = trackIds;
    }

    public List<Integer> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<Integer> trackIds) {
        this.trackIds = trackIds;
    }
}
