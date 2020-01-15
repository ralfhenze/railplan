package com.ralfhenze.railplan.userinterface.web.networks;

import com.ralfhenze.railplan.infrastructure.persistence.dto.ReleasedRailNetworkDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import com.ralfhenze.railplan.userinterface.web.views.MasterView;
import com.ralfhenze.railplan.userinterface.web.views.MasterView.SelectedNavEntry;

import java.util.List;
import java.util.stream.Collectors;

import static j2html.TagCreator.caption;
import static j2html.TagCreator.dd;
import static j2html.TagCreator.div;
import static j2html.TagCreator.dl;
import static j2html.TagCreator.dt;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h3;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.tr;

/**
 * An HTML view for a single Network.
 */
public class NetworkView {

    private static class Track {
        public String firstStationName;
        public String secondStationName;
    }

    public String getHtml(final ReleasedRailNetworkDto networkDto) {
        final var tracks = getTracksWithStationNames(networkDto);

        return new MasterView(SelectedNavEntry.NETWORKS).with(
            div().withClasses("networks", "fullscreen-wrapper").with(
                div().withId("index-box").withClass("box").with(
                    div(
                        h1("Network"),
                        h3("Valid"),
                        dl(
                            dt("From"),
                            dd().withClass("valid-from").withText(networkDto.getStartDate())
                        ),
                        dl(
                            dt("Until"),
                            dd().withClass("valid-until").withText(networkDto.getEndDate())
                        ),
                        table().withId("stations").with(
                            caption(h3("Stations")),
                            thead(
                                tr(
                                    th().attr("scope", "col").withText("Name"),
                                    th().attr("scope", "col").withText("Latitude"),
                                    th().attr("scope", "col").withText("Longitude")
                                )
                            ),
                            tbody(
                                each(networkDto.getStations(), station ->
                                    tr().withClass("station-row").with(
                                        td().withClass("stationName")
                                            .withText(station.getName()),
                                        td().withClass("latitude")
                                            .withText(String.valueOf(station.getLatitude())),
                                        td().withClass("longitude")
                                            .withText(String.valueOf(station.getLongitude()))
                                    )
                                )
                            )
                        ),
                        table().withId("tracks").with(
                            caption(h3("Tracks")),
                            thead(
                                tr(
                                    th().attr("scope", "col").withText("Station 1"),
                                    th().attr("scope", "col"),
                                    th().attr("scope", "col").withText("Station 2")
                                )
                            ),
                            tbody(
                                each(tracks, track ->
                                    tr().withClass("track-row").with(
                                        td().withClass("station-1")
                                            .withText(track.firstStationName),
                                        td().withText("<=>"),
                                        td().withClass("station-2")
                                            .withText(track.secondStationName)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    private List<Track> getTracksWithStationNames(final ReleasedRailNetworkDto networkDto) {
        final var stationNames = networkDto.getStations().stream()
            .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));

        return networkDto.getTracks().stream()
            .map(trackDto -> {
                final var track = new Track();
                track.firstStationName = stationNames.get(trackDto.getFirstStationId());
                track.secondStationName = stationNames.get(trackDto.getSecondStationId());

                return track;
            })
            .collect(Collectors.toList());
    }
}
