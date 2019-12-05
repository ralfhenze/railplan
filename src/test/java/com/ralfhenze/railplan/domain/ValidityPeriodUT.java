package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ValidityPeriodUT {

    @ParameterizedTest
    @CsvSource({"2019-11-15", "2019-11-16"})
    void cannotBeConstructedWithStartDateAfterOrEqualToEndDate(LocalDate invalidStartDate) {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new ValidityPeriod(invalidStartDate, LocalDate.of(2019, 11, 15))
        );
    }
}
