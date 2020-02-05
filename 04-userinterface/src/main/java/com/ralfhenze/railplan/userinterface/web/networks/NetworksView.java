package com.ralfhenze.railplan.userinterface.web.networks;

import com.ralfhenze.railplan.userinterface.web.views.MasterView;
import com.ralfhenze.railplan.userinterface.web.views.View;
import j2html.tags.Tag;

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
public class NetworksView implements View {

    private final List<String> networkIds;

    private static class IndexedNetworkId {
        public int index;
        public String networkId;
    }

    public NetworksView(final List<String> networkIds) {
        this.networkIds = networkIds;
    }

    @Override
    public Tag getHtml() {
        final var indexedNetworkIds = getIndexedNetworkIds(networkIds);

        return new MasterView().with(
            div().withClasses("networks", "fullscreen-wrapper").with(
                div().withId("index-box").withClass("box").with(
                    div().withId("network-selector").with(
                        h3("Rail Networks"),
                        nav().withId("network-navigation").attr("aria-label", "Network Selection").with(
                            ul(
                                each(indexedNetworkIds, networkId ->
                                    li(
                                        a().withHref("/networks/" + networkId.networkId)
                                            .withClass("network-button")
                                            .withText("Network " + networkId.index),
                                        a().withHref("/networks/" + networkId.networkId + "/delete")
                                            .withClass("delete-button")
                                            .withText("x").attr("title", "Delete")
                                    )
                                )
                            )
                        ),
                        div(
                            a().withHref("/networks/new")
                                .withId("add-network-button")
                                .withClass("add-button")
                                .withText("+ Add Rail Network")
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
