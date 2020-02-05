package com.ralfhenze.railplan.userinterface.web.networks.tracks;

import com.ralfhenze.railplan.application.RailwayTrackService;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationNameCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static com.ralfhenze.railplan.userinterface.web.ControllerHelper.redirectTo;

@Controller
public class TracksController {

    private final RailNetworkRepository networkRepository;
    private final RailwayTrackService railwayTrackService;

    @Autowired
    public TracksController(
        final RailNetworkRepository networkRepository,
        final RailwayTrackService railwayTrackService
    ) {
        this.networkRepository = networkRepository;
        this.railwayTrackService = railwayTrackService;
    }

    /**
     * Shows a list of Tracks.
     */
    @GetMapping("/networks/{networkId}/tracks")
    @ResponseBody
    public String showTracks(@PathVariable String networkId) {
        return new TracksView(networkId, networkRepository).getHtml().render();
    }

    /**
     * Shows a form to create new Tracks from presets.
     */
    @GetMapping("/networks/{networkId}/tracks/new-from-preset")
    @ResponseBody
    public String showPresetTrackForm(@PathVariable String networkId) {
        return new TracksView(networkId, networkRepository)
            .withShowPresetTrackForm(true)
            .getHtml()
            .render();
    }

    /**
     * Creates new Tracks from presets or shows validation errors.
     */
    @PostMapping("/networks/{networkId}/tracks/new-from-preset")
    @ResponseBody
    public String createNewTracksFromPresets(
        @PathVariable String networkId,
        PresetTrackFormModel trackIds,
        HttpServletResponse response
    ) {
        for (final var trackId : trackIds.getPresetTrackIdsToAdd()) {
            final var track = new PresetTracks().getTrackOfId(trackId);

            if (track.isPresent()) {
                railwayTrackService.addTrackByStationName(
                    new AddRailwayTrackByStationNameCommand(
                        networkId,
                        track.get().station1.getName(),
                        track.get().station2.getName()
                    )
                );
            } else {
                throw new EntityNotFoundException("Preset Track " + trackId + " does not exist");
            }
        }

        return redirectTo("/networks/" + networkId + "/tracks", response);
    }

    /**
     * Shows a form to create a new custom Track.
     */
    @GetMapping("/networks/{networkId}/tracks/new-custom")
    @ResponseBody
    public String showCustomTrackForm(@PathVariable String networkId) {
        return new TracksView(networkId, networkRepository)
            .withShowCustomTrackForm(true)
            .getHtml()
            .render();
    }

    /**
     * Creates a new custom Track or shows validation errors.
     */
    @PostMapping("/networks/{networkId}/tracks/new-custom")
    @ResponseBody
    public String createNewCustomTrack(
        @PathVariable String networkId,
        RailwayTrackDto trackDto,
        HttpServletResponse response
    ) {
        try {
            railwayTrackService.addTrackByStationId(
                new AddRailwayTrackByStationIdCommand(
                    networkId,
                    trackDto.getFirstStationId(),
                    trackDto.getSecondStationId()
                )
            );
        } catch (ValidationException exception){
            return new TracksView(networkId, networkRepository)
                .withShowCustomTrackForm(true)
                .withValidationException(exception)
                .withTrack(trackDto)
                .getHtml()
                .render();
        }

        return redirectTo("/networks/" + networkId + "/tracks", response);
    }

    /**
     * Deletes an existing Track and redirects to Tracks page.
     */
    @GetMapping("/networks/{networkId}/tracks/{firstStationId}/{secondStationId}/delete")
    public String deleteTrack(
        @PathVariable String networkId,
        @PathVariable String firstStationId,
        @PathVariable String secondStationId
    ) {
        railwayTrackService.deleteTrackFromNetwork(
            new DeleteRailwayTrackCommand(
                networkId,
                Integer.parseInt(firstStationId),
                Integer.parseInt(secondStationId)
            )
        );

        return "redirect:/networks/{networkId}/tracks";
    }
}
