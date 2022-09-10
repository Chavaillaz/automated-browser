package com.chavaillaz.browser.utils;

import com.chavaillaz.browser.engine.Browser;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

@Slf4j
@UtilityClass
public class BrowserUtils {

    public static WebDriver getDriver(Browser browser) {
        WebDriver driver = switch (browser) {
            case EDGE -> getEdgeDriver();
            case CHROME -> getChromeDriver();
            case FIREFOX -> getFirefoxDriver();
        };
        driver.manage().window().maximize();
        return driver;
    }

    public static WebDriver getChromeDriver() {
        WebDriverManager.chromedriver().create();
        return new ChromeDriver(getChromeOptions());
    }

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

    public static WebDriver getFirefoxDriver() {
        WebDriverManager.firefoxdriver().create();
        FirefoxOptions options = new FirefoxOptions().merge(getChromeOptions());
        return new FirefoxDriver(options);
    }

    public static WebDriver getEdgeDriver() {
        WebDriverManager.edgedriver().create();
        EdgeOptions options = new EdgeOptions().merge(getChromeOptions());
        return new EdgeDriver(options);
    }

}
