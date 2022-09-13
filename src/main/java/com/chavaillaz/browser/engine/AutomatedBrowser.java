package com.chavaillaz.browser.engine;

import com.chavaillaz.browser.exception.BrowserException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.awt.Rectangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Thread.currentThread;
import static java.time.Duration.ofSeconds;
import static javax.imageio.ImageIO.write;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.openqa.selenium.OutputType.FILE;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Slf4j
@Getter
public class AutomatedBrowser implements Closeable {

    private final WebDriver driver;

    /**
     * Creates an automated browser using the given driver.
     *
     * @param driver The browser driver to use
     */
    public AutomatedBrowser(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Gets the logger.
     *
     * @return The logger
     */
    public Logger getLogger() {
        return log;
    }

    /**
     * Changes the window size of the browser.
     *
     * @param width  The desired window width
     * @param height The desired window height
     */
    public void setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new Dimension(width, height));
    }

    /**
     * Navigates to a web page.
     * Override it to manage for example authentication to services.
     *
     * @param url The web page to access
     */
    public void navigate(String url) {
        log.debug("Navigating to {}", url);
        getDriver().get(url);
    }

    /**
     * Navigates to a web page and waits 30 seconds for an element to be present.
     *
     * @param url      The web page to access
     * @param selector The selector of the element to wait for
     * @throws TimeoutException If the timeout expires
     */
    public void navigate(String url, By selector) {
        navigate(url);
        wait(selector);
    }

    /**
     * Waits 30 seconds for an element to be present.
     *
     * @param selector The element to wait for
     * @throws TimeoutException If the timeout expires
     */
    public void wait(By selector) {
        wait(presenceOfElementLocated(selector));
    }

    /**
     * Waits 30 seconds for a condition to happen.
     *
     * @param condition The condition to wait for
     * @throws TimeoutException If the timeout expires
     */
    public <E> void wait(ExpectedCondition<E> condition) {
        wait(condition, 30);
    }

    /**
     * Waits the given amount of time for a condition to happen.
     *
     * @param condition The condition to wait for
     * @param seconds   The timeout in seconds
     * @throws TimeoutException If the timeout expires
     */
    public <E> void wait(ExpectedCondition<E> condition, int seconds) {
        new WebDriverWait(getDriver(), ofSeconds(seconds)).until(condition);
    }

    /**
     * Gets the text of an element.
     *
     * @param selector The selector of the element to get the text from
     * @return The text of the element, {@code null} otherwise
     */
    public String getText(By selector) {
        return getElement(selector)
                .map(WebElement::getText)
                .orElse(null);
    }

    /**
     * Gets the attribute of an element.
     *
     * @param selector  The selector of the element to get the attribute from
     * @param attribute The attribute to get from the selected element
     * @return The given attribute of the selected element, {@code null} otherwise
     */
    public String getAttribute(By selector, String attribute) {
        return getElement(selector)
                .map(element -> element.getAttribute(attribute))
                .orElse(null);
    }

    /**
     * Gets an element.
     *
     * @param selector The selector of the element to get
     * @return The first corresponding element, {@link Optional#empty()} otherwise
     */
    public Optional<WebElement> getElement(By selector) {
        return getElements(selector)
                .stream()
                .findFirst();
    }

    /**
     * Gets an element present in a given element.
     *
     * @param element  The element in which the other to get is present
     * @param selector The selector of the element to get
     * @return The first corresponding element, {@link Optional#empty()} otherwise
     */
    public Optional<WebElement> getElement(WebElement element, By selector) {
        return getElements(element, selector)
                .stream()
                .findFirst();
    }

    /**
     * Gets a list of elements.
     *
     * @param selector The selector of the elements to get
     * @return The corresponding elements, an empty list if there is no one
     */
    public List<WebElement> getElements(By selector) {
        return getDriver().findElements(selector);
    }

    /**
     * Gets a list of elements present in a given element.
     *
     * @param element  The element in which the others to get are present
     * @param selector The selector of the elements to get
     * @return The corresponding elements, an empty list if there is no one
     */
    public List<WebElement> getElements(WebElement element, By selector) {
        return element.findElements(selector);
    }

    /**
     * Checks if an element exists.
     *
     * @param selector The selector of the element to find
     * @return {@code true} if the element is found, {@code false} otherwise
     */
    public boolean exist(By selector) {
        return getElement(selector).isPresent();
    }

    /**
     * Sends a char sequence to the given element.
     *
     * @param selector The selector of the element to send the sequence to
     * @param value    The char sequence to send to the element
     * @throws NoSuchElementException If no matching element is found
     */
    public void send(By selector, CharSequence value) {
        getDriver().findElement(selector).sendKeys(value);
    }

    /**
     * Clicks on an element.
     *
     * @param selector The selector of the element to click on
     * @throws NoSuchElementException If no matching element is found
     */
    public void click(By selector) {
        getDriver().findElement(selector).click();
    }

    /**
     * Finds the first valid selector in the given list.
     *
     * @param selectors The selectors to check
     * @return The first valid selector
     */
    public Optional<By> findFirstExisting(By... selectors) {
        return findExisting(selectors)
                .stream()
                .findFirst();
    }

    /**
     * Finds the valid selectors in the given list.
     *
     * @param selectors The selectors to check
     * @return The valid selectors
     */
    public List<By> findExisting(By... selectors) {
        return Stream.of(selectors)
                .filter(this::exist)
                .toList();
    }

    /**
     * Scrolls to the given element.
     *
     * @param selector The selector of the element to scroll to
     */
    public void scroll(By selector) {
        getElement(selector).ifPresent(this::scroll);
    }

    /**
     * Scrolls to the given element.
     *
     * @param element The element to scroll to
     */
    public void scroll(WebElement element) {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element);
        actions.perform();
    }

    /**
     * Highlights in yellow an element.
     *
     * @param selector The selector of the element to highlight
     */
    public void highlight(By selector) {
        getElements(selector).forEach(this::highlight);
    }

    /**
     * Highlights in yellow an element.
     *
     * @param element The element to highlight
     */
    public void highlight(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.background='yellow'", element);
    }

    /**
     * Takes a screenshot of the browser content only.
     *
     * @param path The path where to store the screenshot
     */
    public void screenshot(String path) {
        screenshot(path, false);
    }

    /**
     * Takes a screenshot of the browser or the full screen.
     * Note that the full screenshot is not available when running in a docker container.
     *
     * @param path       The path where to store the screenshot
     * @param fullScreen Indicates whether the screenshot is taken for the whole screen or just for the browser window
     */
    public void screenshot(String path, boolean fullScreen) {
        try {
            // Wait for the browser to render changes (e.g. for highlighted elements)
            Thread.sleep(500);

            if (fullScreen) {
                Rectangle screen = new Rectangle(getDefaultToolkit().getScreenSize());
                BufferedImage image = new Robot().createScreenCapture(screen);
                write(image, "png", new File(path));
            } else {
                File file = ((TakesScreenshot) getDriver()).getScreenshotAs(FILE);
                copyFile(file, new File(path), true);
            }
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } catch (Exception e) {
            throw new BrowserException("Unable to generate screenshot " + path, e);
        }
    }

    @Override
    public void close() throws IOException {
        // Do not close the driver as it may be used somewhere else
    }

}
