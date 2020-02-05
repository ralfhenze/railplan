package com.ralfhenze.railplan.userinterface.web.networks.tracks;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import com.ralfhenze.railplan.userinterface.web.views.GermanyMapSvgView;
import com.ralfhenze.railplan.userinterface.web.views.MasterView;
import com.ralfhenze.railplan.userinterface.web.views.MasterView.SelectedNavEntry;
import com.ralfhenze.railplan.userinterface.web.views.NetworkElementTabsView;
import com.ralfhenze.railplan.userinterface.web.views.NetworkElementTabsView.SelectedTab;
import com.ralfhenze.railplan.userinterface.web.views.View;
import j2html.tags.Tag;

import java.util.ArrayList;
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
public class TracksView implements View {

    private final String networkId;
    private final RailNetworkRepository networkRepository;
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
        public String id;
        public String firstStationName;
        public String secondStationName;
    }

    public TracksView(
        final String networkId,
        final RailNetworkRepository networkRepository
    ) {
        this.networkId = networkId;
        this.networkRepository = networkRepository;
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

    @Override
    public Tag getHtml() {
        final var networkDto = getNetworkDto();
        final var stationNames = getStationNames(networkDto);
        final var trackRows = getTrackRows(networkDto, stationNames);
        var track = this.track;
        if (track == null) {
            track = new RailwayTrackDto();
        }

        var errors = new TrackErrors();
        if (validationException != null) {
            errors = getTrackErrors();
        }

        return new MasterView(SelectedNavEntry.NETWORKS).with(
            div().withId("data-panel").with(
                div().withId("network-elements-box").withClass("box").with(
                    new NetworkElementTabsView(SelectedTab.TRACKS, networkId).getHtml(),
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
                                                    a().withHref("/networks/" + networkId + "/tracks/" + row.firstStationId + "/" + row.secondStationId + "/delete")
                                                        .withText("Delete")
                                                )
                                            )
                                        )
                                    ),
                                    iff(!(showCustomTrackForm || showPresetTrackForm),
                                        tr().withClass("add-button-row").with(
                                            td().attr("colspan", "5").with(
                                                a().withClass("add-button")
                                                    .withHref("/networks/" + networkId + "/tracks/new-from-preset")
                                                    .withText("+ Add Preset Tracks"),
                                                a().withClass("add-button")
                                                    .withHref("/networks/" + networkId + "/tracks/new-custom")
                                                    .withText("+ Add Custom Track")
                                            )
                                        )
                                    ),
                                    getCustomTrackForm(track, errors, stationNames)
                                )
                            )
                        ),
                        getPresetTrackForm(networkDto, trackRows)
                    )
                )
            ),
            new GermanyMapSvgView(networkDto.getStations(), networkDto.getTracks()).getHtml()
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
                    a().withHref("/networks/" + networkId + "/tracks").withText("Cancel")
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

    private Tag getPresetTrackForm(
        final RailNetworkDto networkDto,
        final List<TrackRow> trackRows
    ) {
        if (showPresetTrackForm) {
            final var stationNames = networkDto.getStations().stream()
                .map(TrainStationDto::getName)
                .collect(Collectors.toList());
            final var presetTracks = getAllPresetTracks().stream()
                .filter(t -> stationNames.contains(t.firstStationName) && stationNames.contains(t.secondStationName))
                .filter(t -> trackRows.stream()
                    .filter(tr ->
                        (tr.firstStationName.equals(t.firstStationName) && tr.secondStationName.equals(t.secondStationName))
                        || (tr.firstStationName.equals(t.secondStationName) && tr.secondStationName.equals(t.firstStationName))
                    )
                    .findAny()
                    .isEmpty()
                )
                .collect(Collectors.toList());

            return form().withId("preset-track-form").withMethod("post").with(
                select().withName("presetTrackIdsToAdd")
                    .attr("multiple", "multiple")
                    .attr("size", 10)
                    .with(
                        each(presetTracks, presetTrack ->
                            option().withValue(presetTrack.id).withText(
                                presetTrack.firstStationName + " <=> " + presetTrack.secondStationName
                            )
                        )
                    ),
                p(
                    input().withClass("add-button").withType("submit").withValue("Add Tracks"),
                    a().withHref("/networks/" + networkId + "/tracks").withText("Cancel")
                )
            );
        }

        return null;
    }

    private TrackErrors getTrackErrors() {
        final var trackErrors = new TrackErrors();
        trackErrors.firstStationErrors = new ArrayList<>();
        trackErrors.firstStationErrors.addAll(validationException.getErrorsOfField(Field.FIRST_STATION_ID));
        trackErrors.firstStationErrors.addAll(validationException.getErrorsOfField(Field.TRACKS));
        trackErrors.secondStationErrors = validationException.getErrorsOfField(Field.SECOND_STATION_ID);

        return trackErrors;
    }

    private RailNetworkDto getNetworkDto() {
        final var network = networkRepository
            .getRailNetworkOfId(new RailNetworkId(networkId));

        return new RailNetworkDto(network);
    }

    private Map<Integer, String> getStationNames(final RailNetworkDto networkDto) {
        return networkDto
            .getStations()
            .stream()
            .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));
    }

    private List<TrackRow> getTrackRows(
        final RailNetworkDto networkDto,
        final Map<Integer, String> stationNames
    ) {
        final var tracks = networkDto.getTracks();

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
                track.id = String.valueOf(i);
                track.firstStationName = presetTracks.get(i).station1.getName();
                track.secondStationName = presetTracks.get(i).station2.getName();
                return track;
            })
            .collect(Collectors.toList());
    }
}
