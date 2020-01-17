package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(JUnitParamsRunner.class)
public class TrainStationIdUT {

    @Test
    @Parameters({"-1", "0"})
    public void cannotBeConstructedWithNonPositiveStationId(int invalidStationId) {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new TrainStationId(invalidStationId)
        );
    }
}
