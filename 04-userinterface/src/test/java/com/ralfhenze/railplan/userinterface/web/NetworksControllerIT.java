package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
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

import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.BERLIN_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.HAMBURG_HBF;
import static com.ralfhenze.railplan.userinterface.web.TestData.BERLIN_HBF_STATION;
import static com.ralfhenze.railplan.userinterface.web.TestData.DEFAULT_PERIOD;
import static com.ralfhenze.railplan.userinterface.web.TestData.HAMBURG_HBF_STATION;
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
            .isEqualTo(DEFAULT_PERIOD.getStartDate().toString());
        assertThat(document.selectFirst(".valid-until").text())
            .isEqualTo(DEFAULT_PERIOD.getEndDate().toString());

        // And we get a table with all Stations
        final var stationRows = document.select("table#stations .station-row");
        assertThat(stationRows).hasSize(2);
        assertThatRowShowsNameAndLocation(stationRows.get(0), BERLIN_HBF);
        assertThatRowShowsNameAndLocation(stationRows.get(1), HAMBURG_HBF);

        // And we get a table with all Tracks
        final var trackRows = document.select("table#tracks .track-row");
        assertThat(trackRows).hasSize(1);
        assertThatRowShowsStationNames(trackRows.get(0), BERLIN_HBF.getName(), HAMBURG_HBF.getName());
    }

    private void assertThatRowShowsNameAndLocation(
        final Element row,
        final com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation station
    ) {
        assertThat(row.selectFirst(".stationName").text())
            .isEqualTo(station.getName());
        assertThat(row.selectFirst(".latitude").text())
            .isEqualTo(String.valueOf(station.getLatitude()));
        assertThat(row.selectFirst(".longitude").text())
            .isEqualTo(String.valueOf(station.getLongitude()));
    }

    private void assertThatRowShowsStationNames(
        final Element row,
        final String stationName1,
        final String stationName2
    ) {
        assertThat(row.selectFirst(".station-1").text()).isEqualTo(stationName1);
        assertThat(row.selectFirst(".station-2").text()).isEqualTo(stationName2);
    }

    private MockHttpServletResponse getGetResponse(final String url) throws Exception {
        return mockMvc.perform(get(url)).andReturn().getResponse();
    }

    private ReleasedRailNetwork getBerlinHamburgNetwork() {
        return new ReleasedRailNetwork(
            DEFAULT_PERIOD,
            Lists.immutable.of(BERLIN_HBF_STATION, HAMBURG_HBF_STATION),
            Lists.immutable.of(new RailwayTrack(BERLIN_HBF_STATION.getId(), HAMBURG_HBF_STATION.getId()))
        );
    }
}
