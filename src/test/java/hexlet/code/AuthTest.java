package hexlet.code;

import hexlet.code.pages.DashboardPage;
import hexlet.code.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthTest extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver, wait);
        dashboardPage = new DashboardPage(driver, wait);
        driver.get(baseUrl);
    }

    @Test
    public void testSuccessfulLogin() {
        loginPage.login(username, password);
        assertTrue(dashboardPage.isProfileVisible(), "Profile button should be visible after successful login");
        assertFalse(driver.getCurrentUrl().contains("login"), "URL should not contain 'login' after successful login");
    }

    @Test
    public void testSuccessfulLogout() {
        loginPage.login(username, password)
            .logout();
        assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed after logout");
        assertTrue(driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().endsWith("/"),
            "URL should redirect to login page after logout");
    }
}
