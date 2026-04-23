package hexlet.code;

import hexlet.code.pages.login.LoginPage;
import hexlet.code.pages.DashboardPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        loginPage.login(RandomStringUtils.secure().nextAlphanumeric(4, 8),
            RandomStringUtils.secure().nextAlphanumeric(4, 8));
        assertTrue(dashboardPage.isProfileVisible());
    }

    @Test
    public void testSuccessfulLogout() {
        loginPage.login(RandomStringUtils.secure().nextAlphanumeric(4, 8),
                RandomStringUtils.secure().nextAlphanumeric(4, 8))
            .logout();
        assertTrue(loginPage.isLoginPageDisplayed());
    }
}
