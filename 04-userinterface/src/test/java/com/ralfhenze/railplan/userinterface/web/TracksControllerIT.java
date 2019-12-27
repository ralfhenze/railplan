package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.userinterface.web.drafts.tracks.PresetTracks;
import org.jsoup.Jsoup;
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
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest
public class TracksControllerIT extends HtmlITBase {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private RailNetworkDraftRepository draftRepository;

    @MockBean
    private AddRailwayTrackCommand addRailwayTrackCommand;

    @MockBean
    private DeleteRailwayTrackCommand deleteRailwayTrackCommand;

    @Test
    public void userGetsAListOfAllTracksOfADraft() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/tracks
        final var response = getGetResponse("/drafts/123/tracks");

        // Then we get a table with all Tracks
        final var document = Jsoup.parse(response.getContentAsString());
        final var trackRows = document.select("#tracks .track-row");
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(trackRows).hasSize(1);
        assertThat(trackRows.get(0).selectFirst(".station-1").text())
            .isEqualTo(berlinHbfName.getName());
        assertThat(trackRows.get(0).selectFirst(".station-2").text())
            .isEqualTo(hamburgHbfName.getName());
    }

    @Test
    public void userCanAccessAFormToAddNewTracksFromPresets() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/tracks/new-from-preset
        final var response = getGetResponse("/drafts/123/tracks/new-from-preset");

        // Then we get a form with a multi-select-box of all preset Tracks
        final var presetTrackForm = getElement("#preset-track-form", response);
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(presetTrackForm.select("select[name='presetTrackIdsToAdd'] option"))
            .hasSize(new PresetTracks().getAllPresetTracks().size());
        assertThat(presetTrackForm.select("input[type='submit']")).hasSize(1);
    }

    @Test
    public void userCanAddNewTracksFromPresets() throws Exception {
        // When we call POST /drafts/123/tracks/new-from-preset with two valid preset Track IDs
        final var response = getPostResponseWithMultiValueParameters(
            "/drafts/123/tracks/new-from-preset",
            Map.of("presetTrackIdsToAdd", List.of("1", "2"))
        );

        // Then an AddRailwayTrackCommand is issued for each given preset Track
        final var presetTracks = new PresetTracks().getAllPresetTracks();
        List.of(presetTracks.get(1), presetTracks.get(2)).forEach(track ->
            verify(addRailwayTrackCommand)
                .addRailwayTrackByStationName("123", track.station1.name, track.station2.name)
        );

        // And we will be redirected to the Tracks page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/tracks");
    }

    @Test
    public void userCanAccessAFormToAddANewCustomTrack() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call GET /drafts/123/tracks/new-custom
        final var response = getGetResponse("/drafts/123/tracks/new-custom");

        // Then we get a form with dropdown lists for first and second Station
        final var customTrackForm = getElement("#custom-track-form", response);
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(customTrackForm.select("select[name='firstStationId']")).hasSize(1);
        assertThat(customTrackForm.select("select[name='secondStationId']")).hasSize(1);
        assertThat(customTrackForm.select("input[type='submit']")).hasSize(1);
    }

    @Test
    public void userCanAddANewCustomTrack() throws Exception {
        // When we call POST /drafts/123/tracks/new-custom with valid Track parameters
        final var response = getPostResponse(
            "/drafts/123/tracks/new-custom",
            Map.of("firstStationId", "1", "secondStationId", "2")
        );

        // Then an AddRailwayTrackCommand is issued with given Track parameters
        verify(addRailwayTrackCommand).addRailwayTrack("123", "1", "2");

        // And we will be redirected to the Tracks page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/tracks");
    }

    @Test
    public void userSeesValidationErrorsWhenAddingAnInvalidCustomTrack() throws Exception {
        final var firstStationErrors = List.of("Station 1 error");
        final var secondStationErrors = List.of("Station 2 error");
        final var validationException = new ValidationException(Map.of(
            "First Station ID", firstStationErrors,
            "Second Station ID", secondStationErrors
        ));
        given(addRailwayTrackCommand.addRailwayTrack(any(), any(), any()))
            .willThrow(validationException);

        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // When we call POST /drafts/123/tracks/new-custom with invalid Track parameters
        final var response = getPostResponse(
            "/drafts/123/tracks/new-custom",
            Map.of("firstStationId", "2", "secondStationId", "2")
        );

        // Then each Station selector has the invalid value
        final var customTrackForm = getElement("#custom-track-form", response);
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(customTrackForm.select("select[name='firstStationId']").val()).isEqualTo("2");
        assertThat(customTrackForm.select("select[name='secondStationId']").val()).isEqualTo("2");

        // And each Station selector shows it's error messages
        assertThat(customTrackForm.select(".errors.firstStationId li").eachText())
            .isEqualTo(firstStationErrors);
        assertThat(customTrackForm.select(".errors.secondStationId li").eachText())
            .isEqualTo(secondStationErrors);
    }

    @Test
    public void userCanDeleteAnExistingTrack() throws Exception {
        // When we call GET /drafts/123/tracks/1/2/delete
        final var response = getGetResponse("/drafts/123/tracks/1/2/delete");

        // Then a DeleteRailwayTrackCommand is issued
        verify(deleteRailwayTrackCommand).deleteRailwayTrack("123", "1", "2");

        // And we will be redirected to the Tracks page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123/tracks");
    }
}