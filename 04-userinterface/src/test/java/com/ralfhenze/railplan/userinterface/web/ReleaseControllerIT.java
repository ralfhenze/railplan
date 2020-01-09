package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.ReleasedRailNetworkService;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHamburgDraft;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ReleaseControllerIT extends HtmlITBase {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private RailNetworkDraftRepository draftRepository;

    @MockBean
    private ReleasedRailNetworkService releasedRailNetworkService;

    @Test
    public void userCanAccessAFormToReleaseAnExistingDraft() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

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
        // Given we will get a ReleasedRailNetwork
        final var network = mock(ReleasedRailNetwork.class);
        given(network.getId()).willReturn(Optional.of(new ReleasedRailNetworkId("1")));
        given(releasedRailNetworkService.releaseDraft(any())).willReturn(network);

        // When we call POST /drafts/123/release with valid parameters
        final var response = getPostResponse(
            "/drafts/123/release",
            Map.of("startDate", "2020-01-01", "endDate", "2020-01-31")
        );

        // Then a ReleaseRailNetworkCommand is issued with those parameters
        final var commandCaptor = ArgumentCaptor.forClass(ReleaseRailNetworkCommand.class);
        verify(releasedRailNetworkService).releaseDraft(commandCaptor.capture());

        final var executedCommand = commandCaptor.getValue();
        assertThat(executedCommand.getDraftId()).isEqualTo("123");
        assertThat(executedCommand.getStartDate()).isEqualTo(LocalDate.parse("2020-01-01"));
        assertThat(executedCommand.getEndDate()).isEqualTo(LocalDate.parse("2020-01-31"));

        // And we will be redirected to the Released Rail Network page
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/networks/1");
    }

    @Test
    public void userSeesValidationErrorsWhenReleasingADraftWithAnInvalidPeriod() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // And a ValidationException is thrown when attempting to release the Draft
        final var startDateError = "Start Date error";
        final var endDateError = "End Date error";
        final var validationException = new ValidationException(List.of(
            new ValidationError(startDateError, Field.START_DATE),
            new ValidationError(endDateError, Field.END_DATE)
        ));
        given(releasedRailNetworkService.releaseDraft(any())).willThrow(validationException);

        // When we call POST /drafts/123/release with invalid Date parameters
        final var response = getPostResponse(
            "/drafts/123/release",
            Map.of("startDate", "2020-01-01", "endDate", "2019-12-01")
        );

        // Then each Date field has the invalid value
        final var releaseForm = getElement("#release-form", response);
        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(releaseForm.select("input[name='startDate']").val()).isEqualTo("2020-01-01");
        assertThat(releaseForm.select("input[name='endDate']").val()).isEqualTo("2019-12-01");

        // And each Date field shows it's error messages
        assertThat(releaseForm.select(".errors.startDate li").eachText())
            .isEqualTo(List.of(startDateError));
        assertThat(releaseForm.select(".errors.endDate li").eachText())
            .isEqualTo(List.of(endDateError));
    }
}
