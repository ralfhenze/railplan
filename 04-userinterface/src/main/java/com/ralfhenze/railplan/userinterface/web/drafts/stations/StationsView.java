package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import com.ralfhenze.railplan.userinterface.web.GermanySvgViewFragment;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
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
    private Map<String, List<String>> stationErrors;

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

    public StationsView withShowPresetStationForm(final boolean showPresetStationForm) {
        this.showPresetStationForm = showPresetStationForm;
        return this;
    }

    public StationsView withStationErrorsProvidedBy(final ValidationException exception) {
        this.stationErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public StationsView addRequiredAttributesTo(final Model model) {
        final var draftDto = getDraftDto();
        final var stationNames = getStationNames(draftDto);

        model.addAttribute("currentDraftDto", draftDto);
        model.addAttribute("stationNames", stationNames);

        model.addAttribute("stationTableRows", getStationTableRows(model, draftDto));
        model.addAttribute("showCustomStationForm", showCustomStationForm);
        model.addAttribute("newStationTableRow", getNewStationTableRow(model));

        model.addAttribute("showPresetStationForm", showPresetStationForm);
        model.addAttribute("allPresetStations", PresetStation.values());
        model.addAttribute("presetStationFormModel", new PresetStationFormModel());

        new GermanySvgViewFragment(draftDto.getStations(), draftDto.getTracks())
            .addRequiredAttributesTo(model);

        return this;
    }

    private RailNetworkDraftDto getDraftDto() {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId));

        return new RailNetworkDraftDto(draft);
    }

    private Map<Integer, String> getStationNames(final RailNetworkDraftDto draftDto) {
        return draftDto
            .getStations()
            .stream()
            .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));
    }

    private List<StationTableRow> getStationTableRows(
        final Model model,
        final RailNetworkDraftDto draftDto
    ) {
        return draftDto
            .getStations()
            .stream()
            .map(stationDto -> {
                final var row = new StationTableRow();
                row.stationId = String.valueOf(stationDto.getId());
                row.stationName = stationDto.getName();
                row.latitude = String.valueOf(stationDto.getLatitude());
                row.longitude = String.valueOf(stationDto.getLongitude());
                row.showInputField = (row.stationId.equals(stationIdToEdit));

                if (row.stationId.equals(stationIdToEdit)) {
                    if (stationErrors != null) {
                        row.stationNameErrors = stationErrors.getOrDefault("Station name", List.of());
                        row.latitudeErrors = stationErrors.getOrDefault("Latitude", List.of());
                        row.longitudeErrors = stationErrors.getOrDefault("Longitude", List.of());
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

        if (stationIdToEdit == null && stationErrors != null) {
            newStationTableRow.stationNameErrors = stationErrors.getOrDefault("Station name", List.of());
            newStationTableRow.latitudeErrors = stationErrors.getOrDefault("Latitude", List.of());
            newStationTableRow.longitudeErrors = stationErrors.getOrDefault("Longitude", List.of());
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
