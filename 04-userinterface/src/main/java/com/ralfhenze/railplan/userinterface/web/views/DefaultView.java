package com.ralfhenze.railplan.userinterface.web.views;

import j2html.Config;
import j2html.tags.Tag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.header;
import static j2html.TagCreator.html;
import static j2html.TagCreator.li;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.nav;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.tag;
import static j2html.TagCreator.title;
import static j2html.TagCreator.ul;

/**
 * The wrapping HTML that is common to all pages.
 */
public class DefaultView {

    public enum SelectedNavEntry {
        START, DRAFTS, NETWORKS
    }

    public String getHtml(final SelectedNavEntry selectedNavEntry, final Tag... contentTags) {
        Config.closeEmptyTags = true;

        return tag(null).with(
            rawHtml("<!DOCTYPE html>"),
            html().attr("lang", "en-US").with(
                head(
                    title("RailPlan"),
                    meta()
                        .attr("http-equiv", "Content-Type")
                        .attr("content", "text/html; charset=UTF-8"),
                    link().withRel("stylesheet").withHref("/css/railplan.css")
                ),
                body(
                    header(
                        h2().withId("logo").with(
                            a().withHref("/").withText("RailPlan")
                        ),
                        nav().withId("main-navigation").with(
                            ul(
                                li(
                                    a().withHref("/drafts")
                                        .withCondClass(
                                            selectedNavEntry.equals(SelectedNavEntry.DRAFTS),
                                            "selected"
                                        )
                                        .withText("Drafts")
                                ),
                                li(
                                    a().withHref("/networks")
                                        .withCondClass(
                                            selectedNavEntry.equals(SelectedNavEntry.NETWORKS),
                                            "selected"
                                        )
                                        .withText("Networks")
                                )
                            )
                        )
                    ),
                    tag(null).with(
                        contentTags
                    )
                )
            )
        )
        .render();
    }
}
