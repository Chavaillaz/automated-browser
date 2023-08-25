package com.chavaillaz.browser;

import com.chavaillaz.browser.engine.AutomatedBrowser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
@Getter
@Setter
public class MavenCentral extends AutomatedBrowser {

    public static final String SCREENSHOT_PATH = "maven.png";
    private MavenCentralData data;

    public MavenCentral(WebDriver driver) {
        super(driver);
    }

    public void searchArtifact(String artifact) {
        setData(new MavenCentralData(artifact));
        stepSearchArtifact();
        stepLogLastVersion();
        stepHighlightSnippet();
    }

    public void stepSearchArtifact() {
        MavenCentralStep.stepSearchArtifact(this, getData());
    }

    public void stepLogLastVersion() {
        MavenCentralStep.stepLogLastVersion(this, getData());
    }

    public void stepHighlightSnippet() {
        MavenCentralStep.stepHighlightSnippet(this, getData());
    }

}
