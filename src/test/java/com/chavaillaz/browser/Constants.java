package com.chavaillaz.browser;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.xpath;

@UtilityClass
public class Constants {

    public static final String MAVEN_URL = "https://central.sonatype.com";
    public static final By MAVEN_SEARCH_FIELD = xpath("//div[contains(@class, 'page-content')]//div[@data-test='search-dropdown']//input");
    public static final By MAVEN_SEARCH_RESULTS = xpath("//main//div[@data-test='component-card-item']/a[contains(@class, 'text-link')]");
    public static final By MAVEN_SNIPPET = xpath("//main//pre[@data-test='snippet']");
    public static final By MAVEN_VERSION = xpath("//main//div[@data-test='latest-version-metadata']");

}
