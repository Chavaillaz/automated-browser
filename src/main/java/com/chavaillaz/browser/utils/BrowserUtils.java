package com.chavaillaz.browser.utils;

import static java.util.Collections.singletonList;

import java.util.HashMap;
import java.util.Map;

import com.chavaillaz.browser.engine.Browser;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

@Slf4j
@UtilityClass
public class BrowserUtils {

    /**
     * Gets the driver of the given browser.
     *
     * @param browser The browser from which get the driver
     * @return The driver of the given browser
     */
    public static WebDriver getDriver(Browser browser) {
        WebDriver driver = switch (browser) {
            case EDGE -> getEdgeDriver();
            case CHROME -> getChromeDriver();
            case FIREFOX -> getFirefoxDriver();
        };
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Gets the driver for Google Chrome browser.
     *
     * @return The chrome driver
     */
    public static WebDriver getChromeDriver() {
        return WebDriverManager.chromedriver()
                .capabilities(getChromeOptions())
                .clearResolutionCache()
                .clearDriverCache()
                .create();
    }

    /**
     * Gets the options for Google Chrome browser.
     *
     * @return The chrome options
     */
    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", preferences);
        options.setExperimentalOption("excludeSwitches", singletonList("enable-automation"));
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--safebrowsing-disable-extension-blacklist");
        options.addArguments("--safebrowsing-disable-download-protection");
        return options;
    }

    /**
     * Gets the driver for Mozilla Firefox.
     *
     * @return The firefox driver
     */
    public static WebDriver getFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions()
                .merge(getChromeOptions());
        return WebDriverManager.firefoxdriver()
                .capabilities(options)
                .clearResolutionCache()
                .clearDriverCache()
                .create();
    }

    /**
     * Gets the driver for Microsoft Edge.
     * Note that this driver cannot be used in a docker container.
     *
     * @return The edge driver
     */
    public static WebDriver getEdgeDriver() {
        EdgeOptions options = new EdgeOptions()
                .merge(getChromeOptions());
        return WebDriverManager.edgedriver()
                .capabilities(options)
                .clearResolutionCache()
                .clearDriverCache()
                .create();
    }

}
