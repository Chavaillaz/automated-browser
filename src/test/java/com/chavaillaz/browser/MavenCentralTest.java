package com.chavaillaz.browser;

import com.chavaillaz.browser.engine.AutomatedBrowserFlow;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.List;

import static com.chavaillaz.browser.MavenCentral.SCREENSHOT_PATH;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.By.xpath;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.SKIP;

@Testcontainers
class MavenCentralTest {

    public static final String ARTIFACT = "org.slf4j:slf4j-api";

    @Container
    private final BrowserWebDriverContainer<?> chrome = new WebDriverContainer<>()
            .withRecordingMode(SKIP, null);

    @Test
    void testSearchArtifact() throws Exception {
        RemoteWebDriver driver = chrome.getWebDriver();

        try (MavenCentral browser = new MavenCentral(driver)) {
            browser.setWindowSize(1920, 1080);
            browser.searchArtifact(ARTIFACT);
            assertTrue(isNotBlank(browser.getData().getLastVersion()));

            String dropdownSelection = "//select[@data-test='snippet-dropdown']";
            WebElement dropdown = browser.getElement(xpath(dropdownSelection)).orElse(null);
            assertNotNull(dropdown);

            // Check getElements
            List<WebElement> values = browser.getElements(dropdown, By.tagName("option"));
            assertFalse(values.isEmpty());

            // Check getAttribute
            String firstValue = browser.getAttribute(
                    xpath(dropdownSelection + "/option"),
                    "value");
            assertTrue(values.stream()
                    .map(element -> element.getAttribute("value"))
                    .toList()
                    .contains(firstValue));

            // Check findFirstExisting and findExisting
            By firstExisting = browser.findFirstExisting(
                            xpath("//div[@class='unknown']"),
                            xpath(dropdownSelection))
                    .orElse(null);
            assertEquals(xpath(dropdownSelection), firstExisting);
        }

        File screenshot = new File(SCREENSHOT_PATH);
        assertTrue(screenshot.exists());
        assertTrue(screenshot.delete());
    }

    @Test
    void testSearchArtifactFlowStateful() throws Exception {
        RemoteWebDriver driver = chrome.getWebDriver();

        try (MavenCentral browser = new MavenCentral(driver)) {
            browser.setData(new MavenCentralData(ARTIFACT));
            new AutomatedBrowserFlow<>(browser)
                    .withStep(MavenCentral::stepSearchArtifact)
                    .withStep(MavenCentral::stepLogLastVersion)
                    .withStep(MavenCentral::stepHighlightSnippet);
        }

        File screenshot = new File(SCREENSHOT_PATH);
        assertTrue(screenshot.exists());
        assertTrue(screenshot.delete());
    }

    @Test
    void testSearchArtifactFlowStateless() throws Exception {
        RemoteWebDriver driver = chrome.getWebDriver();

        try (MavenCentral browser = new MavenCentral(driver)) {
            new AutomatedBrowserFlow<MavenCentral, MavenCentralData>(browser)
                    .withContext(new MavenCentralData(ARTIFACT))
                    .withStep(MavenCentralStep::stepSearchArtifact)
                    .withStep(MavenCentralStep::stepLogLastVersion)
                    .withStep(MavenCentralStep::stepHighlightSnippet);
        }

        File screenshot = new File(SCREENSHOT_PATH);
        assertTrue(screenshot.exists());
        assertTrue(screenshot.delete());
    }

}
