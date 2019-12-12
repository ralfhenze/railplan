package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DraftsController {

    private final Queries queries;
    private final RailNetworkDraftRepository railNetworkDraftRepository;
    private final AddRailNetworkDraftCommand addRailNetworkDraftCommand;
    private final AddRailwayTrackCommand addRailwayTrackCommand;
    private final AddTrainStationCommand addTrainStationCommand;
    private final DeleteRailNetworkDraftCommand deleteRailNetworkDraftCommand;
    private final DeleteRailwayTrackCommand deleteRailwayTrackCommand;
    private final DeleteTrainStationCommand deleteTrainStationCommand;
    private final UpdateTrainStationCommand updateTrainStationCommand;

    @Autowired
    public DraftsController(
        final Queries queries,
        final RailNetworkDraftRepository railNetworkDraftRepository,
        final AddRailNetworkDraftCommand addRailNetworkDraftCommand,
        final AddRailwayTrackCommand addRailwayTrackCommand,
        final AddTrainStationCommand addTrainStationCommand,
        final DeleteRailNetworkDraftCommand deleteRailNetworkDraftCommand,
        final DeleteRailwayTrackCommand deleteRailwayTrackCommand,
        final DeleteTrainStationCommand deleteTrainStationCommand,
        final UpdateTrainStationCommand updateTrainStationCommand
    ) {
        this.queries = queries;
        this.railNetworkDraftRepository = railNetworkDraftRepository;
        this.addRailNetworkDraftCommand = addRailNetworkDraftCommand;
        this.addRailwayTrackCommand = addRailwayTrackCommand;
        this.addTrainStationCommand = addTrainStationCommand;
        this.deleteRailNetworkDraftCommand = deleteRailNetworkDraftCommand;
        this.deleteRailwayTrackCommand = deleteRailwayTrackCommand;
        this.deleteTrainStationCommand = deleteTrainStationCommand;
        this.updateTrainStationCommand = updateTrainStationCommand;
    }

    /**
     * Provides a list of all Drafts.
     */
    @GetMapping("/drafts")
    public String drafts(Model model) {
        model.addAttribute("draftIds", queries.getAllDraftIds());

        return "drafts";
    }

    /**
     * Creates a new Draft and redirects to it.
     */
    @GetMapping("/drafts/new")
    public String addDraft() {
        final var draftId = addRailNetworkDraftCommand
            .addRailNetworkDraft().get()
            .getId().get().toString();

        return "redirect:/drafts/" + draftId;
    }

    /**
     * Provides a Draft page with a list of Stations and Tracks.
     */
    @GetMapping("/drafts/{currentDraftId}")
    public String draft(@PathVariable String currentDraftId, Model model) {
        setModelAttributes(currentDraftId, model, null, null, false, false);

        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    /**
     * Deletes an existing Draft and redirects to /drafts.
     */
    @GetMapping("/drafts/{currentDraftId}/delete")
    public String deleteDraft(@PathVariable String currentDraftId) {
        deleteRailNetworkDraftCommand
            .deleteRailNetworkDraft(currentDraftId);

        return "redirect:/drafts";
    }

    /**
     * Shows a form to create a new Station.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/new")
    public String showNewStationForm(@PathVariable String currentDraftId, Model model) {
        setModelAttributes(currentDraftId, model, null, null, true, false);

        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    /**
     * Creates a new Station or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/stations/new")
    public String createNewStation(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "newStationTableRow") StationTableRow stationRow,
        Model model
    ) {
        try {
            addTrainStationCommand.addTrainStation(
                currentDraftId,
                stationRow.stationName,
                Double.parseDouble(stationRow.latitude),
                Double.parseDouble(stationRow.longitude)
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model, null, exception.getErrorMessagesAsHashMap(), true, false);
            model.addAttribute("newTrack", new RailwayTrackDto());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    /**
     * Shows a form to edit an existing Station.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/{stationId}/edit")
    public String editStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId,
        Model model
    ) {
        setModelAttributes(currentDraftId, model, stationId, null, false, false);

        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    /**
     * Updates an existing Station or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/stations/{stationId}/edit")
    public String updateStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId,
        @ModelAttribute(name = "updatedStationTableRow") StationTableRow stationRow,
        Model model
    ) {
        try {
            updateTrainStationCommand.updateTrainStation(
                currentDraftId,
                stationId,
                stationRow.stationName,
                Double.parseDouble(stationRow.latitude),
                Double.parseDouble(stationRow.longitude)
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model, stationId, exception.getErrorMessagesAsHashMap(), false, false);
            model.addAttribute("newTrack", new RailwayTrackDto());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    /**
     * Deletes an existing Station and redirects to Draft page.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/{stationId}/delete")
    public String deleteStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId
    ) {
        deleteTrainStationCommand
            .deleteTrainStation(currentDraftId, stationId);

        return "redirect:/drafts/{currentDraftId}";
    }

    /**
     * Shows a form to create a new Track.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks/new")
    public String showNewTrackForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        setModelAttributes(currentDraftId, model, null, null, false, true);
        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    @PostMapping("/drafts/{currentDraftId}/tracks/new")
    public String createNewTrack(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "newTrack") RailwayTrackDto trackDto,
        Model model
    ) {
        try {
            addRailwayTrackCommand.addRailwayTrack(
                currentDraftId,
                String.valueOf(trackDto.getFirstStationId()),
                String.valueOf(trackDto.getSecondStationId())
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model, null, null, false, true);
            model.addAttribute("trackErrors", exception.getErrorMessagesAsHashMap());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    /**
     * Deletes an existing Track and redirects to Draft page.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks/{firstStationId}/{secondStationId}/delete")
    public String deleteStation(
        @PathVariable String currentDraftId,
        @PathVariable String firstStationId,
        @PathVariable String secondStationId
    ) {
        deleteRailwayTrackCommand
            .deleteRailwayTrack(currentDraftId, firstStationId, secondStationId);

        return "redirect:/drafts/{currentDraftId}";
    }

    private void setModelAttributes(
        final String currentDraftId,
        final Model model,
        final String stationIdToEdit,
        final Map<String, List<String>> errorMessages,
        final boolean showNewStationForm,
        final boolean showNewTrackForm
    ) {
        model.addAttribute("draftIds", queries.getAllDraftIds());

        final var draft = railNetworkDraftRepository
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
        model.addAttribute("trackErrors", new HashMap<String, List<String>>());
        model.addAttribute("showNewTrackForm", showNewTrackForm);

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
                    if (errorMessages != null) {
                        row.stationNameErrors = errorMessages.getOrDefault("Station name", List.of());
                        row.latitudeErrors = errorMessages.getOrDefault("Latitude", List.of());
                        row.longitudeErrors = errorMessages.getOrDefault("Longitude", List.of());
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
        if (stationIdToEdit == null && errorMessages != null) {
            newStationTableRow.stationNameErrors = errorMessages.getOrDefault("Station name", List.of());
            newStationTableRow.latitudeErrors = errorMessages.getOrDefault("Latitude", List.of());
            newStationTableRow.longitudeErrors = errorMessages.getOrDefault("Longitude", List.of());
        }
        model.addAttribute("newStationTableRow", newStationTableRow);
    }
}
