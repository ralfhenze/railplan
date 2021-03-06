package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import org.assertj.core.api.Condition;
import org.junit.Test;

import static com.ralfhenze.railplan.domain.TestData.BERLIN_OST_LAT;
import static com.ralfhenze.railplan.domain.TestData.BERLIN_OST_LNG;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.BERLIN_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.FRANKFURT_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.HAMBURG_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.STUTTGART_HBF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class RailNetworkUT {

    private final Condition<Throwable> oneStationNameError = getErrorCondition(Field.STATION_NAME, 1);
    private final Condition<Throwable> oneStationIdError = getErrorCondition(Field.STATION_ID, 1);
    private final Condition<Throwable> oneLocationError = getErrorCondition(Field.LOCATION, 1);
    private final Condition<Throwable> oneLatitudeError = getErrorCondition(Field.LATITUDE, 1);
    private final Condition<Throwable> oneLongitudeError = getErrorCondition(Field.LONGITUDE, 1);
    private final Condition<Throwable> oneTracksError = getErrorCondition(Field.TRACKS, 1);

    @Test
    public void keepsStationOrderWhenUpdatingStation() {
        // Given a Network with "Berlin Hbf", "Frankfurt Hbf" and "Stuttgart Hbf"
        final var network = new RailNetwork()
            .addStations(BERLIN_HBF, FRANKFURT_HBF, STUTTGART_HBF);

        // When we update "Frankfurt Hbf" to "Hamburg Hbf"
        final var updatedNetwork = network.updateStation(
            2,
            HAMBURG_HBF.getName(),
            HAMBURG_HBF.getLatitude(),
            HAMBURG_HBF.getLongitude()
        );

        // Then the order of the Stations did not change
        final var stationNames = updatedNetwork.getStations().collect(s -> s.getName().getName());
        assertThat(stationNames).containsExactly(
            BERLIN_HBF.getName(),
            HAMBURG_HBF.getName(),
            STUTTGART_HBF.getName()
        );
    }

    @Test
    public void throwsNoErrorsWhenUpdatingStationWithTheSameData() {
        // Given a Network with "Berlin Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF);

        // When we update "Berlin Hbf" with the current "Berlin Hbf" data
        final var ex = catchThrowable(() ->
            network.updateStation(
                1,
                BERLIN_HBF.getName(),
                BERLIN_HBF.getLatitude(),
                BERLIN_HBF.getLongitude()
            )
        );

        // Then no exception is thrown
        assertThat(ex).doesNotThrowAnyException();
    }

    @Test
    public void throwsValidationErrorWhenAddingNewStationWithExistingId() {
        // Given a Network with "Berlin Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF);

        // When we try to add a new Station with an already existing Station ID
        final var ex = catchThrowable(() ->
            network.addStation(
                1,
                HAMBURG_HBF.getName(),
                HAMBURG_HBF.getLatitude(),
                HAMBURG_HBF.getLongitude()
            )
        );

        // Then we get a Station ID validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneStationIdError);
    }

    @Test
    public void accumulatesValidationErrorsWhenAddingNewStation() {
        // Given a Network with "Berlin Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF);

        // When we try to add a new "Berlin Hbf" Station with invalid coordinates
        final var ex = catchThrowable(() ->
            network.addStation(BERLIN_HBF.getName(), 0, 0)
        );

        // Then we get a Station name, a Latitude and a Longitude validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneStationNameError)
            .has(oneLatitudeError)
            .has(oneLongitudeError);
    }

    @Test
    public void accumulatesValidationErrorsWhenAddingNewStationWithTooNearCoordinates() {
        // Given a Network with "Berlin Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF);

        // When we try to add a new "Berlin Hbf" Station with too near / equal coordinates
        final var ex = catchThrowable(() ->
            network.addStation(
                BERLIN_HBF.getName(),
                BERLIN_HBF.getLatitude(),
                BERLIN_HBF.getLongitude()
            )
        );

        // Then we get a Station name and a Location validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneStationNameError) // Name "Berlin Hbf" already exists
            .has(oneLocationError); // Too near to existing "Berlin Hbf"
    }

    @Test
    public void accumulatesValidationErrorsWhenUpdatingStation() {
        // Given a Network with "Berlin Hbf" and "Hamburg Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF, HAMBURG_HBF);

        // When we try to rename "Hamburg Hbf" to "Berlin Hbf" with invalid coordinates
        final var ex = catchThrowable(() ->
            network.updateStation(2, BERLIN_HBF.getName(), 0, 0)
        );

        // Then we get a Station name, a Latitude and a Longitude validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneStationNameError)
            .has(oneLatitudeError)
            .has(oneLongitudeError);
    }

    @Test
    public void accumulatesValidationErrorsWhenUpdatingStationWithTooNearCoordinates() {
        // Given a Network with "Berlin Hbf" and "Hamburg Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF, HAMBURG_HBF);

        // When we try to update "Hamburg Hbf" to "Berlin Hbf" with "Berlin Hbf" coordinates
        final var ex = catchThrowable(() ->
            network.updateStation(
                2,
                BERLIN_HBF.getName(),
                BERLIN_HBF.getLatitude(),
                BERLIN_HBF.getLongitude()
            )
        );

        // Then we get a Station name and a Location validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneStationNameError) // Name "Berlin Hbf" already exists
            .has(oneLocationError); // Too near to existing "Berlin Hbf"
    }

    @Test
    public void ensuresUniqueStationNamesWhenRenaming() {
        // Given a Network with "Berlin Hbf" and "Hamburg Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF, HAMBURG_HBF);

        // When we rename "Hamburg Hbf" to "Berlin Hbf" (which already exists)
        final var ex = catchThrowable(() ->
            network.updateStation(
                2,
                BERLIN_HBF.getName(),
                HAMBURG_HBF.getLatitude(),
                HAMBURG_HBF.getLongitude()
            )
        );

        // Then we get a Station name validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneStationNameError);
    }

    @Test
    public void ensuresMinimumStationDistanceWhenRelocating() {
        // Given a Network with "Berlin Hbf" and "Hamburg Hbf"
        final var network = new RailNetwork().addStations(BERLIN_HBF, HAMBURG_HBF);

        // When we move "Hamburg Hbf" to "Berlin Ost" (which is too close to "Berlin Hbf")
        final var ex = catchThrowable(() ->
            network.updateStation(
                2,
                HAMBURG_HBF.getName(),
                BERLIN_OST_LAT,
                BERLIN_OST_LNG
            )
        );

        // Then we get a Location validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneLocationError);
    }

    @Test
    public void ensuresNoDuplicateTracks() {
        // Given a Network with a Track "Berlin Hbf" <=> "Hamburg Hbf"
        final var network = new RailNetwork()
            .addStations(BERLIN_HBF, HAMBURG_HBF)
            .addTrackBetween(BERLIN_HBF, HAMBURG_HBF);

        // When we add another Track "Hamburg Hbf" <=> "Berlin Hbf"
        final var ex = catchThrowable(() ->
            network.addTrackBetween(HAMBURG_HBF, BERLIN_HBF)
        );

        // Then we get a Tracks validation error
        assertThat(ex)
            .isInstanceOf(ValidationException.class)
            .has(oneTracksError);
    }

    @Test
    public void deletesAssociatedTracksWhenStationIsDeleted() {
        // Given a Network with a Track "Berlin Hbf" <=> "Hamburg Hbf"
        final var network = new RailNetwork()
            .addStations(BERLIN_HBF, HAMBURG_HBF)
            .addTrackBetween(BERLIN_HBF, HAMBURG_HBF);

        // When we delete the Station "Hamburg Hbf"
        final var updatedNetwork = network.deleteStation(HAMBURG_HBF.getName());

        // Then the Station was deleted
        assertThat(updatedNetwork.getStations()).hasSize(1);

        // And the associated Track was deleted
        assertThat(updatedNetwork.getTracks()).isEmpty();
    }

    @Test
    public void providesPossibilityToDeleteTracks() {
        // Given a Network with a Track "Berlin Hbf" <=> "Hamburg Hbf"
        final var network = new RailNetwork()
            .addStations(BERLIN_HBF, HAMBURG_HBF)
            .addTrackBetween(BERLIN_HBF, HAMBURG_HBF);

        // When we delete the Track
        final var updatedNetwork = network.deleteTrackBetween(
            BERLIN_HBF.getName(),
            HAMBURG_HBF.getName()
        );

        // Then the Network contains no Tracks anymore
        assertThat(updatedNetwork.getTracks()).isEmpty();
    }

    @Test
    public void throwsExceptionWhenDeletingNonExistentTrack() {
        // Given a Network with "Berlin Hbf" <=> "Hamburg Hbf" and "Frankfurt Hbf"
        final var network = new RailNetwork()
            .addStations(BERLIN_HBF, HAMBURG_HBF, FRANKFURT_HBF)
            .addTrackBetween(BERLIN_HBF, HAMBURG_HBF);

        // When we try to delete a non-existent Track "Hamburg Hbf" <=> "Frankfurt Hbf"
        final var ex = catchThrowable(() ->
            network.deleteTrackBetween(HAMBURG_HBF.getName(), FRANKFURT_HBF.getName())
        );

        // Then we get an EntityNotFound error
        assertThat(ex).isInstanceOf(EntityNotFoundException.class);
    }

    private Condition<Throwable> getErrorCondition(final Field field, final long count) {
        return new Condition<>(
            (ex) -> ((ValidationException)ex)
                .getValidationErrors()
                .stream()
                .filter(e -> e.getField().equals(field))
                .count() == count
            ,
            count + " " + field.toString() + " ValidationError"
        );
    }
}
