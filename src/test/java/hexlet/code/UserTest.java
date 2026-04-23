package hexlet.code;

import hexlet.code.pages.login.LoginPage;
import hexlet.code.pages.UsersPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest extends BaseTest {

    private LoginPage loginPage;
    private UsersPage usersPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver, wait);
        usersPage = new UsersPage(driver, wait);
        driver.get(baseUrl);
        loginPage.login(username, password)
            .openUsersPage();
    }

    @Test
    public void testUserCreation() {
        String email = "create" + RandomStringUtils.secure().nextAlphanumeric(4, 8) + "@test.com";
        usersPage.create(email, "John", "Doe");
        assertTrue(usersPage.isUserPresent(email));
    }

    @Test
    public void testUserEditing() {
        String email = "edit" + RandomStringUtils.secure().nextAlphanumeric(4, 8) + "@test.com";
        usersPage.create(email, "Before", "Edit");
        usersPage.edit(email, "AfterEdit");
        assertTrue(usersPage.isUserPresent("AfterEdit"));
    }

    @Test
    public void testUserDeletion() {
        String email = "delete" + RandomStringUtils.secure().nextAlphanumeric(4, 8) + "@test.com";
        usersPage.create(email, "To", "Delete");
        usersPage.deleteUser(email);
        assertFalse(usersPage.isUserPresent(email));
    }

    @Test
    public void testBulkDelete() {
        usersPage.bulkDelete();
        assertEquals(0, usersPage.getUserCount());
    }
}
