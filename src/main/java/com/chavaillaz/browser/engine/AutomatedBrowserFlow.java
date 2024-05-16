package com.chavaillaz.browser.engine;

import static java.util.Optional.ofNullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class AutomatedBrowserFlow<B extends AutomatedBrowser, C> {

    private final B automatedBrowser;
    private BiConsumer<Exception, C> defaultExceptionHandler;
    private C context;

    /**
     * Creates a new automated browser flow.
     *
     * @param automatedBrowser The automated browser instance to use
     */
    public AutomatedBrowserFlow(B automatedBrowser) {
        this.automatedBrowser = automatedBrowser;
    }

    /**
     * Creates a new automated browser flow.
     * This will use an instance of {@link AutomatedBrowser} behind.
     *
     * @param driver The browser driver to use
     */
    public AutomatedBrowserFlow(WebDriver driver) {
        this.automatedBrowser = (B) new AutomatedBrowser(driver);
    }

    /**
     * Sets the context instance used to store data and state between browsing steps.
     *
     * @param context The context instance to set
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withContext(C context) {
        this.context = context;
        return this;
    }

    /**
     * Sets the default exception handler when executing flow steps.
     *
     * @param exceptionHandler The exception handler to set
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withDefaultExceptionHandler(BiConsumer<Exception, C> exceptionHandler) {
        this.defaultExceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Sets the default exception handler when executing flow steps.
     *
     * @param exceptionHandler The exception handler to set
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withDefaultExceptionHandler(Consumer<Exception> exceptionHandler) {
        this.defaultExceptionHandler = (exception, unusedContext) -> exceptionHandler.accept(exception);
        return this;
    }

    /**
     * Executes a flow step with a specific exception handler.
     *
     * @param step             The step to execute
     * @param exceptionHandler The exception handler overriding the default one
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withStep(BiConsumer<B, C> step, BiConsumer<Exception, C> exceptionHandler) {
        try {
            step.accept(automatedBrowser, context);
        } catch (Exception e) {
            handleException(exceptionHandler, e);
        }
        return this;
    }

    /**
     * Executes a flow step.
     *
     * @param step The step to execute
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withStep(BiConsumer<B, C> step) {
        return withStep(step, defaultExceptionHandler);
    }

    /**
     * Executes a flow step with a specific exception handler.
     *
     * @param step             The step to execute
     * @param exceptionHandler The exception handler overriding the default one
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withStep(Consumer<B> step, BiConsumer<Exception, C> exceptionHandler) {
        try {
            step.accept(automatedBrowser);
        } catch (Exception e) {
            handleException(exceptionHandler, e);
        }
        return this;
    }

    /**
     * Executes a flow step.
     *
     * @param step The step to execute
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withStep(Consumer<B> step) {
        return withStep(step, defaultExceptionHandler);
    }

    /**
     * Executes a flow step with a specific exception handler.
     *
     * @param step             The step to execute
     * @param exceptionHandler The exception handler overriding the default one
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withStep(Runnable step, BiConsumer<Exception, C> exceptionHandler) {
        try {
            step.run();
        } catch (Exception e) {
            handleException(exceptionHandler, e);
        }
        return this;
    }

    /**
     * Executes a flow step.
     *
     * @param step The step to execute
     * @return The current flow instance
     */
    public AutomatedBrowserFlow<B, C> withStep(Runnable step) {
        return withStep(step, defaultExceptionHandler);
    }

    /**
     * Handles an exception during a browsing step.
     *
     * @param exceptionHandler The exception handler to use, may be {@code null}
     * @param exception        The exception to handle
     */
    protected void handleException(BiConsumer<Exception, C> exceptionHandler, Exception exception) {
        ofNullable(exceptionHandler).ifPresentOrElse(
                handler -> handler.accept(exception, context),
                () -> log.error("Unhandled exception in step", exception));
    }

}
