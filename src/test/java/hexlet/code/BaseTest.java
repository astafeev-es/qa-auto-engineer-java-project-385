package hexlet.code;

import hexlet.code.config.Config;
import hexlet.code.pages.LoginPage;
import hexlet.code.utils.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

abstract class BaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Config config;

    @BeforeEach
    void setUp() {
        LOGGER.info("Setting up test environment...");
        config = Config.get();
        try {
            driver = WebDriverFactory.create(config);
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            LOGGER.info("WebDriver created successfully.");
        } catch (Exception e) {
            LOGGER.error("Failed to create WebDriver session", e);
            throw e;
        }
    }

    protected void login() {
        LOGGER.info("Logging in as user: {}", config.username());
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open();
        loginPage.login(config.username(), config.password());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            LOGGER.info("Quitting WebDriver...");
            driver.quit();
        }
    }
}
