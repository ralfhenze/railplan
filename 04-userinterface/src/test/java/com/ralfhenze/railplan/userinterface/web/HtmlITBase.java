package com.ralfhenze.railplan.userinterface.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
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
        final Map<String, List<String>> parametersMap = new HashMap<>();
        parameters.forEach((key, value) -> parametersMap.put(key, List.of(value)));

        return getPostResponseWithMultiValueParameters(url, parametersMap);
    }

    protected MockHttpServletResponse getPostResponseWithMultiValueParameters(
        final String url,
        final Map<String, List<String>> parameters
    ) throws Exception {
        return mockMvc
            .perform(post(url).params(new LinkedMultiValueMap<>(parameters)))
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
