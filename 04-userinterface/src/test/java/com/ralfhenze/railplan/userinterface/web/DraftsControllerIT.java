package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.RailNetworkDraftService;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DraftsControllerIT extends HtmlITBase {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private Queries queries;

    @MockBean
    private RailNetworkDraftService railNetworkDraftService;

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
        given(railNetworkDraftService.addDraft())
            .willReturn(Optional.of(new RailNetworkDraft().withId(new RailNetworkDraftId("123"))));

        // When we call GET /drafts/new
        final var response = getGetResponse("/drafts/new");

        // Then an AddRailNetworkDraftCommand is issued
        verify(railNetworkDraftService).addDraft();

        // And we will be redirected to the new Draft
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts/123");
    }

    @Test
    public void userCanDeleteAnExistingDraft() throws Exception {
        // When we call GET /drafts/123/delete
        final var response = getGetResponse("/drafts/123/delete");

        // Then a DeleteRailNetworkDraftCommand is issued
        final var commandCaptor = ArgumentCaptor.forClass(DeleteRailNetworkDraftCommand.class);
        verify(railNetworkDraftService).deleteDraft(commandCaptor.capture());

        final var executedCommand = commandCaptor.getValue();
        assertThat(executedCommand.getDraftId()).isEqualTo("123");

        // And we will be redirected to the Drafts overview
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/drafts");
    }
}
