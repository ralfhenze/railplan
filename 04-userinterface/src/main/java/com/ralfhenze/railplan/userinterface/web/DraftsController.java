package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class DraftsController {

    private final Queries queries;
    private final RailNetworkDraftRepository draftRepository;
    private final AddRailNetworkDraftCommand addRailNetworkDraftCommand;
    private final AddRailwayTrackCommand addRailwayTrackCommand;
    private final AddTrainStationCommand addTrainStationCommand;
    private final DeleteRailNetworkDraftCommand deleteRailNetworkDraftCommand;
    private final DeleteRailwayTrackCommand deleteRailwayTrackCommand;
    private final DeleteTrainStationCommand deleteTrainStationCommand;
    private final UpdateTrainStationCommand updateTrainStationCommand;
    private final ReleaseRailNetworkCommand releaseRailNetworkCommand;

    @Autowired
    public DraftsController(
        final Queries queries,
        final RailNetworkDraftRepository draftRepository,
        final AddRailNetworkDraftCommand addRailNetworkDraftCommand,
        final AddRailwayTrackCommand addRailwayTrackCommand,
        final AddTrainStationCommand addTrainStationCommand,
        final DeleteRailNetworkDraftCommand deleteRailNetworkDraftCommand,
        final DeleteRailwayTrackCommand deleteRailwayTrackCommand,
        final DeleteTrainStationCommand deleteTrainStationCommand,
        final UpdateTrainStationCommand updateTrainStationCommand,
        final ReleaseRailNetworkCommand releaseRailNetworkCommand
    ) {
        this.queries = queries;
        this.draftRepository = draftRepository;
        this.addRailNetworkDraftCommand = addRailNetworkDraftCommand;
        this.addRailwayTrackCommand = addRailwayTrackCommand;
        this.addTrainStationCommand = addTrainStationCommand;
        this.deleteRailNetworkDraftCommand = deleteRailNetworkDraftCommand;
        this.deleteRailwayTrackCommand = deleteRailwayTrackCommand;
        this.deleteTrainStationCommand = deleteTrainStationCommand;
        this.updateTrainStationCommand = updateTrainStationCommand;
        this.releaseRailNetworkCommand = releaseRailNetworkCommand;
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
        return new DraftsView(currentDraftId, draftRepository, queries)
            .addRequiredAttributesTo(model)
            .getViewName();
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
        return new DraftsView(currentDraftId, draftRepository, queries)
            .withShowNewStationForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Creates a new Station or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/stations/new")
    public String createNewStation(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "updatedStationTableRow") StationTableRow stationRow,
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
            return new DraftsView(currentDraftId, draftRepository, queries)
                .withShowNewStationForm(true)
                .withStationErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
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
        return new DraftsView(currentDraftId, draftRepository, queries)
            .withStationIdToEdit(stationId)
            .addRequiredAttributesTo(model)
            .getViewName();
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
            return new DraftsView(currentDraftId, draftRepository, queries)
                .withStationIdToEdit(stationId)
                .withStationErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
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
        return new DraftsView(currentDraftId, draftRepository, queries)
            .withShowNewTrackForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Creates a new Track or shows validation errors.
     */
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
            return new DraftsView(currentDraftId, draftRepository, queries)
                .withShowNewTrackForm(true)
                .withTrackErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    /**
     * Deletes an existing Track and redirects to Draft page.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks/{firstStationId}/{secondStationId}/delete")
    public String deleteTrack(
        @PathVariable String currentDraftId,
        @PathVariable String firstStationId,
        @PathVariable String secondStationId
    ) {
        deleteRailwayTrackCommand
            .deleteRailwayTrack(currentDraftId, firstStationId, secondStationId);

        return "redirect:/drafts/{currentDraftId}";
    }

    /**
     * Shows a form to release a Draft.
     */
    @GetMapping("/drafts/{currentDraftId}/release")
    public String showDraftReleaseForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        return new DraftsView(currentDraftId, draftRepository, queries)
            .withShowReleaseForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Releases a Draft or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/release")
    public String releaseDraft(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "validityPeriod") ValidityPeriodDto periodDto,
        Model model
    ) {
        try {
            final var networkId = releaseRailNetworkCommand.releaseRailNetworkDraft(
                currentDraftId,
                LocalDate.parse(periodDto.getStartDate()),
                LocalDate.parse(periodDto.getEndDate())
            );

            return "redirect:/networks/" + networkId;

        } catch (ValidationException exception) {

            return new DraftsView(currentDraftId, draftRepository, queries)
                .withShowReleaseForm(true)
                .withReleaseErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }
    }
}
