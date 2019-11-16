package com.ralfhenze.rms.railnetworkplanning.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReleaseServiceTest {

    @ParameterizedTest
    @CsvSource({
        // invalid start dates for new RailNetwork
        "2019-11-19",
        "2019-11-20", // the only valid date is 2019-11-21
        "2019-11-22",
    })
    void should_ensure_consecutive_periods(LocalDate invalidStartDate) {
        RailNetworkRepository railNetworkRepository = Mockito.mock(RailNetworkRepository.class);
        RailNetwork lastRailNetwork = Mockito.mock(RailNetwork.class);
        Mockito
            .when(lastRailNetwork.getPeriod())
            .thenReturn(
                new RailNetworkPeriod(
                    LocalDate.of(2019, 11, 14),
                    LocalDate.of(2019, 11, 20) // last end date
                )
            );
        Mockito
            .when(railNetworkRepository.getLastReleasedRailNetwork())
            .thenReturn(Optional.of(lastRailNetwork));

        RailNetworkReleaseService releaseService = new RailNetworkReleaseService(railNetworkRepository);

        RailNetworkDraft draft = Mockito.mock(RailNetworkDraft.class);

        assertThrows(IllegalArgumentException.class, () -> {
            releaseService.release(
                draft,
                new RailNetworkPeriod(
                    invalidStartDate, // should be one day after last end date (2019-11-21)
                    LocalDate.of(2019, 12, 1)
                )
            );
        });
    }
}
