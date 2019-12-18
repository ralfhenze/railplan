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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Prepares the necessary data for resources/templates/drafts.html.
 */
public class DraftsView {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private final Queries queries;
    private String stationIdToEdit;
    private boolean showNewTrackForm = false;
    private boolean showNewStationForm = false;
    private boolean showNewDefaultStationsForm = false;
    private boolean showReleaseForm = false;
    private boolean showTracksTab = false;
    private Map<String, List<String>> stationErrors;
    private Map<String, List<String>> trackErrors = Map.of();
    private Map<String, List<String>> releaseErrors = Map.of();

    public DraftsView(
        final String currentDraftId,
        final RailNetworkDraftRepository draftRepository,
        final Queries queries
    ) {
        this.currentDraftId = currentDraftId;
        this.queries = queries;
        this.draftRepository = draftRepository;
    }

    public String getViewName() {
        return "drafts";
    }

    public DraftsView withStationIdToEdit(final String stationIdToEdit) {
        this.stationIdToEdit = stationIdToEdit;
        return this;
    }

    public DraftsView withShowNewStationForm(final boolean showNewStationForm) {
        this.showNewStationForm = showNewStationForm;
        return this;
    }

    public DraftsView withShowNewDefaultStationsForm(final boolean showNewDefaultStationsForm) {
        this.showNewDefaultStationsForm = showNewDefaultStationsForm;
        return this;
    }

    public DraftsView withShowNewTrackForm(final boolean showNewTrackForm) {
        this.showNewTrackForm = showNewTrackForm;
        return this;
    }

    public DraftsView withShowReleaseForm(final boolean showReleaseForm) {
        this.showReleaseForm = showReleaseForm;
        return this;
    }

    public DraftsView withShowTracksTab(final boolean showTracksTab) {
        this.showTracksTab = showTracksTab;
        return this;
    }

    public DraftsView withStationErrorsProvidedBy(final ValidationException exception) {
        this.stationErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public DraftsView withTrackErrorsProvidedBy(final ValidationException exception) {
        this.trackErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public DraftsView withReleaseErrorsProvidedBy(final ValidationException exception) {
        this.releaseErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public DraftsView addRequiredAttributesTo(final Model model) {
        final var draftDto = getDraftDto();
        final var stationNames = getStationNames(draftDto);

        model.addAttribute("draftIds", queries.getAllDraftIds());
        model.addAttribute("currentDraftDto", draftDto);
        model.addAttribute("stationNames", stationNames);
        model.addAttribute("tracks", getTracksWithStationNames(draftDto, stationNames));
        model.addAttribute("showNewTrackForm", showNewTrackForm);
        model.addAttribute("showTracksTab", showTracksTab);
        model.addAttribute("trackErrors", trackErrors);
        if (trackErrors.isEmpty()) {
            model.addAttribute("newTrack", new RailwayTrackDto());
        }

        model.addAttribute("stationTableRows", getStationTableRows(model, draftDto));
        model.addAttribute("showNewStationForm", showNewStationForm);
        model.addAttribute("newStationTableRow", getNewStationTableRow(model));

        model.addAttribute("showNewDefaultStationsForm", showNewDefaultStationsForm);
        model.addAttribute("defaultStations", new DefaultStations().getStationNames());
        model.addAttribute("stations", new Stations());

        model.addAttribute("showReleaseForm", showReleaseForm);
        model.addAttribute("releaseErrors", releaseErrors);
        if (releaseErrors.isEmpty()) {
            model.addAttribute("validityPeriod", new ValidityPeriodDto(
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(1).plusMonths(1).toString()
            ));
        }

        final var germanySvg = new GermanySvg();
        model.addAttribute("germanyWidth", GermanySvg.MAP_WIDTH);
        model.addAttribute("germanyHeight", GermanySvg.MAP_HEIGHT);
        model.addAttribute("germanySvgPath", germanySvg.getPath());
        model.addAttribute("germanySvgStations", germanySvg.getStationCoordinates(
            draftDto.getStations())
        );
        model.addAttribute("germanySvgTracks", germanySvg.getTrackCoordinates(
            draftDto.getStations(), draftDto.getTracks())
        );

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

    private List<List<Pair<String, String>>> getTracksWithStationNames(
        final RailNetworkDraftDto draftDto,
        final Map<Integer, String> stationNames
    ) {
        return draftDto
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
