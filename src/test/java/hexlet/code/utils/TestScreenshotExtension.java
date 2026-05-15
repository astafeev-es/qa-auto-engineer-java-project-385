package hexlet.code.utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.util.function.Supplier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TestScreenshotExtension implements AfterTestExecutionCallback {
    private final Supplier<WebDriver> driverSupplier;

    public TestScreenshotExtension(Supplier<WebDriver> driverSupplier) {
        this.driverSupplier = driverSupplier;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        WebDriver driver = driverSupplier.get();
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                String attachmentName = context.getExecutionException().isPresent()
                        ? "Screenshot on failure"
                        : "Final test screenshot";
                Allure.addAttachment(attachmentName, "image/png", new ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                // Ignore if we can't take a screenshot
            }
        }
    }
}
