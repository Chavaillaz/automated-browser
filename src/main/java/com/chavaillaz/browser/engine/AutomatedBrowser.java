package com.chavaillaz.browser.engine;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Thread.currentThread;
import static java.time.Duration.ofSeconds;
import static javax.imageio.ImageIO.write;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.openqa.selenium.OutputType.FILE;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.chavaillaz.browser.exception.BrowserException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

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
     * Changes the window size of the browser.
     *
     * @param width  The desired window width
     * @param height The desired window height
     */
    public void setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new Dimension(width, height));
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
     * Gets the attribute of an element.
     *
     * @param selector  The selector of the element to get the attribute from
     * @param attribute The attribute to get from the selected element
     * @return The given attribute of the selected element, {@code null} otherwise
     */
    public String getAttribute(By selector, String attribute) {
        return getAttributes(selector, attribute)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the attribute of multiple elements.
     *
     * @param selector  The selector of the elements to get the attribute from
     * @param attribute The attribute to get from the selected elements
     * @return The given attribute of the selected elements, {@code null} otherwise
     */
    public List<String> getAttributes(By selector, String attribute) {
        return getElements(selector)
                .stream()
                .map(element -> element.getAttribute(attribute))
                .toList();
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
     * @param url          The web page to access
     * @param waitSelector The selector of the element to wait for
     * @throws TimeoutException If the timeout expires
     */
    public void navigate(String url, By waitSelector) {
        navigate(url);
        wait(waitSelector);
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
     * Checks if an element exists.
     *
     * @param selector The selector of the element to find
     * @return {@code true} if the element is found, {@code false} otherwise
     */
    public boolean exist(By selector) {
        return getElement(selector).isPresent();
    }

    /**
     * Checks if an element is visible in the current displayed part of the page (viewport).
     *
     * @param selector The selector of the element to find
     * @return {@code true} if the center of the element is visible, {@code false} otherwise
     */
    public boolean visible(By selector) {
        return getElement(selector)
                .map(this::visible)
                .orElse(false);
    }

    /**
     * Checks if the given element is visible in the current displayed part of the page (viewport).
     *
     * @param element The element to check
     * @return {@code true} if the center of the element is visible, {@code false} otherwise
     */
    public boolean visible(WebElement element) {
        return (boolean) execute("""
                var element = arguments[0],
                    box = element.getBoundingClientRect(),
                    cx = box.left + box.width / 2,
                    cy = box.top + box.height / 2,
                    e = document.elementFromPoint(cx, cy);
                for (; e; e = e.parentElement) {
                    if (e === element)
                        return true;
                }
                return false;
                """, element);
    }

    /**
     * Performs actions in the current loaded page.
     *
     * @param actionsDescriptor The consumer calling the actions to execute
     */
    public void perform(Consumer<Actions> actionsDescriptor) {
        Actions actions = new Actions(getDriver());
        actionsDescriptor.accept(actions);
        actions.perform();
    }

    /**
     * Executes a Javascript in the current loaded page.
     *
     * @param script     The script to execute
     * @param parameters The parameters to give to the script
     * @return The returned value of the script
     */
    public Object execute(String script, Object... parameters) {
        JavascriptExecutor js = ((JavascriptExecutor) getDriver());
        return js.executeScript(script, parameters);
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
     * Scrolls up or down by a certain amount of pixels.
     * Use negative values to scroll up and positive ones to scroll down.
     *
     * @param horizontal The horizontal amount of pixels to scroll
     * @param vertical   The vertical amount of pixels to scroll
     */
    public void scroll(int horizontal, int vertical) {
        execute("window.scrollBy(" + horizontal + ", " + vertical + ");");
    }

    /**
     * Scrolls to the end of the given element.
     * This can be used for example to scroll down text areas.
     *
     * @param selector The selector of the element to scroll to the end
     */
    public void scrollArea(By selector) {
        getElement(selector).ifPresent(this::scrollArea);
    }

    /**
     * Scrolls to the end of the given element.
     * This can be used for example to scroll down text areas.
     *
     * @param element The element to scroll to the end
     */
    public void scrollArea(WebElement element) {
        execute("arguments[0].scrollTo(0, arguments[0].scrollHeight)", element);
    }

    /**
     * Aligns the top of the given element to the top of the visible area of the scrollable ancestor.
     *
     * @param selector The selector of the element to align
     */
    public void alignTop(By selector) {
        getElement(selector).ifPresent(this::alignTop);
    }

    /**
     * Aligns the top of the given element to the top of the visible area of the scrollable ancestor.
     *
     * @param element The element to align
     */
    public void alignTop(WebElement element) {
        execute("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Aligns the center of the given element to the center of the visible area of the scrollable ancestor.
     *
     * @param selector The selector of the element to align
     */
    public void alignCenter(By selector) {
        getElement(selector).ifPresent(this::alignCenter);
    }

    /**
     * Aligns the center of the given element to the center of the visible area of the scrollable ancestor.
     *
     * @param element The element to align
     */
    public void alignCenter(WebElement element) {
        execute("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /**
     * Aligns the bottom of given element to the bottom of the visible area of the scrollable ancestor.
     *
     * @param selector The selector of the element to align
     */
    public void alignBottom(By selector) {
        getElement(selector).ifPresent(this::alignBottom);
    }

    /**
     * Aligns the bottom of given element to the bottom of the visible area of the scrollable ancestor.
     *
     * @param element The element to align
     */
    public void alignBottom(WebElement element) {
        execute("arguments[0].scrollIntoView(false);", element);
    }

    /**
     * Moves the mouse hover an element.
     *
     * @param selector The selector of the element to move to
     */
    public void hover(By selector) {
        getElement(selector).ifPresent(this::hover);
    }

    /**
     * Moves the mouse hover an element.
     *
     * @param element The element to move to
     */
    public void hover(WebElement element) {
        perform(action -> action.moveToElement(element));
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
        execute("arguments[0].style.background='yellow'", element);
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

            File targetFile = new File(path);
            if (fullScreen) {
                Rectangle screen = new Rectangle(getDefaultToolkit().getScreenSize());
                BufferedImage image = new Robot().createScreenCapture(screen);
                write(image, "png", targetFile);
            } else {
                File file = ((TakesScreenshot) getDriver()).getScreenshotAs(FILE);
                copyFile(file, targetFile, true);
            }
            log.debug("Screenshot saved at {}", targetFile.getAbsolutePath());
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
