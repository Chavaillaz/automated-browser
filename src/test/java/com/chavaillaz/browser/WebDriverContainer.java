package com.chavaillaz.browser;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.rnorth.ducttape.timeouts.Timeouts;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static com.chavaillaz.browser.utils.BrowserUtils.getChromeOptions;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Override the method to get the web driver in order to increase timeout to 30 seconds.
 */
public class WebDriverContainer<C extends BrowserWebDriverContainer<C>> extends BrowserWebDriverContainer<C> {

    private RemoteWebDriver driver;

    @Override
    public synchronized RemoteWebDriver getWebDriver() {
        if (driver == null) {
            driver = Unreliables.retryUntilSuccess(30, SECONDS,
                    () -> Timeouts.getWithTimeout(30, SECONDS,
                            () -> new RemoteWebDriver(getSeleniumAddress(), getChromeOptions())));
        }
        return driver;
    }
}
