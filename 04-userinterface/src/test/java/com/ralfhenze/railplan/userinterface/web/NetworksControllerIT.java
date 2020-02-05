package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.RailNetworkService;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
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
public class NetworksControllerIT extends HtmlITBase {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private Queries queries;

    @MockBean
    private RailNetworkService railNetworkNetworkService;

    @Test
    public void userCanNavigateToExistingNetworks() throws Exception {
        // Given an existing Network ID "123"
        given(queries.getAllNetworkIds()).willReturn(List.of("123"));

        // When we call GET /networks
        final var response = getGetResponse("/networks");

        // Then the Network navigation contains one entry pointing to the Network
        final var document = Jsoup.parse(response.getContentAsString());
        final var networkNavEntries = document.select("#network-navigation li");

        assertThat(response.getStatus()).isEqualTo(HTTP_OK);
        assertThat(networkNavEntries).hasSize(1);
        assertThat(networkNavEntries.first().select("a").attr("href")).isEqualTo("/networks/123");
    }

    @Test
    public void userCanAddANewNetwork() throws Exception {
        // Given a Network with ID "123" will be created
        given(railNetworkNetworkService.addNetwork())
            .willReturn(Optional.of(new RailNetwork().withId(new RailNetworkId("123"))));

        // When we call GET /networks/new
        final var response = getGetResponse("/networks/new");

        // Then an AddRailNetworkCommand is issued
        verify(railNetworkNetworkService).addNetwork();

        // And we will be redirected to the new Network
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/networks/123");
    }

    @Test
    public void userCanDeleteAnExistingNetwork() throws Exception {
        // When we call GET /networks/123/delete
        final var response = getGetResponse("/networks/123/delete");

        // Then a DeleteRailNetworkCommand is issued
        final var commandCaptor = ArgumentCaptor.forClass(DeleteRailNetworkCommand.class);
        verify(railNetworkNetworkService).deleteNetwork(commandCaptor.capture());

        final var executedCommand = commandCaptor.getValue();
        assertThat(executedCommand.getNetworkId()).isEqualTo("123");

        // And we will be redirected to the Networks overview
        assertThat(response.getStatus()).isEqualTo(HTTP_MOVED_TEMPORARILY);
        assertThat(response.getRedirectedUrl()).isEqualTo("/networks");
    }
}
