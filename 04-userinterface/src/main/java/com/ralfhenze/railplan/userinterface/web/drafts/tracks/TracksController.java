package com.ralfhenze.railplan.userinterface.web.drafts.tracks;

import com.ralfhenze.railplan.application.RailwayTrackService;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationNameCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TracksController {

    private final RailNetworkDraftRepository draftRepository;
    private final RailwayTrackService railwayTrackService;

    @Autowired
    public TracksController(
        final RailNetworkDraftRepository draftRepository,
        final RailwayTrackService railwayTrackService
    ) {
        this.draftRepository = draftRepository;
        this.railwayTrackService = railwayTrackService;
    }

    /**
     * Shows a list of Tracks.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks")
    @ResponseBody
    public String showTracks(@PathVariable String currentDraftId) {
        return new TracksView(currentDraftId, draftRepository).getHtml();
    }

    /**
     * Shows a form to create new Tracks from presets.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks/new-from-preset")
    public String showPresetTrackForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        return new TracksView(currentDraftId, draftRepository)
            .withShowPresetTrackForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Creates new Tracks from presets or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/tracks/new-from-preset")
    public String createNewTracksFromPresets(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "trackIds") PresetTrackFormModel trackIds,
        Model model
    ) {
        for (final var trackId : trackIds.getPresetTrackIdsToAdd()) {
            final var track = new PresetTracks().getTrackOfId(trackId);

            if (track.isPresent()) {
                railwayTrackService.addTrackByStationName(
                    new AddRailwayTrackByStationNameCommand(
                        currentDraftId,
                        track.get().station1.name,
                        track.get().station2.name
                    )
                );
            } else {
                throw new EntityNotFoundException("Preset Track " + trackId + " does not exist");
            }
        }

        return "redirect:/drafts/{currentDraftId}/tracks";
    }

    /**
     * Shows a form to create a new custom Track.
     */
    @GetMapping("/drafts/{currentDraftId}/tracks/new-custom")
    public String showCustomTrackForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        return new TracksView(currentDraftId, draftRepository)
            .withShowCustomTrackForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Creates a new custom Track or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/tracks/new-custom")
    public String createNewCustomTrack(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "newTrack") RailwayTrackDto trackDto,
        Model model
    ) {
        try {
            railwayTrackService.addTrackByStationId(
                new AddRailwayTrackByStationIdCommand(
                    currentDraftId,
                    String.valueOf(trackDto.getFirstStationId()),
                    String.valueOf(trackDto.getSecondStationId())
                )
            );
        } catch (ValidationException exception){
            return new TracksView(currentDraftId, draftRepository)
                .withShowCustomTrackForm(true)
                .withValidationException(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
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
        railwayTrackService.deleteTrackFromDraft(
            new DeleteRailwayTrackCommand(currentDraftId, firstStationId, secondStationId)
        );

        return "redirect:/drafts/{currentDraftId}/tracks";
    }
}
