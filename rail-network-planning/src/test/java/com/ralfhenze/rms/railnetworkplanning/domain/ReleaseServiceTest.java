package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ValidityPeriod;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.RailNetworkReleaseService;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
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
        ReleasedRailNetworkRepository railNetworkRepository = Mockito.mock(ReleasedRailNetworkRepository.class);
        ReleasedRailNetwork lastRailNetwork = Mockito.mock(ReleasedRailNetwork.class);
        Mockito
            .when(lastRailNetwork.getPeriod())
            .thenReturn(
                new ValidityPeriod(
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
                new ValidityPeriod(
                    invalidStartDate, // should be one day after last end date (2019-11-21)
                    LocalDate.of(2019, 12, 1)
                )
            );
        });
    }
}
