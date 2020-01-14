package com.ralfhenze.railplan.userinterface.web.networks;

import com.ralfhenze.railplan.userinterface.web.views.DefaultView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static j2html.TagCreator.a;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h3;
import static j2html.TagCreator.li;
import static j2html.TagCreator.nav;
import static j2html.TagCreator.ul;

/**
 * An HTML view for the Networks page.
 */
public class NetworksView {

    private static class IndexedNetworkId {
        public int index;
        public String networkId;
    }

    public String getHtml(final List<String> networkIds) {
        final var indexedNetworkIds = getIndexedNetworkIds(networkIds);

        return new DefaultView().getHtml(DefaultView.SelectedNavEntry.START,
            div().withClasses("networks", "fullscreen-wrapper").with(
                div().withId("index-box").withClass("box").with(
                    div().withId("draft-selector").with(
                        h3("Released Rail Networks"),
                        nav().withId("network-navigation")
                            .attr("aria-label", "Network Selection")
                            .with(
                            ul(
                                each(indexedNetworkIds, networkId ->
                                    li(
                                        a().withHref("/networks/" + networkId.networkId)
                                            .withText("Network " + networkId.index)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    private List<IndexedNetworkId> getIndexedNetworkIds(final List<String> networkIds) {
        return IntStream
            .range(0, networkIds.size())
            .mapToObj(i -> {
                final var indexedNetworkId = new IndexedNetworkId();
                indexedNetworkId.index = i + 1;
                indexedNetworkId.networkId = networkIds.get(i);

                return indexedNetworkId;
            })
            .collect(Collectors.toList());
    }
}
