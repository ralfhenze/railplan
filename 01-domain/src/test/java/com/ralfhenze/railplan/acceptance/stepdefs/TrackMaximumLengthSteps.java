package com.ralfhenze.railplan.acceptance.stepdefs;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class TrackMaximumLengthSteps {

    private RailNetworkDraft draft;
    private PresetStation station1;
    private PresetStation station2;
    private boolean exceptionWasThrown;

    @Given("^a Rail Network Draft with two Stations \"(.*)\" and \"(.*)\" \\(distance: (.*)\\)$")
    public void setupTwoStations(String stationName1, String stationName2, String distance) {
        station1 = PresetStation.ofName(stationName1);
        station2 = PresetStation.ofName(stationName2);
        draft = new RailNetworkDraft()
            .withNewStation(
                station1.getName(),
                station1.getLatitude(),
                station1.getLongitude()
            )
            .withNewStation(
                station2.getName(),
                station2.getLatitude(),
                station2.getLongitude()
            );
    }

    @When("^a Network Planner tries to connect those two stations with a new Railway Track$")
    public void addRailwayTrack() {
        exceptionWasThrown = false;
        try {
            draft = draft.withNewTrack(
                station1.getName(),
                station2.getName()
            );
        } catch (IllegalArgumentException | ValidationException e) {
            exceptionWasThrown = true;
        }
    }

    @Then("^the new Track should be (.*)$")
    public void assertAdded(String addedOrRejected) {
        if ("added".equals(addedOrRejected)) {
            assertThat(exceptionWasThrown).isFalse();
            assertThat(draft.getTracks()).hasSize(1);
        } else if ("rejected".equals(addedOrRejected)) {
            assertThat(exceptionWasThrown).isTrue();
        }
    }
}
