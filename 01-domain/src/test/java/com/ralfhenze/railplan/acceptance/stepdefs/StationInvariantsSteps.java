package com.ralfhenze.railplan.acceptance.stepdefs;

import com.ralfhenze.railplan.domain.TestData;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class StationInvariantsSteps {

    private RailNetworkDraft draft;

    @Given("^a Rail Network Draft with a Station \"(.*)\"$")
    public void setupRailNetworkDraft(final String stationName) {
        final var station = TestData.getStation(stationName);
        draft = new RailNetworkDraft()
            .withNewStation(station.getName(), station.getLocation());
    }

    @When("^a Network Planner tries to add a new Station \"(.*)\"$")
    public void addNewStation(final String stationName) {
        addStation(stationName);
    }

    @When("^a Network Planner tries to add a new Station \"(.*)\", which is (.*) away$")
    public void addNearStation(final String stationName, final String distance) {
        addStation(stationName);
    }

    private void addStation(final String stationName) {
        final var stationToAdd = TestData.getStation(stationName);
        draft = draft.withNewStation(stationToAdd.getName(), stationToAdd.getLocation());
    }

    @Then("^the new Station should be (.*)$")
    public void assertAdded(final String addedOrRejected) {
        if ("added".equals(addedOrRejected)) {
            assertThat(draft.isValid()).isTrue();
            assertThat(draft.getStations()).hasSize(2);
        } else if ("rejected".equals(addedOrRejected)) {
            assertThat(draft.isValid()).isFalse();
        }
    }
}
