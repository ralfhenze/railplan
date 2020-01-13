package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.userinterface.web.GermanySvgViewFragment;
import j2html.Config;
import j2html.tags.ContainerTag;
import j2html.tags.EmptyTag;
import j2html.tags.Tag;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.caption;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.h3;
import static j2html.TagCreator.head;
import static j2html.TagCreator.header;
import static j2html.TagCreator.html;
import static j2html.TagCreator.iff;
import static j2html.TagCreator.iffElse;
import static j2html.TagCreator.input;
import static j2html.TagCreator.join;
import static j2html.TagCreator.li;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.nav;
import static j2html.TagCreator.option;
import static j2html.TagCreator.select;
import static j2html.TagCreator.span;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tag;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.title;
import static j2html.TagCreator.tr;
import static j2html.TagCreator.ul;

/**
 * Prepares the necessary data for resources/templates/stations.html.
 */
public class StationsView {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private String stationIdToEdit;
    private boolean showCustomStationForm = false;
    private boolean showPresetStationForm = false;
    private boolean showPresetStationFormErrors = false;
    private RailNetworkDraft draft;
    private ValidationException validationException;

    public StationsView(
        final String currentDraftId,
        final RailNetworkDraftRepository draftRepository
    ) {
        this.currentDraftId = currentDraftId;
        this.draftRepository = draftRepository;
    }

    public String getViewName() {
        return "stations";
    }

    public StationsView withStationIdToEdit(final String stationIdToEdit) {
        this.stationIdToEdit = stationIdToEdit;
        return this;
    }

    public StationsView withShowCustomStationForm(final boolean showCustomStationForm) {
        this.showCustomStationForm = showCustomStationForm;
        return this;
    }

    public StationsView withShowPresetStationForm(
        final boolean showPresetStationForm,
        final boolean showPresetStationFormErrors
    ) {
        this.showPresetStationForm = showPresetStationForm;
        this.showPresetStationFormErrors = showPresetStationFormErrors;
        return this;
    }

    public StationsView withValidationException(final ValidationException validationException) {
        this.validationException = validationException;
        return this;
    }

    public StationsView addRequiredAttributesTo(final Model model) {
        final var draftDto = getDraftDto();

        model.addAttribute("stationTableRows", getStationTableRows(model));
        model.addAttribute("showCustomStationForm", showCustomStationForm);
        model.addAttribute("newStationTableRow", getNewStationTableRow(model));

        model.addAttribute("showPresetStationForm", showPresetStationForm);
        model.addAttribute("allPresetStations", PresetStation.values());
        if (!showPresetStationFormErrors) {
            model.addAttribute("presetStationFormModel", new PresetStationFormModel());
        }

        new GermanySvgViewFragment(draftDto.getStations(), draftDto.getTracks())
            .addRequiredAttributesTo(model);

        return this;
    }

