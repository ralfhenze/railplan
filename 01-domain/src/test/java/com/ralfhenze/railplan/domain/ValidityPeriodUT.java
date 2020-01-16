package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(JUnitParamsRunner.class)
public class ValidityPeriodUT {

    @Test
    @Parameters({"2019-11-15", "2019-11-16"})
    public void cannotBeConstructedWithStartDateAfterOrEqualToEndDate(
        String invalidStartDateString
    ) {
        final var invalidStartDate = LocalDate.parse(invalidStartDateString);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new ValidityPeriod(invalidStartDate, LocalDate.of(2019, 11, 15))
        );
    }
}
