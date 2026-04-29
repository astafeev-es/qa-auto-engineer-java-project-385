package hexlet.code;

import hexlet.code.pages.LoginPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmokeTest extends BaseTest {

    @Test
    void testApplicationLoadsCoreUi() {
        LoginPage loginPage = new LoginPage(driver, wait);

        driver.get(config.baseUrl());

        assertEquals("Task manager", driver.getTitle(), "Page title should match the application title");
        assertTrue(loginPage.isLoginPageDisplayed());
    }
}
