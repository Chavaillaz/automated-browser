package com.chavaillaz.browser;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static com.chavaillaz.browser.MavenCentral.SCREENSHOT_PATH;
import static com.chavaillaz.browser.utils.BrowserUtils.getChromeOptions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.SKIP;

@Testcontainers
class MavenCentralTest {

    @Container
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withCapabilities(getChromeOptions())
            .withRecordingMode(SKIP, null);

    @Test
    void testSearchArtifact() throws Exception {
        RemoteWebDriver driver = chrome.getWebDriver();

        try (MavenCentral browser = new MavenCentral(driver)) {
            browser.setWindowSize(1920, 1080);
            browser.searchArtifactBadge("org.slf4j:slf4j-api");
            assertTrue(new File(SCREENSHOT_PATH).exists());
        }

        driver.quit();
    }

}
