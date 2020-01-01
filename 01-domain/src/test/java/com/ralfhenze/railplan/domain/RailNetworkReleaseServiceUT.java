package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.RailNetworkReleaseService;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RailNetworkReleaseServiceUT {

    /*
    @ParameterizedTest
    @CsvSource({
        // invalid start dates for new RailNetwork
        "2019-11-19",
        "2019-11-20", // the only valid date is 2019-11-21
        "2019-11-22",
    })
     */
    public void ensuresConsecutiveValidityPeriods(LocalDate invalidStartDate) {
        final var railNetworkRepository = mock(ReleasedRailNetworkRepository.class);
        final var lastRailNetwork = mock(ReleasedRailNetwork.class);

        given(lastRailNetwork.getPeriod())
            .willReturn(
                new ValidityPeriod(
                    LocalDate.of(2019, 11, 14),
                    LocalDate.of(2019, 11, 20) // last end date
                )
            );

        given(railNetworkRepository.getLastReleasedRailNetwork())
            .willReturn(Optional.of(lastRailNetwork));

        final var releaseService = new RailNetworkReleaseService(railNetworkRepository);
        final var draft = mock(RailNetworkDraft.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            releaseService.release(
                draft,
                invalidStartDate, // should be one day after last end date (2019-11-21)
                LocalDate.of(2019, 12, 1)
            );
        });
    }
}
