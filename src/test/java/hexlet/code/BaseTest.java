package hexlet.code;

import hexlet.code.config.Config;
import hexlet.code.pages.LoginPage;
import hexlet.code.utils.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Config config;

    @BeforeEach
    void setUp() {
        config = Config.get();
        driver = WebDriverFactory.create(config);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected void login() {
        LoginPage loginPage = new LoginPage(driver, wait);
        driver.get(config.baseUrl());
        loginPage.login(config.username(), config.password());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
