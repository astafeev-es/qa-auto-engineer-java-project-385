package hexlet.code;

import hexlet.code.pages.DashboardPage;
import hexlet.code.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthTest extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver, wait);
        dashboardPage = new DashboardPage(driver, wait);
    }

    @Test
    void testSuccessfulLogin() {
        loginPage.open();
        loginPage.login(config.username(), config.password());
        assertTrue(dashboardPage.isDashboardPageDisplayed(), "Dashboard should be displayed");
        assertFalse(driver.getCurrentUrl().contains("login"), "URL should not contain 'login' after successful login");
    }

    @Test
    void testSuccessfulLogout() {
        loginPage.open();
        loginPage.login(config.username(), config.password())
            .logout();
        assertTrue(loginPage.isLoginPageDisplayed());
        assertTrue(driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().endsWith("/"),
            "URL should redirect to login page after logout");
    }
}
