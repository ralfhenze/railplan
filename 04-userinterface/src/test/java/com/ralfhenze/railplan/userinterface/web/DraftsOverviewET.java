package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
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
public class DraftsOverviewET {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private MongoTemplate mongoTemplate;

    private RemoteWebDriver driver;
    private String baseUrl;

    @Before
    public void setup() {
        final var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);

        baseUrl = "http://localhost:" + serverPort;
        mongoTemplate.dropCollection(RailNetworkDraftMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void userCanCreateNewDraft() {
        // Given I am on the Drafts page
        driver.get(baseUrl + "/drafts");

        // When I click the Add-Draft button
        driver.findElementById("add-draft-button").click();

        // Then I should see one Draft in the navigation
        final var draftNavEntries = driver.findElementsByCssSelector("#draft-navigation li");
        assertThat(draftNavEntries).hasSize(1);
    }

    @After
    public void tearDown() {
        driver.close();
    }
}
