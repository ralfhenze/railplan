package com.ralfhenze.rms.railnetworkplanning.acceptance.stepdefs;

import com.ralfhenze.rms.railnetworkplanning.domain.TestData;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class TrackMaximumLengthSteps {

    private RailNetworkDraft draft;
    private TrainStation station1;
    private TrainStation station2;
    private boolean exceptionWasThrown;

    @Given("^a Rail Network Draft with two Stations \"(.*)\" and \"(.*)\" \\(distance: (.*)\\)$")
    public void setupTwoStations(String stationName1, String stationName2, String distance) {
        station1 = TestData.getStation(stationName1);
        station2 = TestData.getStation(stationName2);
        draft = new RailNetworkDraft()
            .withNewStation(station1.getName(), station1.getLocation())
            .withNewStation(station2.getName(), station2.getLocation());
    }

    @When("^a Network Planner tries to connect those two stations with a new Railway Track$")
    public void addRailwayTrack() {
        exceptionWasThrown = false;
        try {
            draft = draft.withNewTrack(station1.getName(), station2.getName());
        } catch (IllegalArgumentException e) {
            exceptionWasThrown = true;
        }
    }

    @Then("^the new Track should be (.*)$")
    public void assertAdded(String addedOrRejected) {
        if (addedOrRejected.equals("added")) {
            assertFalse(exceptionWasThrown);
            assertEquals(1, draft.getTracks().size());
        } else if (addedOrRejected.equals("rejected")) {
            assertTrue(exceptionWasThrown);
        }
    }
}
