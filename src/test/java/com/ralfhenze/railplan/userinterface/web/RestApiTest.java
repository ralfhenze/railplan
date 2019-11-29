package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Test
    public void should_provide_all_drafts() throws Exception {
        // Given an existing Draft
        given(mongoTemplate.findAll(any(), any()))
            .willReturn(List.of(getDraftDto()));

        // When we call GET /api/drafts
        final var response = mockMvc
            .perform(get("/api/drafts")).andReturn().getResponse();

        // Then we get the Draft as JSON
        then(response.getStatus()).isEqualTo(200);
        then(response.getContentAsString()).isEqualTo(
            "[{\"id\":\"123\",\"stations\":[],\"tracks\":[]}]"
        );
    }

    private RailNetworkDraftDto getDraftDto() {
        final var draftDto = new RailNetworkDraftDto();
        draftDto.setId("123");
        draftDto.setStations(List.of());
        draftDto.setTracks(List.of());

        return draftDto;
    }
}
