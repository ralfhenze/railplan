package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.userinterface.web.GermanySvgViewFragment;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

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

    public StationsView withDraft(final RailNetworkDraft draft) {
        this.draft = draft;
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
        if (draft == null) {
            draft = draftRepository.getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId));
        }

        return new RailNetworkDraftDto(draft);
    }

    private List<StationTableRow> getStationTableRows(final Model model) {
        return draft
            .getStations()
            .stream()
            .map(station -> {
                final var row = new StationTableRow();
                row.stationId = station.getId().toString();
                row.stationName = station.getName().getName();
                row.latitude = station.getLocation().getLatitudeAsString();
                row.longitude = station.getLocation().getLongitudeAsString();
                row.showInputField = (row.stationId.equals(stationIdToEdit));

                if (row.stationId.equals(stationIdToEdit)) {
                    if (!station.isValid()) {
                        row.stationNameErrors = getErrorsAsString(station.getName().getValidationErrors());
                        row.latitudeErrors = getErrorsAsString(station.getLocation().getLatitudeErrors());
                        row.longitudeErrors = getErrorsAsString(station.getLocation().getLongitudeErrors());
                    }

                    setStationRowPropertiesIfModelContainsThem(row, model);
                }

                return row;
            })
            .collect(Collectors.toList());
    }

    private List<String> getErrorsAsString(List<ValidationError> validationErrors) {
        return validationErrors.stream().map(e -> e.getMessage()).collect(Collectors.toList());
    }

    private StationTableRow getNewStationTableRow(final Model model) {
        final var newStationTableRow = new StationTableRow();

        setStationRowPropertiesIfModelContainsThem(newStationTableRow, model);

        newStationTableRow.showInputField = true;
        newStationTableRow.disabled = (stationIdToEdit != null);

        if (stationIdToEdit == null && draft != null) {
            draft.getStations().getLastOptional().ifPresent(station -> {
                newStationTableRow.stationNameErrors = getErrorsAsString(station.getName().getValidationErrors());
                newStationTableRow.latitudeErrors = getErrorsAsString(station.getLocation().getLatitudeErrors());
                newStationTableRow.longitudeErrors = getErrorsAsString(station.getLocation().getLongitudeErrors());
            });
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
}
