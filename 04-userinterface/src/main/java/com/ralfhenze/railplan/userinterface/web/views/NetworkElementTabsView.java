package com.ralfhenze.railplan.userinterface.web.views;

import j2html.tags.Tag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.li;
import static j2html.TagCreator.nav;
import static j2html.TagCreator.ul;

/**
 * A tab navigation bar.
 */
public class NetworkElementTabsView {

    public enum SelectedTab {
        STATIONS, TRACKS, RELEASE
    }

    public Tag getTag(final String draftId, final SelectedTab selectedTab) {
        return nav().attr("aria-label", "Network Element Tabs").with(
            ul(
                li(
                    a().withHref("/drafts/" + draftId + "/stations")
                        .withCondClass(selectedTab.equals(SelectedTab.STATIONS), "selected")
                        .withText("Train Stations")
                ),
                li(
                    a().withHref("/drafts/" + draftId + "/tracks")
                        .withCondClass(selectedTab.equals(SelectedTab.TRACKS), "selected")
                        .withText("Railway Tracks")
                ),
                li(
                    a().withHref("/drafts/" + draftId + "/release")
                        .withCondClass(selectedTab.equals(SelectedTab.RELEASE), "selected")
                        .withText("Release")
                )
            )
        );
    }
}
