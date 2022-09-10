package com.chavaillaz.browser;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

@UtilityClass
public class Constants {

    public static final String MAVEN_URL = "https://search.maven.org";
    public static final By MAVEN_SEARCH_FIELD = By.xpath("//app-home//app-search//input");
    public static final By MAVEN_SEARCH_BUTTON = By.xpath("//app-home//mat-icon[contains(., 'search')]");
    public static final By MAVEN_SEARCH_RESULTS = By.xpath("//app-artifacts//table/tbody/tr/td//div[@class='latest']");
    public static final By MAVEN_BADGE_CARD = By.xpath("//mat-card[contains(., 'Badge')]");
    public static final By MAVEN_BADGE_CONTENT = By.xpath("//mat-card[contains(., 'Badge')]//textarea");

}
