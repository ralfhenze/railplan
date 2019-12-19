package com.ralfhenze.railplan.userinterface.web;

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
import java.util.stream.IntStream;

/**
 * Prepares the necessary data for resources/templates/tracks.html.
 */
public class TracksView {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private boolean showNewTrackForm = false;
    private boolean showNewDefaultTracksForm = false;
    private Map<String, List<String>> trackErrors = Map.of();

    public TracksView(
        final String currentDraftId,
        final RailNetworkDraftRepository draftRepository
    ) {
        this.currentDraftId = currentDraftId;
        this.draftRepository = draftRepository;
    }

    public String getViewName() {
        return "tracks";
    }

    public TracksView withShowNewTrackForm(final boolean showNewTrackForm) {
        this.showNewTrackForm = showNewTrackForm;
        return this;
    }

    public TracksView withShowNewDefaultTracksForm(final boolean showNewDefaultTracksForm) {
        this.showNewDefaultTracksForm = showNewDefaultTracksForm;
        return this;
    }

    public TracksView withTrackErrorsProvidedBy(final ValidationException exception) {
        this.trackErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public TracksView addRequiredAttributesTo(final Model model) {
        final var draftDto = getDraftDto();
        final var stationNames = getStationNames(draftDto);

        model.addAttribute("currentDraftDto", draftDto);
        model.addAttribute("stationNames", stationNames);
        model.addAttribute("tracks", getTracksWithStationNames(draftDto, stationNames));
        model.addAttribute("showNewTrackForm", showNewTrackForm);
        model.addAttribute("trackErrors", trackErrors);
        if (trackErrors.isEmpty()) {
            model.addAttribute("newTrack", new RailwayTrackDto());
        }

        model.addAttribute("showNewDefaultTracksForm", showNewDefaultTracksForm);
        model.addAttribute("trackIds", new TrackIds());

        final var defaultTracks = new DefaultTracks().getTracks();
        model.addAttribute("defaultTracks", IntStream
            .range(0, defaultTracks.size())
            .mapToObj(i -> Map.of(
                "value", i,
                "text", defaultTracks.get(i).station1.name + " <=> " + defaultTracks.get(i).station2.name
            ))
            .collect(Collectors.toList()));

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
}
