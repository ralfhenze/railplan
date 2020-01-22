package com.ralfhenze.railplan.acceptance.stepdefs;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.ralfhenze.railplan.domain.TestData.BERLIN_OST_LAT;
import static com.ralfhenze.railplan.domain.TestData.BERLIN_OST_LNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class StationInvariantsSteps {

    private RailNetworkDraft draft;
    private Throwable thrownException;

    @Given("^a Rail Network Draft with a Station \"(.*)\"$")
    public void setupRailNetworkDraft(final String stationName) {
        final var station = PresetStation.ofName(stationName);
        draft = new RailNetworkDraft().addStation(
            station.getName(),
            station.getLatitude(),
            station.getLongitude()
        );
    }

    @When("^a Network Planner tries to add a new Station \"(.*)\"$")
    public void addNewStation(final String stationName) {
        addStation(stationName);
    }

    @When("^a Network Planner tries to add a new Station \"(.*)\", which is (.*) away$")
    public void addNearStation(final String stationName, final String distance) {
        if ("Berlin Ostbahnhof".equals(stationName)) {
            thrownException = catchThrowable(() ->
                draft = draft.addStation(stationName, BERLIN_OST_LAT, BERLIN_OST_LNG)
            );
        } else {
            addStation(stationName);
        }
    }

    private void addStation(final String stationName) {
        final var stationToAdd = PresetStation.ofName(stationName);
        thrownException = catchThrowable(() ->
            draft = draft.addStation(
                stationToAdd.getName(),
                stationToAdd.getLatitude(),
                stationToAdd.getLongitude()
            )
        );
    }

    @Then("^the new Station should be (.*)$")
    public void assertAdded(final String addedOrRejected) {
        if ("added".equals(addedOrRejected)) {
            assertThat(thrownException).doesNotThrowAnyException();
            assertThat(draft.getStations()).hasSize(2);
        } else if ("rejected".equals(addedOrRejected)) {
            assertThat(thrownException).isInstanceOf(ValidationException.class);
        }
    }
}