    private RailNetworkDraftDto getDraftDto() {
        draft = draftRepository.getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId));

        return new RailNetworkDraftDto(draft);
    }

    private List<StationTableRow> getStationTableRows(final Model model) {
        final var stations = draft.getStations();

        return IntStream
            .range(0, stations.size())
            .mapToObj(index -> {
                final var station = stations.get(index);
                final var row = new StationTableRow();
                row.index = index + 1;
                row.stationId = station.getId().toString();
                row.stationName = station.getName().getName();
                row.latitude = station.getLocation().getLatitudeAsString();
                row.longitude = station.getLocation().getLongitudeAsString();
                row.showInputField = (row.stationId.equals(stationIdToEdit));

                if (row.stationId.equals(stationIdToEdit)) {
                    if (validationException != null) {
                        row.stationNameErrors = validationException.getErrorsOfField(Field.STATION_NAME);
                        row.locationErrors = validationException.getErrorsOfField(Field.LOCATION);
                        row.latitudeErrors = validationException.getErrorsOfField(Field.LATITUDE);
                        row.longitudeErrors = validationException.getErrorsOfField(Field.LONGITUDE);
                    }

                    setStationRowPropertiesIfModelContainsThem(row, model);
                }

                return row;
            })
            .collect(Collectors.toList());
    }

    private StationTableRow getNewStationTableRow(final Model model) {
        final var newStationTableRow = new StationTableRow();

        setStationRowPropertiesIfModelContainsThem(newStationTableRow, model);

        newStationTableRow.showInputField = true;
        newStationTableRow.disabled = (stationIdToEdit != null);

        if (stationIdToEdit == null && validationException != null) {
            newStationTableRow.stationNameErrors = validationException.getErrorsOfField(Field.STATION_NAME);
            newStationTableRow.locationErrors = validationException.getErrorsOfField(Field.LOCATION);
            newStationTableRow.latitudeErrors = validationException.getErrorsOfField(Field.LATITUDE);
            newStationTableRow.longitudeErrors = validationException.getErrorsOfField(Field.LONGITUDE);
        }

        return newStationTableRow;
    }

    private void setStationRowPropertiesIfModelContainsThem(
        final StationTableRow row,
        final Model model
    ) {
        if (model.containsAttribute("updatedStationTableRow")) {
            final var updatedStationTableRow = (StationTableRow) model
                .getAttribute("updatedStationTableRow");
            row.stationName = updatedStationTableRow.getStationName();
            row.latitude = updatedStationTableRow.getLatitude();
            row.longitude = updatedStationTableRow.getLongitude();
        }
    }

    public String getHtml(final Model model) {
        final var selectedNavEntry = "drafts";
        final var selectedTab = "stations";
        final var draftId = currentDraftId;
        final var draftDto = getDraftDto();
        final var svg = new GermanySvgViewFragment(draftDto.getStations(), draftDto.getTracks());
        final var stationTableRows = getStationTableRows(model);
        final var showCustomStationForm = this.showCustomStationForm;
        final var newStationTableRow = getNewStationTableRow(model);
        final var showPresetStationForm = this.showPresetStationForm;
        final var allPresetStations = List.of(PresetStation.values());

        Config.closeEmptyTags = true;

        return "<!DOCTYPE HTML>\n" + html().attr("lang", "en-US").with(
            head(
                title("RailPlan"),
                meta().attr("http-equiv", "Content-Type").attr("content", "text/html; charset=UTF-8"),
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
                                    .withCondClass("drafts".equals(selectedNavEntry), "selected")
                                    .withText("Drafts")
                            ),
                            li(
                                a().withHref("/networks")
                                    .withCondClass("networks".equals(selectedNavEntry), "selected")
                                    .withText("Networks")
                            )
                        )
                    )
                ),
                div().withId("data-panel").with(
                    div().withId("network-elements-box").withClass("box").with(
                        nav().attr("aria-label", "Network Element Tabs").with(
                            ul(
                                li(
                                    a().withHref("/drafts/" + draftId + "/stations")
                                        .withCondClass("stations".equals(selectedTab), "selected")
                                        .withText("Train Stations")
                                ),
                                li(
                                    a().withHref("/drafts/" + draftId + "/tracks")
                                        .withCondClass("tracks".equals(selectedTab), "selected")
                                        .withText("Railway Tracks")
                                ),
                                li(
                                    a().withHref("/drafts/" + draftId + "/release")
                                        .withCondClass("release".equals(selectedTab), "selected")
                                        .withText("Release")
                                )
                            )
                        ),
                        div().withId("stations").with(
                            h1("Draft"),
                            form().withMethod("post").with(
                                table(
                                    caption(h3("Train Stations")),
                                    thead(
                                        tr(
                                            th().attr("scope", "col").withText("#"),
                                            th().attr("scope", "col").withText("Name"),
                                            th().attr("scope", "col").withText("Latitude"),
                                            th().attr("scope", "col").withText("Longitude"),
                                            th().attr("scope", "col").withText("Actions")
                                        )
                                    ),
                                    tbody(
                                        each(stationTableRows, row ->
                                            tag(null).with(
                                                tr().withClass("station-row").with(
                                                    td(span().withText(String.valueOf(row.index))),
                                                    td(getStationTableCell("stationName", row.stationName, row.showInputField, row.stationNameIsInvalid())),
                                                    td(getStationTableCell("latitude", row.latitude, row.showInputField, row.latitudeIsInvalid())),
                                                    td(getStationTableCell("longitude", row.longitude, row.showInputField, row.longitudeIsInvalid())),
                                                    td(getActionsCell(draftId, row))
                                                ),
                                                getErrorsRow(row)
                                            )
                                        ),
                                        getLastStationsTableRow(draftId, showCustomStationForm, showPresetStationForm, newStationTableRow),
                                        iff(newStationTableRow.hasErrors(), getErrorsRow(newStationTableRow))
                                    )
                                )
                            ),
                            iff(showPresetStationForm, getPresetStationForm(draftId, allPresetStations))
                        )
                    )
                ),
                div().withId("germany-map").withClass("box").with(
                    svg().attr("viewBox", "0 0 " + svg.MAP_WIDTH + " " + svg.MAP_HEIGHT).with(
                        path().attr("d", svg.getPath()),
                        each(svg.getTrackCoordinates(), coordinates ->
                            line()
                                .attr("x1", coordinates.get(0)).attr("y1", coordinates.get(1))
                                .attr("x2", coordinates.get(2)).attr("y2", coordinates.get(3))
                        ),
                        each(svg.getStationCoordinates(), coordinates ->
                            circle()
                                .attr("cx", coordinates.get(0)).attr("cy", coordinates.get(1))
                                .attr("r", "4")
                        )
                    )
                )
            )
        ).renderFormatted();
    }

    private static ContainerTag svg() {
        return new ContainerTag("svg");
    }

    private static EmptyTag path() {
        return new EmptyTag("path");
    }

    private static EmptyTag line() {
        return new EmptyTag("line");
    }

    private static EmptyTag circle() {
        return new EmptyTag("circle");
    }

    private static Tag getStationTableCell(
        final String name,
        final String value,
        final boolean showInputField,
        final boolean isInvalid
    ) {
        if (showInputField) {
            return input().withCondClass(isInvalid, "invalid").withType("text").withName(name).withValue(value);
        } else {
            return span().withClass(name).withText(value);
        }
    }

    private static Tag getActionsCell(final String draftId, final StationTableRow row) {
        if (row.showInputField) {
            return div(
                join(
                    input().withType("submit").withClass("add-button").withValue("Update"),
                    " | ",
                    a().withHref("/drafts/" + draftId + "/stations/").withText("Cancel")
                )
            );
        } else {
            return div(
                join(
                    a().withHref("/drafts/" + draftId + "/stations/" + row.stationId + "/edit")
                        .withText("Edit"),
                    " | ",
                    a().withHref("/drafts/" + draftId + "/stations/" + row.stationId + "/delete")
                        .withText("Delete")
                )
            );
        }
    }

    private static Tag getErrorsRow(final StationTableRow row) {
        if (row.hasErrors()) {
            return tr(
                td(),
                td(
                    getErrorMessages("stationName", row.stationNameErrors)
                ),
                iffElse(row.locationErrors.isEmpty(),
                    tag(null).with(
                        td(getErrorMessages("latitude", row.latitudeErrors)),
                        td(getErrorMessages("longitude", row.longitudeErrors))
                    ),
                    td().attr("colspan", "2").with(
                        getErrorMessages("location", row.locationErrors)
                    )
                ),
                td()
            );
        }

        return null;
    }

    private static Tag getErrorMessages(final String name, final List<String> errors) {
        if (!errors.isEmpty()) {
            return ul().withClasses("errors", name).with(
                each(errors, error -> li().withText(error))
            );
        }

        return null;
    }

    private static Tag getLastStationsTableRow(
        final String draftId,
        final boolean showCustomStationForm,
        final boolean showPresetStationForm,
        final StationTableRow row
    ) {
        if (showCustomStationForm) {
            return tr(
                td().withText("+"),
                td(getStationTableCell("stationName", row.stationName, row.showInputField, row.stationNameIsInvalid())),
                td(getStationTableCell("latitude", row.latitude, row.showInputField, row.latitudeIsInvalid())),
                td(getStationTableCell("longitude", row.longitude, row.showInputField, row.longitudeIsInvalid())),
                td(
                    input().withType("submit").withClass("add-button").withValue("Add Station"),
                    a().withHref("/drafts/" + draftId + "/stations").withText("Cancel")
                )
            );
        } else if (!showPresetStationForm) {
            return tr().withClass("add-button-row").with(
                td().attr("colspan", "5").with(
                    a().withClass("add-button")
                        .withHref("/drafts/" + draftId + "/stations/new-from-preset")
                        .withText("+ Add Preset Stations"),
                    a().withClass("add-button")
                        .withHref("/drafts/" + draftId + "/stations/new-custom")
                        .withText("+ Add Custom Station")
                )
            );
        }

        return null;
    }

    private static Tag getPresetStationForm(
        final String draftId,
        final List<PresetStation> allPresetStations
    ) {
        return form().withId("preset-station-form").withMethod("post").with(
            select().attr("multiple", "multiple").attr("size", 10).withName("presetStationsToAdd").with(
                each(allPresetStations, presetStation ->
                    option().withValue(presetStation.name()).withText(presetStation.name)
                )
            ),
            input().withType("submit").withClass("add-button").withValue("Add Stations"),
            a().withHref("/drafts/" + draftId + "/stations").withText("Cancel")
        );
    }
}
