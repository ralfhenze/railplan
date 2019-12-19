package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import java.util.List;

public class Stations {

    private List<String> stations;

    public Stations() {}

    public Stations(List<String> stations) {
        this.stations = stations;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}
