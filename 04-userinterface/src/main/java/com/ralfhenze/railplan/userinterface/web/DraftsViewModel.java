package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.javatuples.Pair;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A view model that prepares the necessary data for resources/templates/drafts.html.
 */
public class DraftsViewModel {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private final Queries queries;
    private String stationIdToEdit;
    private boolean showNewTrackForm = false;
    private boolean showNewStationForm = false;
    private Map<String, List<String>> stationErrors;
    private Map<String, List<String>> trackErrors = Map.of();

    public DraftsViewModel(
        final String currentDraftId,
        final RailNetworkDraftRepository draftRepository,
        final Queries queries
    ) {
        this.currentDraftId = currentDraftId;
        this.queries = queries;
        this.draftRepository = draftRepository;
    }

    public DraftsViewModel withStationIdToEdit(final String stationIdToEdit) {
        this.stationIdToEdit = stationIdToEdit;
        return this;
    }

    public DraftsViewModel withShowNewStationForm(final boolean showNewStationForm) {
        this.showNewStationForm = showNewStationForm;
        return this;
    }

    public DraftsViewModel withShowNewTrackForm(final boolean showNewTrackForm) {
        this.showNewTrackForm = showNewTrackForm;
        return this;
    }

    public DraftsViewModel withStationErrorsFrom(final ValidationException exception) {
        this.stationErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public DraftsViewModel withTrackErrorsFrom(final ValidationException exception) {
        this.trackErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public void writeTo(final Model model) {
        model.addAttribute("draftIds", queries.getAllDraftIds());

        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId));
        final var draftDto = new RailNetworkDraftDto(draft);

        model.addAttribute("currentDraftDto", draftDto);

        final Map<Integer, String> stationNames = draftDto
            .getStations()
            .stream()
            .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));

        model.addAttribute("stationNames", stationNames);

        final List<List<Pair<String, String>>> tracksWithStationNames = draftDto
            .getTracks()
            .stream()
            .map(trackDto -> List.of(
                Pair.with(
                    String.valueOf(trackDto.getFirstStationId()),
                    stationNames.get(trackDto.getFirstStationId())
                ),
                Pair.with(
                    String.valueOf(trackDto.getSecondStationId()),
                    stationNames.get(trackDto.getSecondStationId())
                )
            ))
            .collect(Collectors.toList());

        model.addAttribute("tracks", tracksWithStationNames);
        model.addAttribute("showNewTrackForm", showNewTrackForm);
        model.addAttribute("trackErrors", trackErrors);
        if (trackErrors.isEmpty()) {
            model.addAttribute("newTrack", new RailwayTrackDto());
        }

        final List<StationTableRow> stationTableRows = draftDto
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

                    if (model.containsAttribute("updatedStationTableRow")) {
                        final var updatedStationTableRow = (StationTableRow) model
                            .getAttribute("updatedStationTableRow");
                        row.stationName = updatedStationTableRow.getStationName();
                        row.latitude = updatedStationTableRow.getLatitude();
                        row.longitude = updatedStationTableRow.getLongitude();
                    }
                }

                return row;
            })
            .collect(Collectors.toList());

        model.addAttribute("stationTableRows", stationTableRows);
        model.addAttribute("showNewStationForm", showNewStationForm);

        final var newStationTableRow = new StationTableRow();
        newStationTableRow.showInputField = true;
        newStationTableRow.disabled = (stationIdToEdit != null);
        if (stationIdToEdit == null && stationErrors != null) {
            newStationTableRow.stationNameErrors = stationErrors.getOrDefault("Station name", List.of());
            newStationTableRow.latitudeErrors = stationErrors.getOrDefault("Latitude", List.of());
            newStationTableRow.longitudeErrors = stationErrors.getOrDefault("Longitude", List.of());
        }

        model.addAttribute("newStationTableRow", newStationTableRow);
    }
}
