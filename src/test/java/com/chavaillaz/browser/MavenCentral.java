package com.chavaillaz.browser;

import com.chavaillaz.browser.engine.AutomatedBrowser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import static com.chavaillaz.browser.Constants.*;

@Slf4j
public class MavenCentral extends AutomatedBrowser {

    public static final String SCREENSHOT_PATH = "maven.png";

    public MavenCentral(WebDriver driver) {
        super(driver);
    }

    public void searchArtifactBadge(String artifact) {
        navigate(MAVEN_URL, MAVEN_SEARCH_FIELD);
        send(MAVEN_SEARCH_FIELD, artifact);
        click(MAVEN_SEARCH_BUTTON);
        wait(MAVEN_SEARCH_RESULTS);

        if (exist(MAVEN_SEARCH_RESULTS)) {
            log.info("Last version of artifact {} is {}", artifact, getText(MAVEN_SEARCH_RESULTS));
            click(MAVEN_SEARCH_RESULTS);
            wait(MAVEN_BADGE_CARD);
            scroll(MAVEN_BADGE_CARD);
            highlight(MAVEN_BADGE_CARD);
            log.info("Markdown to use for artifact {} is {}", artifact, getAttribute(MAVEN_BADGE_CONTENT, "value"));
            screenshot(SCREENSHOT_PATH);
        }
    }

}
