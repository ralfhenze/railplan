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
import j2html.tags.Tag;

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
import static j2html.TagCreator.iff;
import static j2html.TagCreator.input;
import static j2html.TagCreator.li;
import static j2html.TagCreator.option;
import static j2html.TagCreator.p;
import static j2html.TagCreator.select;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tag;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.tr;
import static j2html.TagCreator.ul;

/**
 * An HTML view that renders a table of Tracks and corresponding forms.
 */
public class TracksView {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private boolean showCustomTrackForm = false;
    private boolean showPresetTrackForm = false;
    private ValidationException validationException;
    private RailwayTrackDto track;

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

    private static class PresetTrack {
        public String text;
        public String value;
    }

    public TracksView(
        final String currentDraftId,
        final RailNetworkDraftRepository draftRepository
    ) {
        this.currentDraftId = currentDraftId;
        this.draftRepository = draftRepository;
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

    public TracksView withTrack(final RailwayTrackDto track) {
        this.track = track;
        return this;
    }

    public String getHtml() {
        final var draftId = currentDraftId;
        final var draftDto = getDraftDto();
        final var germanyMapSvg = new GermanySvgViewFragment(draftDto.getStations(), draftDto.getTracks());
        final var tabsView = new NetworkElementTabsView();
        final var stationNames = getStationNames(draftDto);
        final var trackRows = getTrackRows(draftDto, stationNames);
        var track = this.track;
        if (track == null) {
            track = new RailwayTrackDto();
        }

        var errors = new TrackErrors();
        if (validationException != null) {
            errors = getTrackErrors();
        }

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
                                    iff(!(showCustomTrackForm || showPresetTrackForm),
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
                                    ),
                                    getCustomTrackForm(track, errors, stationNames)
                                )
                            )
                        ),
                        getPresetTrackForm()
                    )
                )
            ),
            germanyMapSvg.getDivTag()
        );
    }

    private Tag getCustomTrackForm(
        final RailwayTrackDto track,
        final TrackErrors errors,
        final Map<Integer, String> stationNames
    ) {
        if (showCustomTrackForm) {
            return tr(
                td("+"),
                td(
                    getTrackSelector(
                        "firstStationId",
                        String.valueOf(track.getFirstStationId()),
                        errors.firstStationErrors,
                        stationNames
                    )
                ),
                td("<=>"),
                td(
                    getTrackSelector(
                        "secondStationId",
                        String.valueOf(track.getSecondStationId()),
                        errors.secondStationErrors,
                        stationNames
                    )
                ),
                td(
                    input().withClass("add-button").withType("submit").withValue("Add Track"),
                    a().withHref("/drafts/" + currentDraftId + "/tracks").withText("Cancel")
                )
            );
        }

        return null;
    }

    private Tag getTrackSelector(
        final String name,
        final String value,
        final List<String> errors,
        final Map<Integer, String> stationNames
    ) {
        return tag(null).with(
            select().withName(name).withValue(value).with(
                each(stationNames, (stationId, stationName) ->
                    option()
                        .condAttr(value.equals(String.valueOf(stationId)), "selected", "selected")
                        .withValue(String.valueOf(stationId))
                        .withText(stationName)
                )
            ),
            iff(!errors.isEmpty(),
                ul().withClasses("errors", name).with(
                    each(errors, error -> li().withText(error))
                )
            )
        );
    }

    private Tag getPresetTrackForm() {
        if (showPresetTrackForm) {
            final var allPresetTracks = getAllPresetTracks();

            return form().withId("preset-track-form").withMethod("post").with(
                select().withName("presetTrackIdsToAdd")
                    .attr("multiple", "multiple")
                    .attr("size", 10)
                    .with(
                        each(allPresetTracks, presetTrack ->
                            option().withValue(presetTrack.value).withText(presetTrack.text)
                        )
                    ),
                p(
                    input().withClass("add-button").withType("submit").withValue("Add Tracks"),
                    a().withHref("/drafts/" + currentDraftId + "/tracks").withText("Cancel")
                )
            );
        }

        return null;
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

    private List<PresetTrack> getAllPresetTracks() {
        final var presetTracks = new PresetTracks().getAllPresetTracks();

        return IntStream
            .range(0, presetTracks.size())
            .mapToObj(i -> {
                final var track = new PresetTrack();
                track.value = String.valueOf(i);
                track.text = presetTracks.get(i).station1.name + " <=> " + presetTracks.get(i).station2.name;
                return track;
            })
            .collect(Collectors.toList());
    }
}
