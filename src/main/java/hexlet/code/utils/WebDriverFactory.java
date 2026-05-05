package hexlet.code.utils;

import hexlet.code.config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);

    public static WebDriver create(Config config) {
        LOGGER.info("Creating Chrome WebDriver with window size: {}", config.windowSize());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=" + config.windowSize());

        int maxAttempts = 3;
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                WebDriver driver = new ChromeDriver(options);
                LOGGER.info("Chrome WebDriver created successfully on attempt {}", attempt);
                return driver;
            } catch (Exception e) {
                LOGGER.error("Attempt {} to create Chrome WebDriver failed", attempt, e);
                lastException = e;
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(2000); // Wait before retry
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        LOGGER.error("All {} attempts to create Chrome WebDriver failed", maxAttempts);
        String msg = "Failed to create WebDriver session after " + maxAttempts + " attempts";
        throw new RuntimeException(msg, lastException);
    }
}
