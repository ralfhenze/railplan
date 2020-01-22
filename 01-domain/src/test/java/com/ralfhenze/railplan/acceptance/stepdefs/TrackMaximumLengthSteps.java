package com.ralfhenze.railplan.acceptance.stepdefs;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class TrackMaximumLengthSteps {

    private RailNetworkDraft draft;
    private PresetStation station1;
    private PresetStation station2;
    private Throwable thrownException;

    @Given("^a Rail Network Draft with two Stations \"(.*)\" and \"(.*)\" \\(distance: (.*)\\)$")
    public void setupTwoStations(String stationName1, String stationName2, String distance) {
        station1 = PresetStation.ofName(stationName1);
        station2 = PresetStation.ofName(stationName2);
        draft = new RailNetworkDraft()
            .addStation(
                station1.getName(),
                station1.getLatitude(),
                station1.getLongitude()
            )
            .addStation(
                station2.getName(),
                station2.getLatitude(),
                station2.getLongitude()
            );
    }

    @When("^a Network Planner tries to connect those two stations with a new Railway Track$")
    public void addRailwayTrack() {
        thrownException = catchThrowable(() ->
            draft = draft.addTrackBetween(station1, station2)
        );
    }

    @Then("^the new Track should be (.*)$")
    public void assertAdded(String addedOrRejected) {
        if ("added".equals(addedOrRejected)) {
            assertThat(thrownException).doesNotThrowAnyException();
            assertThat(draft.getTracks()).hasSize(1);
        } else if ("rejected".equals(addedOrRejected)) {
            assertThat(thrownException).isInstanceOf(ValidationException.class);
        }
    }
}
