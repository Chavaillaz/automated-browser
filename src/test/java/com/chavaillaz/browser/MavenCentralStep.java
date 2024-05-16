package com.chavaillaz.browser;

import static com.chavaillaz.browser.Constants.MAVEN_BADGE_CONTENT;
import static com.chavaillaz.browser.Constants.MAVEN_SEARCH_FIELD;
import static com.chavaillaz.browser.Constants.MAVEN_SEARCH_RESULTS;
import static com.chavaillaz.browser.Constants.MAVEN_SEARCH_VERSION;
import static com.chavaillaz.browser.Constants.MAVEN_URL;
import static com.chavaillaz.browser.MavenCentral.SCREENSHOT_PATH;

import com.chavaillaz.browser.engine.AutomatedBrowser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

@Slf4j
public class MavenCentralStep {

    public static void stepSearchArtifact(AutomatedBrowser browser, MavenCentralData data) {
        browser.navigate(MAVEN_URL, MAVEN_SEARCH_FIELD);
        browser.click(MAVEN_SEARCH_FIELD);
        browser.send(MAVEN_SEARCH_FIELD, data.getArtifact());
        browser.send(MAVEN_SEARCH_FIELD, Keys.RETURN);
        browser.wait(MAVEN_SEARCH_RESULTS);
    }

    public static void stepLogLastVersion(AutomatedBrowser browser, MavenCentralData data) {
        browser.wait(MAVEN_SEARCH_RESULTS);
        browser.getElement(MAVEN_SEARCH_RESULTS).ifPresent(element -> {
            browser.wait(MAVEN_SEARCH_VERSION);
            String lastVersion = browser.getElements(MAVEN_SEARCH_VERSION).stream()
                    .map(WebElement::getText)
                    .findFirst()
                    .orElseThrow();
            data.setLastVersion(lastVersion);
            log.info("Last version of artifact {} is {}", data.getArtifact(), lastVersion);
        });
    }

    public static void stepHighlightSnippet(AutomatedBrowser browser, MavenCentralData data) {
        browser.click(MAVEN_SEARCH_RESULTS);
        browser.wait(MAVEN_BADGE_CONTENT);
        browser.hover(MAVEN_BADGE_CONTENT);
        browser.highlight(MAVEN_BADGE_CONTENT);
        String markdown = browser.getText(MAVEN_BADGE_CONTENT);
        log.info("Maven for {} version {} is\n{}", data.getArtifact(), data.getLastVersion(), markdown);
        browser.screenshot(SCREENSHOT_PATH);
    }

}
