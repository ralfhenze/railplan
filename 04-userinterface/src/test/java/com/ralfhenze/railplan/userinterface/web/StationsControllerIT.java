package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.Command;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
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
import static org.mockito.Mockito.doThrow;
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
    public void userCanAccessAFormToAddANewStation() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/stations/new
        final var response = getGetResponse("/drafts/123/stations/new");

        // Then we get a form with input fields for Station name and coordinates
        final var document = Jsoup.parse(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(document.select("input[name='stationName']")).hasSize(1);
        assertThat(document.select("input[name='latitude']")).hasSize(1);
        assertThat(document.select("input[name='longitude']")).hasSize(1);
    }

    @Test
    public void userCanAddANewStation() throws Exception {
        // When we call POST /drafts/123/stations/new with valid Station parameters
        final var response = getPostResponse(
            "/drafts/123/stations/new",
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
    public void userSeesValidationErrorsWhenAddingAnInvalidStation() throws Exception {
        verifyPostRequestWithInvalidStationData("/drafts/123/stations/new", addTrainStationCommand);
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
        final var nameErrors = List.of("name error 1", "name error 2");
        final var latErrors = List.of("lat error 1");
        final var lngErrors = List.of("lng error 1");
        final var validationException = new ValidationException(Map.of(
            "Station name", nameErrors,
            "Latitude", latErrors,
            "Longitude", lngErrors
        ));
        if (command instanceof AddTrainStationCommand) {
            given(addTrainStationCommand.addTrainStation(any(), any(), anyDouble(), anyDouble()))
                .willThrow(validationException);
        } else if (command instanceof UpdateTrainStationCommand) {
            doThrow(validationException)
                .when(updateTrainStationCommand)
                .updateTrainStation(any(), any(), any(), anyDouble(), anyDouble());
        } else {
            throw new Exception("Unsupported command " + command.toString());
        }

        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

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
        assertThat(document.select(".errors.stationName li").eachText()).isEqualTo(nameErrors);
        assertThat(document.select(".errors.latitude li").eachText()).isEqualTo(latErrors);
        assertThat(document.select(".errors.longitude li").eachText()).isEqualTo(lngErrors);
    }
}
