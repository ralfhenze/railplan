package com.ralfhenze.railplan.userinterface.web.controllers;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.userinterface.web.DraftsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TracksController {

    private final Queries queries;
    private final RailNetworkDraftRepository draftRepository;
    private final AddRailwayTrackCommand addRailwayTrackCommand;
    private final DeleteRailwayTrackCommand deleteRailwayTrackCommand;

    @Autowired
    public TracksController(
        final Queries queries,
        final RailNetworkDraftRepository draftRepository,
        final AddRailwayTrackCommand addRailwayTrackCommand,
        final DeleteRailwayTrackCommand deleteRailwayTrackCommand
    ) {
        this.queries = queries;
        this.draftRepository = draftRepository;
        this.addRailwayTrackCommand = addRailwayTrackCommand;
        this.deleteRailwayTrackCommand = deleteRailwayTrackCommand;
    }

    /**
     * Provides a Draft page with a list of Tracks.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks")
    public String showTracks(@PathVariable String currentDraftId, Model model) {
        new DraftsView(currentDraftId, draftRepository, queries)
            .withShowTracksTab(true)
            .addRequiredAttributesTo(model);

        return "tracks";
    }

    /**
     * Shows a form to create a new Track.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks/new")
    public String showNewTrackForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        new DraftsView(currentDraftId, draftRepository, queries)
            .withShowTracksTab(true)
            .withShowNewTrackForm(true)
            .addRequiredAttributesTo(model);

        return "tracks";
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
            new DraftsView(currentDraftId, draftRepository, queries)
                .withShowTracksTab(true)
                .withShowNewTrackForm(true)
                .withTrackErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model);

            return "tracks";
        }

        return "redirect:/drafts/{currentDraftId}/tracks";
    }

    /**
     * Deletes an existing Track and redirects to Tracks page.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks/{firstStationId}/{secondStationId}/delete")
    public String deleteTrack(
        @PathVariable String currentDraftId,
        @PathVariable String firstStationId,
        @PathVariable String secondStationId
    ) {
        deleteRailwayTrackCommand
            .deleteRailwayTrack(currentDraftId, firstStationId, secondStationId);

        return "redirect:/drafts/{currentDraftId}/tracks";
    }
}
