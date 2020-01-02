package com.ralfhenze.railplan.userinterface.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GermanySvgViewFragment {

    private static final double MAP_WIDTH = 200;
    private static final double MAP_HEIGHT = 400;
    private static final double EARTH_RADIUS = 6378137.0;
    private static final double ZOOM = 5.0;
    private static final double MAX_LATITUDE = 85.0511287798;
    private static final double PI = 3.1415926535898;
    private static final double SCALE = 256.0 * Math.pow(2, ZOOM) / EARTH_RADIUS;

    private final List<Double> center = getPointFromLatLng(50.0, 10.5, SCALE);
    private final List<TrainStationDto> stationDtos;
    private final List<RailwayTrackDto> trackDtos;

    public GermanySvgViewFragment(
        final List<TrainStationDto> stationDtos,
        final List<RailwayTrackDto> trackDtos
    ) {
        this.stationDtos = stationDtos;
        this.trackDtos = trackDtos;
    }

    public void addRequiredAttributesTo(final Model model) {
        model.addAttribute("germanyWidth", MAP_WIDTH);
        model.addAttribute("germanyHeight", MAP_HEIGHT);
        model.addAttribute("germanySvgPath", getPath());
        model.addAttribute("germanySvgStations", getStationCoordinates(stationDtos));
        model.addAttribute("germanySvgTracks", getTrackCoordinates(stationDtos, trackDtos));
    }

    private String getPath() {
        final var mapper = new ObjectMapper();
        final var inputStream = TypeReference.class
            .getResourceAsStream("/json/germany_border_geo_path.json");

        final var geoTypeReference = new TypeReference<List<List<Double>>>(){};
        try {
            final var geoPoints = mapper.readValue(inputStream, geoTypeReference);

            final var pixelCoordinates = geoPoints.stream()
                .map(point -> getPixelCoordinates(point.get(1), point.get(0)))
                .collect(Collectors.toList());

            return pixelCoordinates.stream()
                .map(pixel -> "L" + pixel.get(0) + " " + pixel.get(1))
                .collect(Collectors.joining(" "))
                .replaceFirst("L", "M");

        } catch (IOException exception) {
            return "Couldn't read /json/germany_border_geo_path.json";
        }
    }

    private List<List<Long>> getStationCoordinates(final List<TrainStationDto> stationDtos) {
        return stationDtos.stream()
            .map(stationDto ->
                getPixelCoordinates(stationDto.getLatitude(), stationDto.getLongitude())
            )
            .collect(Collectors.toList());
    }

    private List<List<Long>> getTrackCoordinates(
        final List<TrainStationDto> stationDtos,
        final List<RailwayTrackDto> trackDtos
    ) {
        final Map<Integer, List<Long>> stationPixelCoordinates = stationDtos.stream()
            .collect(Collectors.toMap(
                TrainStationDto::getId,
                station -> getPixelCoordinates(station.getLatitude(), station.getLongitude())
            ));

        return trackDtos.stream()
            .map(trackDto -> {
                final var station1 = stationPixelCoordinates.get(trackDto.getFirstStationId());
                final var station2 = stationPixelCoordinates.get(trackDto.getSecondStationId());

                return List.of(station1.get(0), station1.get(1), station2.get(0), station2.get(1));
            })
            .collect(Collectors.toList());
    }

    private List<Long> getPixelCoordinates(final double lat, final double lng) {
        final var pixel = getPointFromLatLng(lat, lng, SCALE);
        return List.of(
            Math.round(pixel.get(0) - center.get(0) + MAP_WIDTH / 2.0),
            Math.round(pixel.get(1) - center.get(1) + MAP_HEIGHT / 2.0)
        );
    }

    private List<Double> getPointFromLatLng(double lat, final double lng, final double scale) {
        // Spherical Mercator map projection
        // inspired by Leaflet JavaScript code (functions project, _transform, scale)
        double clippedLat = Math.max(Math.min(MAX_LATITUDE, lat), -MAX_LATITUDE);
        double x = Math.toRadians(lng) * EARTH_RADIUS;
        double y = Math.toRadians(clippedLat);
        y = Math.log(Math.tan((PI / 4.0) + (y / 2.0))) * EARTH_RADIUS;

        x = scale * (0.5 / PI * x + 0.5);
        y = scale * (-0.5 / PI * y + 0.5);

        return List.of(x, y);
    }
}
