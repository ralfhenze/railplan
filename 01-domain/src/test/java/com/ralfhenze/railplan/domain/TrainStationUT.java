package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TrainStationUT {

    @Test
    public void isInvalidWithNullArguments() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() ->
            new TrainStation(null, null, null).isValid()
        );
    }
}
