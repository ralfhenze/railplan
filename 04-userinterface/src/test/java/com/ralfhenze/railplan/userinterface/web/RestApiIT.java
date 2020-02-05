package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RestApiIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Test
    public void providesAllNetworks() throws Exception {
        // Given an existing Network
        given(mongoTemplate.findAll(any(), any()))
            .willReturn(List.of(getNetworkDto()));

        // When we call GET /api/networks
        final var response = mockMvc
            .perform(get("/api/networks")).andReturn().getResponse();

        // Then we get the Network as JSON
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(
            "[{\"id\":\"123\",\"stations\":[],\"tracks\":[]}]"
        );
    }

    private RailNetworkDto getNetworkDto() {
        final var networkDto = new RailNetworkDto();
        networkDto.setId("123");
        networkDto.setStations(List.of());
        networkDto.setTracks(List.of());

        return networkDto;
    }
}
