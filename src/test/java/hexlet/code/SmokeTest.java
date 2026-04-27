package hexlet.code;

import hexlet.code.pages.LoginPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmokeTest extends BaseTest {

    @Test
    public void testApplicationLoadsCoreUi() {
        LoginPage loginPage = new LoginPage(driver, wait);

        driver.get(baseUrl);

        assertEquals("Task manager", driver.getTitle(), "Page title should match the application title");
        assertTrue(loginPage.isLoginPageDisplayed(), "Login form should be visible");
    }
}
