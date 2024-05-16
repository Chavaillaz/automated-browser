package com.chavaillaz.browser;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

@UtilityClass
public class Constants {

    public static final String MAVEN_URL = "https://central.sonatype.com/";
    public static final By MAVEN_SEARCH_FIELD = By.xpath("(//input[@role='searchbox'])[2]");
    public static final By MAVEN_SEARCH_RESULTS = By.xpath("(//ul/li/div[contains(@class, 'card')]/a)[1]");
    public static final By MAVEN_SEARCH_VERSION = By.xpath("//div[contains(., 'Latest version')]/dd/div");
    public static final By MAVEN_BADGE_CARD = By.xpath("//mat-card[contains(., 'Badge')]");
    public static final By MAVEN_BADGE_CONTENT = By.xpath("//pre[@data-test='snippet']");

}
