package hexlet.code;

import hexlet.code.config.Config;
import hexlet.code.pages.LoginPage;
import hexlet.code.utils.TestScreenshotExtension;
import hexlet.code.utils.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(TestScreenshotExtension.class)
abstract class BaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected Config config;

    @BeforeEach
    void setUp() {
        config = Config.get();
        driver = WebDriverFactory.create(config);
    }

    protected void login() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login(config.username(), config.password());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
