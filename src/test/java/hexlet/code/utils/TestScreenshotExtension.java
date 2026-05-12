package hexlet.code.utils;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class TestScreenshotExtension implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Object testInstance = context.getRequiredTestInstance();
        try {
            java.lang.reflect.Field driverField = testInstance.getClass().getSuperclass().getDeclaredField("driver");
            driverField.setAccessible(true);
            WebDriver driver = (WebDriver) driverField.get(testInstance);
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot on failure", new ByteArrayInputStream(screenshot));
            }
        } catch (Exception e) {
            // Ignore if we can't take a screenshot
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
    }
}
