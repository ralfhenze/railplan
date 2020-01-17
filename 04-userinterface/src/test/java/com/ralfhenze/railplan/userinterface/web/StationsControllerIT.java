package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.TrainStationService;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.Command;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.userinterface.web.drafts.stations.PresetStation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHamburgDraft;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest
public class StationsControllerIT extends HtmlITBase {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private RailNetworkDraftRepository draftRepository;

    @MockBean
    private TrainStationService trainStationService;

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
        assertThatRowShowsNameAndLocation(stationRows.get(0), berlinHbfName, berlinHbfLat, berlinHbfLng);
        assertThatRowShowsNameAndLocation(stationRows.get(1), hamburgHbfName, hamburgHbfLat, hamburgHbfLng);
    }

    private void assertThatRowShowsNameAndLocation(
        final Element row,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        assertThat(row.selectFirst(".stationName").text()).isEqualTo(stationName);
        assertThat(row.selectFirst(".latitude").text()).isEqualTo(String.valueOf(latitude));
        assertThat(row.selectFirst(".longitude").text()).isEqualTo(String.valueOf(longitude));
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
        final var stations = List.of(PresetStation.FRANKFURT_HBF, PresetStation.STUTTGART_HBF);
        final var commandCaptor = ArgumentCaptor.forClass(AddTrainStationCommand.class);

        verify(trainStationService, times(2)).addStationToDraft(commandCaptor.capture());

        for (final var i : List.of(0, 1)) {
            final var executedCommand = commandCaptor.getAllValues().get(i);
            final var station = stations.get(i);
            assertThat(executedCommand.getDraftId()).isEqualTo("123");
            assertThat(executedCommand.getStationName()).isEqualTo(station.name);
            assertThat(executedCommand.getLatitude()).isEqualTo(station.latitude);
            assertThat(executedCommand.getLongitude()).isEqualTo(station.longitude);
        }

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
        //given(trainStationService.addStationToDraft(any())).willReturn(berlinHamburgDraft);

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
        // When we call POST /drafts/123/stations/new-custom with valid Station parameters
        final var response = getPostResponse(
            "/drafts/123/stations/new-custom",
            Map.of(
                "stationName", potsdamHbfName,
                "latitude", String.valueOf(potsdamHbfLat),
                "longitude", String.valueOf(potsdamHbfLng)
            )
        );

        // Then an AddTrainStationCommand is issued with given Station parameters
        final var commandCaptor = ArgumentCaptor.forClass(AddTrainStationCommand.class);

        verify(trainStationService).addStationToDraft(commandCaptor.capture());

        final var executedCommand = commandCaptor.getValue();
        assertThat(executedCommand.getDraftId()).isEqualTo("123");
        assertThat(executedCommand.getStationName()).isEqualTo(potsdamHbfName);
        assertThat(executedCommand.getLatitude()).isEqualTo(potsdamHbfLat);
        assertThat(executedCommand.getLongitude()).isEqualTo(potsdamHbfLng);

        // And we will be redirected to the Stations page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/stations");
    }

    @Test
    public void userSeesValidationErrorsWhenAddingAnInvalidCustomStation() throws Exception {
        verifyPostRequestWithInvalidStationData(
            "/drafts/123/stations/new-custom",
            new AddTrainStationCommand("", "", 0, 0)
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
            .isEqualTo(berlinHbfName);
        assertThat(document.selectFirst("input[name='latitude']").val())
            .isEqualTo(String.valueOf(berlinHbfLat));
        assertThat(document.selectFirst("input[name='longitude']").val())
            .isEqualTo(String.valueOf(berlinHbfLng));
    }

    @Test
    public void userCanUpdateAnExistingStation() throws Exception {
        // When we call POST /drafts/123/stations/1/edit with valid Station parameters
        final var response = getPostResponse(
            "/drafts/123/stations/1/edit",
            Map.of(
                "stationName", potsdamHbfName,
                "latitude", String.valueOf(potsdamHbfLat),
                "longitude", String.valueOf(potsdamHbfLng)
            )
        );

        // Then an UpdateTrainStationCommand is issued with given Station parameters
        final var commandCaptor = ArgumentCaptor.forClass(UpdateTrainStationCommand.class);
        verify(trainStationService).updateStationOfDraft(commandCaptor.capture());

        final var executedCommand = commandCaptor.getValue();
        assertThat(executedCommand.getDraftId()).isEqualTo("123");
        assertThat(executedCommand.getStationId()).isEqualTo(1);
        assertThat(executedCommand.getStationName()).isEqualTo(potsdamHbfName);
        assertThat(executedCommand.getLatitude()).isEqualTo(potsdamHbfLat);
        assertThat(executedCommand.getLongitude()).isEqualTo(potsdamHbfLng);

        // And we will be redirected to the Stations page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/stations");
    }

    @Test
    public void userSeesValidationErrorsWhenUpdatingAStationWithInvalidData() throws Exception {
        verifyPostRequestWithInvalidStationData(
            "/drafts/123/stations/1/edit",
            new UpdateTrainStationCommand("", 1, "", 0, 0)
        );
    }

    @Test
    public void userCanDeleteAnExistingStation() throws Exception {
        // When we call GET /drafts/123/stations/1/delete
        final var response = getGetResponse("/drafts/123/stations/1/delete");

        // Then a DeleteTrainStationCommand is issued
        final var commandCaptor = ArgumentCaptor.forClass(DeleteTrainStationCommand.class);
        verify(trainStationService).deleteStationFromDraft(commandCaptor.capture());

        final var executedDeleteCommand = commandCaptor.getValue();
        assertThat(executedDeleteCommand.getDraftId()).isEqualTo("123");
        assertThat(executedDeleteCommand.getStationId()).isEqualTo(1);

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

        // And the commands will throw a ValidationException
        final var nameError1 = "name error 1";
        final var nameError2 = "name error 2";
        final var latError = "lat error";
        final var lngError = "lng error";
        final var validationException = new ValidationException(List.of(
            new ValidationError(nameError1, Field.STATION_NAME),
            new ValidationError(nameError2, Field.STATION_NAME),
            new ValidationError(latError, Field.LATITUDE),
            new ValidationError(lngError, Field.LONGITUDE)
        ));
        if (command instanceof AddTrainStationCommand) {
            doThrow(validationException).when(trainStationService).addStationToDraft(any());
        } else if (command instanceof UpdateTrainStationCommand) {
            doThrow(validationException).when(trainStationService).updateStationOfDraft(any());
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
        assertThat(document.select(".errors.stationName li").eachText())
            .isEqualTo(List.of(nameError1, nameError2));
        assertThat(document.select(".errors.latitude li").eachText())
            .isEqualTo(List.of(latError));
        assertThat(document.select(".errors.longitude li").eachText())
            .isEqualTo(List.of(lngError));
    }
}
