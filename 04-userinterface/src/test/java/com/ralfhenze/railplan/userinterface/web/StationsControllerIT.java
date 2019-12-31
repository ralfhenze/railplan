package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.Command;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.userinterface.web.drafts.stations.PresetStation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHamburgDraft;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfPos;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfPos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest
public class StationsControllerIT extends HtmlITBase {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private RailNetworkDraftRepository draftRepository;

    @MockBean
    private AddTrainStationCommand addTrainStationCommand;

    @MockBean
    private UpdateTrainStationCommand updateTrainStationCommand;

    @MockBean
    private DeleteTrainStationCommand deleteTrainStationCommand;

    @Test
    public void userGetsAListOfAllStationsOfADraft() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/stations
        final var response = getGetResponse("/drafts/123/stations");

        // Then we get a table with all Stations
        final var document = Jsoup.parse(response.getContentAsString());
        final var stationRows = document.select("#stations .station-row");
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(stationRows).hasSize(2);
        assertThatRowShowsNameAndLocation(stationRows.get(0), berlinHbfName, berlinHbfPos);
        assertThatRowShowsNameAndLocation(stationRows.get(1), hamburgHbfName, hamburgHbfPos);
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

    @Test
    public void userCanAccessAFormToAddNewStationsFromPresets() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/stations/new-from-preset
        final var response = getGetResponse("/drafts/123/stations/new-from-preset");

        // Then we get a form with a multi-select-box of all preset Stations
        final var document = Jsoup.parse(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(document.select("select[name='presetStationsToAdd'] option"))
            .hasSize(PresetStation.values().length);
    }

    @Test
    public void userCanAddNewStationsFromPresets() throws Exception {
        // Given RailNetworkDraftRepository and AddTrainStationCommand will return a valid Draft
        final var draft = mock(RailNetworkDraft.class);
        given(draft.isValid()).willReturn(true);
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(draft);
        given(addTrainStationCommand.addTrainStation(any(), any(), anyDouble(), anyDouble()))
            .willReturn(draft);

        // When we call POST /drafts/123/stations/new-from-preset with two valid preset Stations
        final var response = getPostResponseWithMultiValueParameters(
            "/drafts/123/stations/new-from-preset",
            Map.of(
                "presetStationsToAdd", List.of(
                    PresetStation.FRANKFURT_HBF.name(),
                    PresetStation.STUTTGART_HBF.name()
                )
            )
        );

        // Then an AddTrainStationCommand is issued for each given preset Station
        List.of(PresetStation.FRANKFURT_HBF, PresetStation.STUTTGART_HBF).forEach(station ->
            verify(addTrainStationCommand)
                .addTrainStation("123", station.name, station.latitude, station.longitude)
        );

        // And we will be redirected to the Stations page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/stations");
    }

    @Test
    public void userSeesValidationErrorsWhenAddingAlreadyExistingPresetStations() throws Exception {
        /* TODO
           Was kann alles schief gehen?

            * Stations-ID ist nicht als Preset Station bekannt (z.B. XYZ)
            * Station bereits in Draft vorhanden
            * gar keine Stations-IDs mitgegeben
            * mitgegebene Stations-IDs enthalten Duplikate
            * keine Verbindung zur Datenbank (Netzwerkfehler)
         */
        final var nameErrors = List.of("name error 1", "name error 2");
        given(addTrainStationCommand.addTrainStation(any(), any(), anyDouble(), anyDouble()))
            .willReturn(berlinHamburgDraft);

        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call POST /drafts/123/stations/new-from-preset with an already existing Station
        final var response = getPostResponseWithMultiValueParameters(
            "/drafts/123/stations/new-from-preset",
            Map.of("presetStationsToAdd", List.of(PresetStation.BERLIN_HBF.name()))
        );

        // Then the preset Station form shows the error messages
        final var presetStationForm = getElement("#preset-station-form", response);
        //assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        //assertThat(presetStationForm.select(".errors li").eachText()).isEqualTo(nameErrors);
    }

    @Test
    public void userCanAccessAFormToAddANewCustomStation() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/stations/new-custom
        final var response = getGetResponse("/drafts/123/stations/new-custom");

        // Then we get a form with input fields for Station name and coordinates
        final var document = Jsoup.parse(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(document.select("input[name='stationName']")).hasSize(1);
        assertThat(document.select("input[name='latitude']")).hasSize(1);
        assertThat(document.select("input[name='longitude']")).hasSize(1);
    }

