package com.ralfhenze.railplan.userinterface.web.networks.stations;

import java.util.List;

public class PresetStationFormModel {

    private List<String> presetStationsToAdd = List.of();

    public List<String> getPresetStationsToAdd() {
        return presetStationsToAdd;
    }

    public void setPresetStationsToAdd(final List<String> presetStationsToAdd) {
        this.presetStationsToAdd = presetStationsToAdd;
    }
}
