package com.ralfhenze.railplan.userinterface.web.drafts.tracks;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import com.ralfhenze.railplan.userinterface.web.views.DefaultView;
import com.ralfhenze.railplan.userinterface.web.views.GermanySvgViewFragment;
import com.ralfhenze.railplan.userinterface.web.views.NetworkElementTabsView;
import org.javatuples.Pair;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static j2html.TagCreator.a;
import static j2html.TagCreator.caption;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h3;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tag;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.tr;

/**
 * Prepares the necessary data for resources/templates/tracks.html.
 */
public class TracksView {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private boolean showCustomTrackForm = false;
    private boolean showPresetTrackForm = false;
    private ValidationException validationException;

    private static class TrackRow {
        public String index;
        public String firstStationId;
        public String firstStationName;
        public String secondStationId;
        public String secondStationName;
    }

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

    private List<TrackRow> getTrackRows(
        final RailNetworkDraftDto draftDto,
        final Map<Integer, String> stationNames
    ) {
        final var tracks = draftDto.getTracks();

        return IntStream
            .range(0, tracks.size())
            .mapToObj(i -> {
                final var trackDto = tracks.get(i);
                final var trackRow = new TrackRow();
                trackRow.index = String.valueOf(i + 1);
                trackRow.firstStationId = String.valueOf(trackDto.getFirstStationId());
                trackRow.firstStationName = stationNames.get(trackDto.getFirstStationId());
                trackRow.secondStationId = String.valueOf(trackDto.getSecondStationId());
                trackRow.secondStationName = stationNames.get(trackDto.getSecondStationId());

                return trackRow;
            })
            .collect(Collectors.toList());
    }


    public String getHtml() {
        final var draftId = currentDraftId;
        final var draftDto = getDraftDto();
        final var germanyMapSvg = new GermanySvgViewFragment(draftDto.getStations(), draftDto.getTracks());
        final var tabsView = new NetworkElementTabsView();
        final var stationNames = getStationNames(draftDto);
        final var trackRows = getTrackRows(draftDto, stationNames);

        return new DefaultView().getHtml(DefaultView.SelectedNavEntry.DRAFTS,
            div().withId("data-panel").with(
                div().withId("network-elements-box").withClass("box").with(
                    tabsView.getTag(draftId, NetworkElementTabsView.SelectedTab.TRACKS),
                    div().withId("tracks").with(
                        form().withId("custom-track-form").withMethod("post").with(
                            table(
                                caption(h3("Railway Tracks")),
                                thead(
                                    tr(
                                        th().attr("scope", "col").withText("#"),
                                        th().attr("scope", "col").withText("Station 1"),
                                        th().attr("scope", "col"),
                                        th().attr("scope", "col").withText("Station 2"),
                                        th().attr("scope", "col").withText("Actions")
                                    )
                                ),
                                tbody(
                                    each(trackRows, row ->
                                        tag(null).with(
                                            tr().withClass("track-row").with(
                                                td(row.index),
                                                td().withClass("station-1").withText(row.firstStationName),
                                                td("<=>"),
                                                td().withClass("station-2").withText(row.secondStationName),
                                                td(
                                                    a().withHref("/drafts/" + draftId + "/tracks/" + row.firstStationId + "/" + row.secondStationId + "/delete")
                                                        .withText("Delete")
                                                )
                                            )
                                        )
                                    ),
                                    tr().withClass("add-button-row").with(
                                        td().attr("colspan", "5").with(
                                            a().withClass("add-button")
                                                .withHref("/drafts/" + draftId + "/tracks/new-from-preset")
                                                .withText("+ Add Preset Tracks"),
                                            a().withClass("add-button")
                                                .withHref("/drafts/" + draftId + "/tracks/new-custom")
                                                .withText("+ Add Custom Track")
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            germanyMapSvg.getDivTag()
        );
    }
}
