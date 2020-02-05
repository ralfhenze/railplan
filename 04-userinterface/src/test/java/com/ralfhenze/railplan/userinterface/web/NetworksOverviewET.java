package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkMongoDbRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NetworksOverviewET {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private MongoTemplate mongoTemplate;

    private RemoteWebDriver driver;
    private String baseUrl;

    @Before
    public void initializeSelenium() {
        final var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);

        baseUrl = "http://localhost:" + serverPort;
        mongoTemplate.dropCollection(RailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void userCanCreateNewNetwork() {
        // Given I am on the Networks page
        driver.get(baseUrl + "/networks");

        // When I click the Add-Network button
        driver.findElementById("add-network-button").click();

        // Then I should see one Network in the navigation
        driver.get(baseUrl + "/networks");
        final var networkNavEntries = driver.findElementsByCssSelector("#network-navigation li");
        assertThat(networkNavEntries).hasSize(1);
    }

    @After
    public void tearDown() {
        driver.close();
    }
}
