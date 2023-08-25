package com.chavaillaz.browser;

import com.chavaillaz.browser.engine.AutomatedBrowser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;

import static com.chavaillaz.browser.Constants.*;
import static com.chavaillaz.browser.MavenCentral.SCREENSHOT_PATH;

@Slf4j
public class MavenCentralStep {

    public static void stepSearchArtifact(AutomatedBrowser browser, MavenCentralData data) {
        browser.navigate(MAVEN_URL, MAVEN_SEARCH_FIELD);
        browser.send(MAVEN_SEARCH_FIELD, data.getArtifact());
        browser.send(MAVEN_SEARCH_FIELD, Keys.ENTER);
        browser.wait(MAVEN_SEARCH_RESULTS);
    }

    public static void stepLogLastVersion(AutomatedBrowser browser, MavenCentralData data) {
        if (browser.exist(MAVEN_VERSION)) {
            String lastVersion = browser.getText(MAVEN_VERSION);
            data.setLastVersion(lastVersion);
            log.info("Last version of artifact {} is {}", data.getArtifact(), lastVersion);
        }
    }

    public static void stepHighlightSnippet(AutomatedBrowser browser, MavenCentralData data) {
        browser.click(MAVEN_SEARCH_RESULTS);
        browser.wait(MAVEN_SNIPPET);
        browser.scroll(MAVEN_SNIPPET);
        browser.highlight(MAVEN_SNIPPET);
        browser.screenshot(SCREENSHOT_PATH);
    }

}
