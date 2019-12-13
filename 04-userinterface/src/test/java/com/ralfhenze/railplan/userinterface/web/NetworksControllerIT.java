package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import org.eclipse.collections.api.factory.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbf;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.userinterface.web.TestData.defaultPeriod;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbf;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfPos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest
public class NetworksControllerIT {

    private final static int HTTP_OK = 200;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private Queries queries;

    @MockBean
    private ReleasedRailNetworkRepository networkRepository;

    @Test
    public void userCanNavigateToExistingNetworks() throws Exception {
        // Given an existing Network ID "456"
        given(queries.getAllNetworkIds()).willReturn(List.of("456"));

        // When we call GET /networks
        final var response = getGetResponse("/networks");

        // Then the Draft navigation contains one entry pointing to the Draft
        final var document = Jsoup.parse(response.getContentAsString());
        final var networkNavEntries = document.select("#network-navigation li");

        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(networkNavEntries).hasSize(1);
        assertThat(networkNavEntries.first().select("a").attr("href")).isEqualTo("/networks/456");
    }

    @Test
    public void userCanAccessAPageWithAllInfosAboutAReleasedRailNetwork() throws Exception {
        // Given an existing Network
        given(networkRepository.getReleasedRailNetworkOfId(any()))
            .willReturn(getBerlinHamburgNetwork());

        // When we call GET /networks/456
        final var response = getGetResponse("/networks/456");

        // Then we get start- and end-date of the Validity Period
        final var document = Jsoup.parse(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(document.selectFirst(".valid-from").text())
            .isEqualTo(defaultPeriod.getStartDate().toString());
        assertThat(document.selectFirst(".valid-until").text())
            .isEqualTo(defaultPeriod.getEndDate().toString());

        // And we get a table with all Stations
        final var stationRows = document.select("table#stations .station-row");
        assertThat(stationRows).hasSize(2);
        assertThatRowShowsNameAndLocation(stationRows.get(0), berlinHbfName, berlinHbfPos);
        assertThatRowShowsNameAndLocation(stationRows.get(1), hamburgHbfName, hamburgHbfPos);

        // And we get a table with all Tracks
        final var trackRows = document.select("table#tracks .track-row");
        assertThat(trackRows).hasSize(1);
        assertThatRowShowsStationNames(trackRows.get(0), berlinHbfName, hamburgHbfName);
    }

    private void assertThatRowShowsNameAndLocation(
        final Element row,
        final TrainStationName stationName,
        final GeoLocationInGermany location
    ) {
        assertThat(row.selectFirst(".stationName").text())
            .isEqualTo(stationName.getName());
        assertThat(row.selectFirst(".latitude").text())
            .isEqualTo(location.getLatitudeAsString());
        assertThat(row.selectFirst(".longitude").text())
            .isEqualTo(location.getLongitudeAsString());
    }

    private void assertThatRowShowsStationNames(
        final Element row,
        final TrainStationName stationName1,
        final TrainStationName stationName2
    ) {
        assertThat(row.selectFirst(".station-1").text())
            .isEqualTo(stationName1.getName());
        assertThat(row.selectFirst(".station-2").text())
            .isEqualTo(stationName2.getName());
    }

    private MockHttpServletResponse getGetResponse(final String url) throws Exception {
        return mockMvc.perform(get(url)).andReturn().getResponse();
    }

    private ReleasedRailNetwork getBerlinHamburgNetwork() {
        return new ReleasedRailNetwork(
            defaultPeriod,
            Lists.immutable.of(berlinHbf, hamburgHbf),
            Lists.immutable.of(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
        );
    }
}
