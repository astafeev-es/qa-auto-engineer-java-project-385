package hexlet.code.utils;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ElementHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElementHelper.class);
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_POLLING = Duration.ofMillis(500);

    private final WebDriver driver;

    public ElementHelper(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isVisible(WebElement element) {
        try {
            return getWait("Element should be visible")
                    .until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isVisibleBy(By locator) {
        try {
            return getWait("Element located by " + locator + " should be visible")
                    .until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void sendKeys(WebElement element, String text) {
        LOGGER.debug("Sending keys to element");
        WebElement readyElement = getWait("Element should be visible for sendKeys")
                .until(ExpectedConditions.visibilityOf(element));
        readyElement.clear();
        readyElement.sendKeys(text);
    }

    public void replaceInputValue(WebElement element, String value) {
        LOGGER.debug("Replacing input value with '{}'", value);
        WebElement readyElement = getWait("Element should be visible for replacing value")
                .until(ExpectedConditions.visibilityOf(element));
        String currentValue = readyElement.getAttribute("value");
        click(readyElement);
        readyElement.sendKeys(Keys.END);
        for (int index = 0; index < currentValue.length(); index++) {
            readyElement.sendKeys(Keys.BACK_SPACE);
        }
        readyElement.sendKeys(value);
    }

    public void click(WebElement element) {
        LOGGER.debug("Clicking element");
        getWait("Element should be clickable")
                .until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void pressEscape() {
        LOGGER.debug("Pressing ESCAPE key");
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
    }

    public String getText(WebElement element) {
        return getWait("Element should be visible to get text")
                .until(ExpectedConditions.visibilityOf(element)).getText();
    }

    public String getText(By locator) {
        return getWait("Element should be visible to get text")
                .until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public String getAttribute(WebElement element, String attribute) {
        return getWait("Element should be visible to get attribute")
                .until(ExpectedConditions.visibilityOf(element)).getAttribute(attribute);
    }

    public String getAttribute(By locator, String attribute) {
        return getWait("Element should be visible to get attribute")
                .until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute(attribute);
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public WebElement findElement(By locator) {
        return getWait("Element located by " + locator + " should be present")
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    public void waitForInvisibility(By locator) {
        getWait("Element should become invisible")
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForVisibility(WebElement element) {
        getWait("Element should become visible")
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForNumberOfElements(By locator, int number) {
        getWait("Number of elements should be " + number)
                .until(ExpectedConditions.numberOfElementsToBe(locator, number));
    }

    private WebDriverWait getWait(Duration timeout, String message) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.pollingEvery(DEFAULT_POLLING);
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        if (message != null) {
            wait.withMessage(message);
        }
        return wait;
    }

    private WebDriverWait getWait(String message) {
        return getWait(DEFAULT_TIMEOUT, message);
    }
}
