package com.ralfhenze.rms.railnetworkplanning.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkPeriodTest {

    @ParameterizedTest
    @CsvSource({"2019-11-15", "2019-11-16"})
    void should_ensure_start_date_is_before_end_date(LocalDate invalidStartDate) {
        assertThrows(Exception.class, () -> {
            new RailNetworkPeriod(invalidStartDate, LocalDate.of(2019, 11, 15));
        });
    }
}
