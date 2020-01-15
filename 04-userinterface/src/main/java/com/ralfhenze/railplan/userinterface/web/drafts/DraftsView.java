package com.ralfhenze.railplan.userinterface.web.drafts;

import com.ralfhenze.railplan.userinterface.web.views.MasterView;
import com.ralfhenze.railplan.userinterface.web.views.MasterView.SelectedNavEntry;
import com.ralfhenze.railplan.userinterface.web.views.View;
import j2html.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static j2html.TagCreator.a;
import static j2html.TagCreator.br;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h3;
import static j2html.TagCreator.join;
import static j2html.TagCreator.li;
import static j2html.TagCreator.nav;
import static j2html.TagCreator.ul;

/**
 * An HTML view for the Drafts page.
 */
public class DraftsView implements View {

    private final List<String> draftIds;

    private static class IndexedDraftId {
        public int index;
        public String draftId;
    }

    public DraftsView(final List<String> draftIds) {
        this.draftIds = draftIds;
    }

    @Override
    public Tag getHtml() {
        final var indexedDraftIds = getIndexedDraftIds(draftIds);

        return new MasterView(SelectedNavEntry.DRAFTS).with(
            div().withClasses("drafts", "fullscreen-wrapper").with(
                div().withId("index-box").withClass("box").with(
                    div().withId("draft-selector").with(
                        h3("Rail Network Drafts"),
                        nav().withId("draft-navigation").attr("aria-label", "Draft Selection").with(
                            ul(
                                each(indexedDraftIds, draftId ->
                                    li(
                                        join(
                                            a().withHref("/drafts/" + draftId.draftId)
                                                .withText("Draft " + draftId.index),
                                            " (",
                                            a().withHref("/drafts/" + draftId.draftId + "/delete")
                                                .withText("Delete"),
                                            ")"
                                        )
                                    )
                                )
                            )
                        ),
                        div(
                            br(),
                            a().withHref("/drafts/new")
                                .withId("add-draft-button")
                                .withClass("add-button")
                                .withText("+ Add Draft")
                        )
                    )
                )
            )
        );
    }

    private List<IndexedDraftId> getIndexedDraftIds(final List<String> draftIds) {
        return IntStream
            .range(0, draftIds.size())
            .mapToObj(i -> {
                final var indexedDraftId = new IndexedDraftId();
                indexedDraftId.index = i + 1;
                indexedDraftId.draftId = draftIds.get(i);

                return indexedDraftId;
            })
            .collect(Collectors.toList());
    }
}
