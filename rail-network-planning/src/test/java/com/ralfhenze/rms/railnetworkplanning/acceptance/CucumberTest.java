package com.ralfhenze.rms.railnetworkplanning.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    features = {"src/test/resources/com/ralfhenze/rms/railnetworkplanning/features/"},
    glue = {"com.ralfhenze.rms.railnetworkplanning.acceptance.stepdefs"}
)
public class CucumberTest {}
