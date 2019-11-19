package com.ralfhenze.rms.railnetworkplanning.acceptance.stepdefs;

import com.ralfhenze.rms.railnetworkplanning.domain.TestData;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class UniqueStationNamesSteps {

    private RailNetworkDraft draft;
    private boolean exceptionWasThrown;

    @Given("^a Rail Network Draft with a Station \"(.*)\"$")
    public void setupTwoStations(final String stationName) {
        final TrainStation station = TestData.getStation(stationName);
        draft = new RailNetworkDraft()
            .withNewStation(station.getName(), station.getLocation());
    }

    @When("^a Network Planner tries to add a new Station \"(.*)\"$")
    public void addRailwayTrack(final String stationName) {
        final TrainStation stationToAdd = TestData.getStation(stationName);
        exceptionWasThrown = false;
        try {
            draft = draft.withNewStation(stationToAdd.getName(), stationToAdd.getLocation());
        } catch (IllegalArgumentException e) {
            exceptionWasThrown = true;
        }
    }

    @Then("^the new Station should be (.*)$")
    public void assertAdded(final String addedOrRejected) {
        if (addedOrRejected.equals("added")) {
            assertFalse(exceptionWasThrown);
            assertEquals(2, draft.getStations().size());
        } else if (addedOrRejected.equals("rejected")) {
            assertTrue(exceptionWasThrown);
        }
    }
}
