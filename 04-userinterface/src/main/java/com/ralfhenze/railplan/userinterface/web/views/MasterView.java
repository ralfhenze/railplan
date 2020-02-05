package com.ralfhenze.railplan.userinterface.web.views;

import j2html.Config;
import j2html.tags.Tag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.header;
import static j2html.TagCreator.html;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.tag;
import static j2html.TagCreator.title;

/**
 * The wrapping HTML that is common to all pages.
 */
public class MasterView implements View {

    private Tag[] contentTags;

    public Tag with(final Tag... contentTags) {
        this.contentTags = contentTags;
        return this.getHtml();
    }

    @Override
    public Tag getHtml() {
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
                        )
                    ),
                    tag(null).with(
                        contentTags
                    )
                )
            )
        );
    }
}
