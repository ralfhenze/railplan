package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.queries.Queries;
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

import static org.assertj.core.api.Assertions.assertThat;
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

    private MockHttpServletResponse getGetResponse(final String url) throws Exception {
        return mockMvc.perform(get(url)).andReturn().getResponse();
    }
}
