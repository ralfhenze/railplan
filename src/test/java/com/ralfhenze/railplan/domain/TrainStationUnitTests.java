package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TrainStationUnitTests {

    @Test
    void should_fail_on_construction_with_null_arguments() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new TrainStation(null, null, null)
        );
    }
}