    @Test
    public void userCanAddANewCustomStation() throws Exception {
        // Given AddTrainStationCommand will return a valid Draft
        final var draft = mock(RailNetworkDraft.class);
        given(draft.isValid()).willReturn(true);
        given(addTrainStationCommand.addTrainStation(any(), any(), anyDouble(), anyDouble()))
            .willReturn(draft);

        // When we call POST /drafts/123/stations/new-custom with valid Station parameters
        final var response = getPostResponse(
            "/drafts/123/stations/new-custom",
            Map.of(
                "stationName", potsdamHbfName.getName(),
                "latitude", potsdamHbfPos.getLatitudeAsString(),
                "longitude", potsdamHbfPos.getLongitudeAsString()
            )
        );

        // Then an AddTrainStationCommand is issued with given Station parameters
        verify(addTrainStationCommand).addTrainStation(
            "123",
            potsdamHbfName.getName(),
            potsdamHbfPos.getLatitude(),
            potsdamHbfPos.getLongitude()
        );

        // And we will be redirected to the Stations page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/stations");
    }

    @Test
    public void userSeesValidationErrorsWhenAddingAnInvalidCustomStation() throws Exception {
        verifyPostRequestWithInvalidStationData(
            "/drafts/123/stations/new-custom",
            addTrainStationCommand
        );
    }

    @Test
    public void userCanAccessAFormToEditAnExistingStation() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/stations/1/edit
        final var response = getGetResponse("/drafts/123/stations/1/edit");

        // Then we get a form with pre-filled input fields for Station name and coordinates
        final var document = Jsoup.parse(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(document.selectFirst("input[name='stationName']").val())
            .isEqualTo(berlinHbfName.getName());
        assertThat(document.selectFirst("input[name='latitude']").val())
            .isEqualTo(berlinHbfPos.getLatitudeAsString());
        assertThat(document.selectFirst("input[name='longitude']").val())
            .isEqualTo(berlinHbfPos.getLongitudeAsString());
    }

    @Test
    public void userCanUpdateAnExistingStation() throws Exception {
        // Given UpdateTrainStationCommand will return a valid Draft
        final var draft = mock(RailNetworkDraft.class);
        given(draft.isValid()).willReturn(true);
        given(updateTrainStationCommand
            .updateTrainStation(any(), any(), any(), anyDouble(), anyDouble()))
            .willReturn(draft);

        // When we call POST /drafts/123/stations/1/edit with valid Station parameters
        final var response = getPostResponse(
            "/drafts/123/stations/1/edit",
            Map.of(
                "stationName", potsdamHbfName.getName(),
                "latitude", potsdamHbfPos.getLatitudeAsString(),
                "longitude", potsdamHbfPos.getLongitudeAsString()
            )
        );

        // Then an UpdateTrainStationCommand is issued with given Station parameters
        verify(updateTrainStationCommand).updateTrainStation(
            "123",
            "1",
            potsdamHbfName.getName(),
            potsdamHbfPos.getLatitude(),
            potsdamHbfPos.getLongitude()
        );

        // And we will be redirected to the Stations page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/stations");
    }

    @Test
    public void userSeesValidationErrorsWhenUpdatingAStationWithInvalidData() throws Exception {
        verifyPostRequestWithInvalidStationData(
            "/drafts/123/stations/1/edit",
            updateTrainStationCommand
        );
    }

    @Test
    public void userCanDeleteAnExistingStation() throws Exception {
        // When we call GET /drafts/123/stations/1/delete
        final var response = getGetResponse("/drafts/123/stations/1/delete");

        // Then a DeleteTrainStationCommand is issued
        verify(deleteTrainStationCommand).deleteTrainStation("123", "1");

        // And we will be redirected to the Stations page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/stations");
    }

    private void verifyPostRequestWithInvalidStationData(
        final String url,
        final Command command
    ) throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // And the commands will return the updated Draft
        if (command instanceof AddTrainStationCommand) {
            given(addTrainStationCommand.addTrainStation(any(), any(), anyDouble(), anyDouble()))
                .willReturn(
                    berlinHamburgDraft.withNewStation(
                        new TrainStationName("ab"),
                        new GeoLocationInGermany(1.0, 1.0)
                    )
                );
        } else if (command instanceof UpdateTrainStationCommand) {
            given(updateTrainStationCommand
                .updateTrainStation(any(), any(), any(), anyDouble(), anyDouble()))
                .willReturn(
                    berlinHamburgDraft.withUpdatedStation(
                        new TrainStationId("1"),
                        new TrainStationName("ab"),
                        new GeoLocationInGermany(1.0, 1.0)
                    )
                );
        } else {
            throw new IllegalArgumentException("Unsupported command " + command.toString());
        }

        // When we call POST to url with invalid Station parameters
        final var response = getPostResponse(
            url, Map.of("stationName", "ab", "latitude", "1.0", "longitude", "1.0")
        );

        // Then each input field has the invalid value
        final var document = Jsoup.parse(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(document.selectFirst("input[name='stationName']").val()).isEqualTo("ab");
        assertThat(document.selectFirst("input[name='latitude']").val()).isEqualTo("1.0");
        assertThat(document.selectFirst("input[name='longitude']").val()).isEqualTo("1.0");

        // And each input field shows it's error messages
        assertThat(document.select(".errors.stationName li")).hasSize(2);
        assertThat(document.select(".errors.latitude li")).hasSize(1);
        assertThat(document.select(".errors.longitude li")).hasSize(1);
    }
}
