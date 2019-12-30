package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidityPeriodUT {

    /*
    @ParameterizedTest
    @CsvSource({"2019-11-15", "2019-11-16"})
     */
    public void isInvalidWithStartDateAfterOrEqualToEndDate(LocalDate invalidStartDate) {
        final var period = new ValidityPeriod(invalidStartDate, LocalDate.of(2019, 11, 15));
        assertThat(period.isValid()).isFalse();
    }
}
