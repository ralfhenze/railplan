package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import com.ralfhenze.railplan.userinterface.web.views.GermanyMapSvgView;
import com.ralfhenze.railplan.userinterface.web.views.MasterView;
import com.ralfhenze.railplan.userinterface.web.views.MasterView.SelectedNavEntry;
import com.ralfhenze.railplan.userinterface.web.views.NetworkElementTabsView;
import com.ralfhenze.railplan.userinterface.web.views.NetworkElementTabsView.SelectedTab;
import com.ralfhenze.railplan.userinterface.web.views.View;
import j2html.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static j2html.TagCreator.a;
import static j2html.TagCreator.caption;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h3;
import static j2html.TagCreator.iff;
import static j2html.TagCreator.iffElse;
import static j2html.TagCreator.input;
import static j2html.TagCreator.join;
import static j2html.TagCreator.li;
import static j2html.TagCreator.option;
import static j2html.TagCreator.select;
import static j2html.TagCreator.span;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tag;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.tr;
import static j2html.TagCreator.ul;

/**
 * An HTML view that renders a table of Stations and corresponding forms.
 */
public class StationsView implements View {

    private final String draftId;
    private final RailNetworkDraftRepository draftRepository;
    private int stationIdToEdit = -1;
    private boolean showCustomStationForm = false;
    private boolean showPresetStationForm = false;
    private RailNetworkDraft draft;
    private ValidationException validationException;
    private PresetStationFormModel presetStationFormModel;
    private StationTableRow stationTableRow;

    public StationsView(
        final String draftId,
        final RailNetworkDraftRepository draftRepository
    ) {
        this.draftId = draftId;
        this.draftRepository = draftRepository;
    }

    public StationsView withStationIdToEdit(final int stationIdToEdit) {
        this.stationIdToEdit = stationIdToEdit;
        return this;
    }

    public StationsView withShowCustomStationForm(final boolean showCustomStationForm) {
        this.showCustomStationForm = showCustomStationForm;
        return this;
    }

    public StationsView withShowPresetStationForm(final boolean showPresetStationForm) {
        this.showPresetStationForm = showPresetStationForm;
        return this;
    }

    public StationsView withPresetStationFormModel(
        final PresetStationFormModel presetStationFormModel
    ) {
        this.presetStationFormModel = presetStationFormModel;
        return this;
    }

    public StationsView withStationTableRow(final StationTableRow stationTableRow) {
        this.stationTableRow = stationTableRow;
        return this;
    }

    public StationsView withValidationException(final ValidationException validationException) {
        this.validationException = validationException;
        return this;
    }

    private RailNetworkDraftDto getDraftDto() {
        draft = draftRepository.getRailNetworkDraftOfId(new RailNetworkDraftId(draftId));

        return new RailNetworkDraftDto(draft);
    }

    private List<StationTableRow> getStationTableRows() {
        final var stations = draft.getStations();

        return IntStream
            .range(0, stations.size())
            .mapToObj(index -> {
                final var station = stations.get(index);
                final var row = new StationTableRow();
                row.index = index + 1;
                row.stationId = station.getId().getId();
                row.stationName = station.getName().getName();
                row.latitude = station.getLocation().getLatitudeAsString();
                row.longitude = station.getLocation().getLongitudeAsString();
                row.showInputField = (row.stationId == stationIdToEdit);

                if (row.stationId == stationIdToEdit) {
                    if (validationException != null) {
                        row.stationNameErrors = validationException.getErrorsOfField(Field.STATION_NAME);
                        row.locationErrors = validationException.getErrorsOfField(Field.LOCATION);
                        row.latitudeErrors = validationException.getErrorsOfField(Field.LATITUDE);
                        row.longitudeErrors = validationException.getErrorsOfField(Field.LONGITUDE);
                    }

                    setStationRowPropertiesIfModelContainsThem(row);
                }

                return row;
            })
            .collect(Collectors.toList());
    }

    private StationTableRow getNewStationTableRow() {
        final var newStationTableRow = new StationTableRow();

        setStationRowPropertiesIfModelContainsThem(newStationTableRow);

        newStationTableRow.showInputField = true;
        newStationTableRow.disabled = (stationIdToEdit != -1);

        if (stationIdToEdit == -1 && validationException != null) {
            newStationTableRow.stationNameErrors = validationException.getErrorsOfField(Field.STATION_NAME);
            newStationTableRow.locationErrors = validationException.getErrorsOfField(Field.LOCATION);
            newStationTableRow.latitudeErrors = validationException.getErrorsOfField(Field.LATITUDE);
            newStationTableRow.longitudeErrors = validationException.getErrorsOfField(Field.LONGITUDE);
        }

        return newStationTableRow;
    }

    private void setStationRowPropertiesIfModelContainsThem(
        final StationTableRow row
    ) {
        if (stationTableRow != null) {
            row.stationName = stationTableRow.getStationName();
            row.latitude = stationTableRow.getLatitude();
            row.longitude = stationTableRow.getLongitude();
        }
    }

    @Override
    public Tag getHtml() {
        final var draftDto = getDraftDto();
        final var stationTableRows = getStationTableRows();
        final var showCustomStationForm = this.showCustomStationForm;
        final var newStationTableRow = getNewStationTableRow();
        final var showPresetStationForm = this.showPresetStationForm;
        final var presetStationFormModel = (this.presetStationFormModel == null) ?
            new PresetStationFormModel() : this.presetStationFormModel;

        return new MasterView(SelectedNavEntry.DRAFTS).with(
            div().withId("data-panel").with(
                div().withId("network-elements-box").withClass("box").with(
                    new NetworkElementTabsView(SelectedTab.STATIONS, draftId).getHtml(),
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
                        iff(
                            showPresetStationForm,
                            getPresetStationForm(draftId, draftDto.getStations(), presetStationFormModel)
                        )
                    )
                )
            ),
            new GermanyMapSvgView(draftDto.getStations(), draftDto.getTracks()).getHtml()
        );
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
                input().withType("submit").withClass("add-button").withValue("Update"),
                a().withHref("/drafts/" + draftId + "/stations/").withText("Cancel")
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
        final List<TrainStationDto> stations,
        final PresetStationFormModel presetStationFormModel
    ) {
        final var stationNames = stations.stream()
            .map(TrainStationDto::getName)
            .collect(Collectors.toList());
        final var presetStations = PresetStation.getAllPresetStations().stream()
            .filter(s -> !stationNames.contains(s.getName()))
            .collect(Collectors.toList());

        return form().withId("preset-station-form").withMethod("post").with(
            select().attr("multiple", "multiple").attr("size", 10).withName("presetStationsToAdd").with(
                each(presetStations, presetStation ->
                    option()
                        .withValue(presetStation.getName())
                        .withText(presetStation.getName())
                        .condAttr(
                            presetStationFormModel.getPresetStationsToAdd()
                                .contains(presetStation.getName()),
                            "selected",
                            "selected"
                        )
                )
            ),
            // TODO: render presetStationErrors
            input().withType("submit").withClass("add-button").withValue("Add Stations"),
            a().withHref("/drafts/" + draftId + "/stations").withText("Cancel")
        );
    }
}
