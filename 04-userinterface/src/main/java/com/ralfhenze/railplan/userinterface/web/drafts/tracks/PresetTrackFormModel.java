package com.ralfhenze.railplan.userinterface.web.drafts.tracks;

import java.util.List;

public class PresetTrackFormModel {

    private List<Integer> presetTrackIdsToAdd;

    public List<Integer> getPresetTrackIdsToAdd() {
        return presetTrackIdsToAdd;
    }

    public void setPresetTrackIdsToAdd(final List<Integer> presetTrackIdsToAdd) {
        this.presetTrackIdsToAdd = presetTrackIdsToAdd;
    }
}
