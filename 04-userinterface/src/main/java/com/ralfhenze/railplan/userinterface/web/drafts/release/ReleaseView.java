package com.ralfhenze.railplan.userinterface.web.drafts.release;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.userinterface.web.views.DefaultView;
import com.ralfhenze.railplan.userinterface.web.views.GermanyMapSvgView;
import com.ralfhenze.railplan.userinterface.web.views.NetworkElementTabsView;
import j2html.tags.Tag;

import java.time.LocalDate;
import java.util.List;

import static j2html.TagCreator.a;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h4;
import static j2html.TagCreator.input;
import static j2html.TagCreator.label;
import static j2html.TagCreator.li;
import static j2html.TagCreator.p;
import static j2html.TagCreator.ul;

/**
 * An HTML view that provides the possibility to release a Draft.
 */
public class ReleaseView {

    private final String draftId;
    private final RailNetworkDraftRepository draftRepository;
    private ValidationException validationException;
    private ValidityPeriodDto validityPeriod = new ValidityPeriodDto();

    private static class ReleaseErrors {
        public List<String> startDateErrors = List.of();
        public List<String> endDateErrors = List.of();
    }

    public ReleaseView(
        final String draftId,
        final RailNetworkDraftRepository draftRepository
    ) {
        this.draftId = draftId;
        this.draftRepository = draftRepository;
    }

    public ReleaseView withValidationException(final ValidationException validationException) {
        this.validationException = validationException;
        return this;
    }

    public ReleaseView withValidityPeriod(final ValidityPeriodDto validityPeriod) {
        this.validityPeriod = validityPeriod;
        return this;
    }

    public String getHtml() {
        final var draftId = this.draftId;
        final var draftDto = getDraftDto();
        final var germanyMapSvg = new GermanyMapSvgView(
            draftDto.getStations(),
            draftDto.getTracks()
        );
        final var tabsView = new NetworkElementTabsView();

        final var validityPeriod = this.validityPeriod;
        final var releaseErrors = new ReleaseErrors();

        if (validationException == null) {
            validityPeriod.setStartDate(LocalDate.now().plusDays(1).toString());
            validityPeriod.setEndDate(LocalDate.now().plusDays(1).plusMonths(1).toString());
        } else {
            releaseErrors.startDateErrors = validationException.getErrorsOfField(Field.START_DATE);
            releaseErrors.endDateErrors = validationException.getErrorsOfField(Field.END_DATE);
        }

        return new DefaultView().getHtml(DefaultView.SelectedNavEntry.DRAFTS,
            div().withId("data-panel").with(
                div().withId("network-elements-box").withClass("box").with(
                    tabsView.getTag(draftId, NetworkElementTabsView.SelectedTab.RELEASE),
                    form().withId("release-form").withMethod("post").with(
                        h4("Validity Period"),
                        p(
                            label().attr("for", "start-date").withText("From:"),
                            input().withId("start-date").withName("startDate").withType("date")
                                .withValue(validityPeriod.getStartDate()),
                            getErrorMessagesListTag("startDate", releaseErrors.startDateErrors)
                        ),
                        p(
                            label().attr("for", "end-date").withText("Until:"),
                            input().withId("end-date").withName("endDate").withType("date")
                                .withValue(validityPeriod.getEndDate()),
                            getErrorMessagesListTag("endDate", releaseErrors.endDateErrors)
                        ),
                        p(
                            input().withType("submit").withClass("add-button")
                                .withValue("Release Draft"),
                            a().withHref("/drafts/" + draftId + "/release").withText("Cancel")
                        )
                    )
                )
            ),
            germanyMapSvg.getHtml()
        );
    }

    private Tag getErrorMessagesListTag(final String name, final List<String> errors) {
        if (!errors.isEmpty()) {
            return ul().withClasses("errors", name).with(
                each(errors, error -> li().withText(error))
            );
        }

        return null;
    }

    private RailNetworkDraftDto getDraftDto() {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(draftId));

        return new RailNetworkDraftDto(draft);
    }
}
