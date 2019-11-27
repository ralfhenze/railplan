package com.ralfhenze.railplan.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    features = {"src/test/resources/com/ralfhenze/railplan/features/"},
    glue = {"com.ralfhenze.railplan.acceptance.stepdefs"}
)
public class CucumberTest {}
