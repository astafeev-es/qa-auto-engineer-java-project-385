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
        options.addArguments("--window-size=" + config.windowSize());

        try {
            WebDriver driver = new ChromeDriver(options);
            LOGGER.info("Chrome WebDriver created successfully");
            return driver;
        } catch (Exception e) {
            LOGGER.error("Error creating Chrome WebDriver", e);
            throw e;
        }
    }
}
