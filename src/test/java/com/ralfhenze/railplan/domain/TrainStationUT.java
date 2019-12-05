package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TrainStationUT {

    @Test
    void cannotBeConstructedWithNullArguments() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new TrainStation(null, null, null)
        );
    }
}
