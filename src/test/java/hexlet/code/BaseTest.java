package hexlet.code;
import hexlet.code.pages.login.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;
    protected String username;
    protected String password;

    @BeforeEach
    public void setUp() {
        baseUrl = System.getProperty("APP_BASE_URL", System.getenv("APP_BASE_URL"));
        if (baseUrl == null) {
            baseUrl = "http://localhost:5173";
        }

        username = System.getProperty("USERNAME", System.getenv("USERNAME"));
        if (username == null) {
            username = "aleksei98";
        }

        password = System.getProperty("PASSWORD", System.getenv("PASSWORD"));
        if (password == null) {
            password = "[trcktnghjtrn_98";
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected void login() {
        LoginPage loginPage = new LoginPage(driver, wait);
        driver.get(baseUrl);
        loginPage.login(username, password);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
