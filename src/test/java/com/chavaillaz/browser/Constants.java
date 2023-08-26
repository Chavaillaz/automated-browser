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
    public static final By MAVEN_POM = xpath("//main//pre[@data-test='pom-file']");
    public static final By MAVEN_POM_COPY = xpath("//main//div[contains(@class, 'POMFile')]//button[@data-test='copy-to-clipboard-btn']");

}
