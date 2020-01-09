package com.ralfhenze.railplan.userinterface.web.drafts.tracks;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import com.ralfhenze.railplan.userinterface.web.GermanySvgViewFragment;
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
    private boolean showCustomTrackForm = false;
    private boolean showPresetTrackForm = false;
    private ValidationException validationException;

    public static class TrackErrors {
        public List<String> firstStationErrors = List.of();
        public List<String> secondStationErrors = List.of();
    }

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

    public TracksView withShowCustomTrackForm(final boolean showCustomTrackForm) {
        this.showCustomTrackForm = showCustomTrackForm;
        return this;
    }

    public TracksView withShowPresetTrackForm(final boolean showPresetTrackForm) {
        this.showPresetTrackForm = showPresetTrackForm;
        return this;
    }

    public TracksView withValidationException(final ValidationException validationException) {
        this.validationException = validationException;
        return this;
    }

    public TracksView addRequiredAttributesTo(final Model model) {
        final var draftDto = getDraftDto();
        final var stationNames = getStationNames(draftDto);

        model.addAttribute("stationNames", stationNames);
        model.addAttribute("tracks", getTracksWithStationNames(draftDto, stationNames));
        model.addAttribute("showCustomTrackForm", showCustomTrackForm);

        if (validationException == null) {
            model.addAttribute("newTrack", new RailwayTrackDto());
            model.addAttribute("trackErrors", new TrackErrors());
        } else {
            model.addAttribute("trackErrors", getTrackErrors());
        }

        model.addAttribute("showPresetTrackForm", showPresetTrackForm);
        model.addAttribute("presetTrackFormModel", new PresetTrackFormModel());

        final var presetTracks = new PresetTracks().getAllPresetTracks();
        model.addAttribute("allPresetTracks", IntStream
            .range(0, presetTracks.size())
            .mapToObj(i -> Map.of(
                "value", i,
                "text", presetTracks.get(i).station1.name + " <=> " + presetTracks.get(i).station2.name
            ))
            .collect(Collectors.toList()));

        new GermanySvgViewFragment(draftDto.getStations(), draftDto.getTracks())
            .addRequiredAttributesTo(model);

        return this;
    }

    private TrackErrors getTrackErrors() {
        final var trackErrors = new TrackErrors();
        trackErrors.firstStationErrors = validationException.getErrorsOfField(Field.FIRST_STATION_ID);
        trackErrors.secondStationErrors = validationException.getErrorsOfField(Field.SECOND_STATION_ID);

        return trackErrors;
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
