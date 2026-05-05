package hexlet.code.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElementHelper.class);
    private final WebDriverWait wait;

    public ElementHelper(WebDriverWait wait) {
        this.wait = wait;
    }

    public boolean isVisible(WebElement element) {
        try {
            LOGGER.debug("Checking visibility of element: {}", element);
            WebElement visibility = wait.until(ExpectedConditions.visibilityOf(element));
            boolean displayed = visibility != null && visibility.isDisplayed();
            LOGGER.debug("Element visibility: {}", displayed);
            return displayed;
        } catch (Exception e) {
            LOGGER.error("Element is not visible: {}", element);
            return false;
        }
    }

    public boolean isVisibleBy(By locator) {
        try {
            LOGGER.debug("Checking visibility by locator: {}", locator);
            WebElement visibility = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            boolean displayed = visibility != null && visibility.isDisplayed();
            LOGGER.debug("Locator visibility: {}", displayed);
            return displayed;
        } catch (Exception e) {
            LOGGER.error("Element with locator {} is not visible", locator);
            return false;
        }
    }

    public void sendKeys(WebElement element, String text) {
        try {
            LOGGER.debug("Sending keys '{}' to element: {}", text, element);
            WebElement readyElement = wait.until(ExpectedConditions.visibilityOf(element));
            if (readyElement != null) {
                readyElement.clear();
                readyElement.sendKeys(text);
            } else {
                LOGGER.error("Element {} not found for sendKeys", element);
            }
        } catch (Exception e) {
            LOGGER.error("Error sending keys to element: {}", element, e);
        }
    }

    public void click(WebElement element) {
        try {
            LOGGER.debug("Clicking element: {}", element);
            wait.pollingEvery(Duration.ofMillis(500))
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
        } catch (Exception e) {
            LOGGER.error("Error clicking element: {}", element);
            throw e;
        }
    }
}
