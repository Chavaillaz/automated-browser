package com.chavaillaz.browser;

import com.chavaillaz.browser.engine.AutomatedBrowser;
import lombok.extern.slf4j.Slf4j;

import static com.chavaillaz.browser.Constants.*;
import static com.chavaillaz.browser.MavenCentral.SCREENSHOT_PATH;

@Slf4j
public class MavenCentralStep {

    public static void stepSearchArtifact(AutomatedBrowser browser, MavenCentralData data) {
        browser.navigate(MAVEN_URL, MAVEN_SEARCH_FIELD);
        browser.send(MAVEN_SEARCH_FIELD, data.getArtifact());
        browser.click(MAVEN_SEARCH_BUTTON);
        browser.wait(MAVEN_SEARCH_RESULTS);
    }

    public static void stepLogLastVersion(AutomatedBrowser browser, MavenCentralData data) {
        if (browser.exist(MAVEN_SEARCH_RESULTS)) {
            String lastVersion = browser.getText(MAVEN_SEARCH_RESULTS);
            data.setLastVersion(lastVersion);
            log.info("Last version of artifact {} is {}", data.getArtifact(), lastVersion);
        }
    }

    public static void stepHighlightBadge(AutomatedBrowser browser, MavenCentralData data) {
        browser.click(MAVEN_SEARCH_RESULTS);
        browser.wait(MAVEN_BADGE_CARD);
        browser.scroll(MAVEN_BADGE_CARD);
        browser.highlight(MAVEN_BADGE_CARD);
        String markdown = browser.getAttribute(MAVEN_BADGE_CONTENT, "value");
        log.info("Markdown for {} version {} is {}", data.getArtifact(), data.getLastVersion(), markdown);
        browser.screenshot(SCREENSHOT_PATH);
    }

}
