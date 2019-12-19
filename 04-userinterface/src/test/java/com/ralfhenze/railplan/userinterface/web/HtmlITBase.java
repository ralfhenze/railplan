package com.ralfhenze.railplan.userinterface.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public abstract class HtmlITBase {

    protected final static int HTTP_OK = 200;
    protected final static int HTTP_MOVED_TEMPORARILY = 302;

    @Autowired
    protected MockMvc mockMvc;

    protected MockHttpServletResponse getGetResponse(final String url) throws Exception {
        return mockMvc.perform(get(url)).andReturn().getResponse();
    }

    protected MockHttpServletResponse getPostResponse(
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

    protected Element getElement(
        final String cssSelector,
        final MockHttpServletResponse response
    ) throws Exception {
        return Jsoup.parse(response.getContentAsString()).selectFirst(cssSelector);
    }
}
