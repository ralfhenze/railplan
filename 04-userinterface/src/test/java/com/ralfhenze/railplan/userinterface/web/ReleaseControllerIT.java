package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHamburgDraft;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ReleaseControllerIT extends HtmlITBase {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private RailNetworkDraftRepository draftRepository;

    @MockBean
    private ReleaseRailNetworkCommand releaseRailNetworkCommand;

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
        // Given we will get an ID for our Released Rail Network
        given(releaseRailNetworkCommand.releaseRailNetworkDraft(any(), any(), any()))
            .willReturn(new ReleasedRailNetworkId("1"));

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

    @Test
    public void userSeesValidationErrorsWhenReleasingADraftWithAnInvalidPeriod() throws Exception {
        // Given an existing Draft
        given(draftRepository.getRailNetworkDraftOfId(any())).willReturn(berlinHamburgDraft);

        // And we will get validation errors when attempting to release the Draft
        final var startDateErrors = List.of("Start Date error");
        final var endDateErrors = List.of("End Date error");
        final var validationException = new ValidationException(Map.of(
            "Start Date", startDateErrors,
            "End Date", endDateErrors
        ));
        given(releaseRailNetworkCommand.releaseRailNetworkDraft(any(), any(), any()))
            .willThrow(validationException);

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
            .isEqualTo(startDateErrors);
        assertThat(releaseForm.select(".errors.endDate li").eachText())
            .isEqualTo(endDateErrors);
    }
}
