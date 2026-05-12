package hexlet.code;

import hexlet.code.pages.DashboardPage;
import hexlet.code.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Authentication Tests")
class AuthTest extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Test
    @DisplayName("Successfully login with valid credentials")
    void testSuccessfulLogin() {
        loginPage.open();
        loginPage.login(config.username(), config.password());
        assertTrue(dashboardPage.isDashboardPageDisplayed(), "Dashboard should be displayed");
        assertFalse(driver.getCurrentUrl().contains("login"), "URL should not contain 'login' after successful login");
    }

    @Test
    @DisplayName("Successfully logout from the application")
    void testSuccessfulLogout() {
        loginPage.open();
        loginPage.login(config.username(), config.password())
            .logout();
        assertTrue(loginPage.isLoginPageDisplayed());
        assertTrue(driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().endsWith("/"),
            "URL should redirect to login page after logout");
    }
}
