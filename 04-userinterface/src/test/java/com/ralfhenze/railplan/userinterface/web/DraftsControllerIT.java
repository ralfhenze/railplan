package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.Command;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
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
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DraftsControllerIT {

    private final static int HTTP_OK = 200;
    private final static int HTTP_MOVED_TEMPORARILY = 302;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private Queries queries;

    @MockBean
    private RailNetworkDraftRepository draftRepository;

    @MockBean
    private AddRailNetworkDraftCommand addRailNetworkDraftCommand;

    @MockBean
    private DeleteRailNetworkDraftCommand deleteRailNetworkDraftCommand;

    @MockBean
    private AddTrainStationCommand addTrainStationCommand;

    @MockBean
    private UpdateTrainStationCommand updateTrainStationCommand;

    @MockBean
    private DeleteTrainStationCommand deleteTrainStationCommand;

    @MockBean
    private AddRailwayTrackCommand addRailwayTrackCommand;

    @MockBean
    private DeleteRailwayTrackCommand deleteRailwayTrackCommand;

    @MockBean
    private ReleaseRailNetworkCommand releaseRailNetworkCommand;

    @Test
    public void userCanNavigateToExistingDrafts() throws Exception {
        // Given an existing Draft ID "123"
        given(queries.getAllDraftIds()).willReturn(List.of("123"));

        // When we call GET /drafts
        final var response = getGetResponse("/drafts");

        // Then the Draft navigation contains one entry pointing to the Draft
        final var document = Jsoup.parse(response.getContentAsString());
        final var draftNavEntries = document.select("#draft-navigation li");

        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(draftNavEntries).hasSize(1);
        assertThat(draftNavEntries.first().select("a").attr("href")).isEqualTo("/drafts/123");
    }

    @Test
    public void userCanAddANewDraft() throws Exception {
        // Given a Draft with ID "123" will be created
        given(addRailNetworkDraftCommand.addRailNetworkDraft())
            .willReturn(Optional.of(new RailNetworkDraft().withId(new RailNetworkDraftId("123"))));

        // When we call GET /drafts/new
        final var response = getGetResponse("/drafts/new");

        // Then an AddRailNetworkDraftCommand is issued
        verify(addRailNetworkDraftCommand).addRailNetworkDraft();

        // And we will be redirected to the new Draft
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123");
    }

    @Test
    public void userGetsAListOfAllStationsAndTracksOfADraft() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(getBerlinHamburgDraft());

        // When we call GET /drafts/123
        final var response = getGetResponse("/drafts/123");

        // Then we get a table with all Stations
        final var document = Jsoup.parse(response.getContentAsString());
        final var stationRows = document.select("table#stations .station-row");
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
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

    @Test
    public void userCanDeleteAnExistingDraft() throws Exception {
        // When we call GET /drafts/123/delete
        final var response = getGetResponse("/drafts/123/delete");

        // Then a DeleteRailNetworkDraftCommand is issued
        verify(deleteRailNetworkDraftCommand).deleteRailNetworkDraft("123");

        // And we will be redirected to the Drafts overview
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts");
    }

    @Test
    public void userCanAccessAFormToAddANewStation() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(getBerlinHamburgDraft());

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

        // And we will be redirected to the Draft page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123");
    }

    @Test
    public void userSeesValidationErrorsWhenAddingAnInvalidStation() throws Exception {
        verifyPostRequestWithInvalidStationData("/drafts/123/stations/new", addTrainStationCommand);
    }

    @Test
    public void userCanAccessAFormToEditAnExistingStation() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(getBerlinHamburgDraft());

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

        // And we will be redirected to the Draft page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123");
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

        // And we will be redirected to the Draft page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123");
    }

    @Test
    public void userCanAccessAFormToAddANewTrack() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(getBerlinHamburgDraft());

        // When we call GET /drafts/123/tracks/new
        final var response = getGetResponse("/drafts/123/tracks/new");

        // Then we get a form with input fields for Station name and coordinates
        final var trackForm = getElement("#track-form", response);
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(trackForm.select("select[name='firstStationId']")).hasSize(1);
        assertThat(trackForm.select("select[name='secondStationId']")).hasSize(1);
        assertThat(trackForm.select("input[type='submit']")).hasSize(1);
    }

    @Test
    public void userCanAddANewTrack() throws Exception {
        // When we call POST /drafts/123/tracks/new with valid Track parameters
        final var response = getPostResponse(
            "/drafts/123/tracks/new",
            Map.of("firstStationId", "1", "secondStationId", "2")
        );

        // Then an AddRailwayTrackCommand is issued with given Track parameters
        verify(addRailwayTrackCommand).addRailwayTrack("123", "1", "2");

        // And we will be redirected to the Draft page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123");
    }

    @Test
    public void userSeesValidationErrorsWhenAddingAnInvalidTrack() throws Exception {
        final var firstStationErrors = List.of("Station 1 error");
        final var secondStationErrors = List.of("Station 2 error");
        final var validationException = new ValidationException(Map.of(
            "First Station ID", firstStationErrors,
            "Second Station ID", secondStationErrors
        ));
        given(addRailwayTrackCommand.addRailwayTrack(any(), any(), any()))
            .willThrow(validationException);

        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(getBerlinHamburgDraft());

        // When we call POST /drafts/123/tracks/new with invalid Track parameters
        final var response = getPostResponse(
            "/drafts/123/tracks/new",
            Map.of("firstStationId", "2", "secondStationId", "2")
        );

        // Then each Station selector has the invalid value
        final var trackForm = getElement("#track-form", response);
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(trackForm.select("select[name='firstStationId']").val()).isEqualTo("2");
        assertThat(trackForm.select("select[name='secondStationId']").val()).isEqualTo("2");

        // And each Station selector shows it's error messages
        assertThat(trackForm.select(".errors.firstStationId li").eachText())
            .isEqualTo(firstStationErrors);
        assertThat(trackForm.select(".errors.secondStationId li").eachText())
            .isEqualTo(secondStationErrors);
    }

    @Test
    public void userCanDeleteAnExistingTrack() throws Exception {
        // When we call GET /drafts/123/tracks/1/2/delete
        final var response = getGetResponse("/drafts/123/tracks/1/2/delete");

        // Then a DeleteRailwayTrackCommand is issued
        verify(deleteRailwayTrackCommand).deleteRailwayTrack("123", "1", "2");

        // And we will be redirected to the Draft page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123");
    }

    @Test
    public void userCanAccessAFormToReleaseAnExistingDraft() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(getBerlinHamburgDraft());

        // When we call GET /drafts/123/release
        final var response = getGetResponse("/drafts/123/release");

        // Then we get a form with input fields for start- and end-date
        final var releaseForm = getElement("#release-form", response);
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(releaseForm.select("input[name='startDate']")).hasSize(1);
        assertThat(releaseForm.select("input[name='endDate']")).hasSize(1);
        assertThat(releaseForm.select("input[type='submit']")).hasSize(1);
    }

    @Test
    public void userCanReleaseAnExistingDraft() throws Exception {
        // Given we will get an ID for our Released Rail Network
        given(releaseRailNetworkCommand.releaseRailNetworkDraft(any(), any(), any()))
            .willReturn(Optional.of(new ReleasedRailNetworkId("1")));

        // When we call POST /drafts/123/release with valid parameters
        final var response = getPostResponse(
            "/drafts/123/release",
            Map.of("startDate", "2020-01-01", "endDate", "2020-01-31")
        );

        // Then a ReleaseRailNetworkCommand is issued with those parameters
        verify(releaseRailNetworkCommand).releaseRailNetworkDraft(
            "123", LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-31")
        );

        // And we will be redirected to the Released Rail Network page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/networks/1");
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
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(getBerlinHamburgDraft());

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

    private RailNetworkDraft getBerlinHamburgDraft() {
        return new RailNetworkDraft()
            .withId(new RailNetworkDraftId("123"))
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewTrack(berlinHbfName, hamburgHbfName);
    }

    private MockHttpServletResponse getGetResponse(final String url) throws Exception {
        return mockMvc.perform(get(url)).andReturn().getResponse();
    }

    private MockHttpServletResponse getPostResponse(
        final String url,
        final Map<String, String> parameters
    ) throws Exception {
        final var multiValueMapParameters = new LinkedMultiValueMap<String, String>();
        parameters.forEach((key, value) -> multiValueMapParameters.put(key, List.of(value)));

        return mockMvc
            .perform(post(url).params(multiValueMapParameters))
            .andReturn()
            .getResponse();
    }

    private Element getElement(
        final String cssSelector,
        final MockHttpServletResponse response
    ) throws Exception {
        return Jsoup.parse(response.getContentAsString()).selectFirst(cssSelector);
    }
}
