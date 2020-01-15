package com.ralfhenze.railplan.userinterface.web.views;

import com.ralfhenze.railplan.userinterface.web.views.MasterView.SelectedNavEntry;

import static j2html.TagCreator.a;
import static j2html.TagCreator.br;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.h3;

/**
 * An HTML view for the landing page.
 */
public class IndexView {

    public String getHtml() {
        return new MasterView(SelectedNavEntry.START).with(
            div().withClasses("index", "fullscreen-wrapper").with(
                div().withId("index-box").withClass("box").with(
                    h2("Create your own railway network!"),
                    br(),
                    h3(a().withHref("/drafts").withText("Drafts")),
                    h3(a().withHref("/networks").withText("Networks"))
                )
            )
        );
    }
}
