package hexlet.code;

import hexlet.code.pages.UsersPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest extends BaseTest {

    private UsersPage usersPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        usersPage = new UsersPage(driver, wait);
        login();
        usersPage.openUsersPage();
    }

    @Test
    public void testUserCreation() {
        String email = "create" + RandomStringUtils.secure().nextAlphanumeric(4, 8) + "@test.com";
        usersPage.create(email, "John", "Doe");
        assertTrue(usersPage.isUserPresent(email), "Created user should be present in the user list");
    }

    @Test
    public void testUserListDisplay() {
        String email = "list" + RandomStringUtils.secure().nextAlphanumeric(4, 8) + "@test.com";
        usersPage.create(email, "List", "User");

        assertTrue(usersPage.isColumnVisible("Email"), "Email column should be visible");
        assertTrue(usersPage.isColumnVisible("First name"), "First name column should be visible");
        assertTrue(usersPage.isColumnVisible("Last name"), "Last name column should be visible");

        assertEquals(email, usersPage.getCellValue(email, "Email"),
            "Email cell value should match created user's email");
        assertEquals("List", usersPage.getCellValue(email, "First name"), "First name cell value should match");
        assertEquals("User", usersPage.getCellValue(email, "Last name"), "Last name cell value should match");
    }

    @Test
    public void testUserEditing() {
        String email = "edit" + RandomStringUtils.secure().nextAlphanumeric(4, 8) + "@test.com";
        usersPage.create(email, "Before", "Edit");

        usersPage.openUserSettings(email);
        assertEquals(email, usersPage.getFieldValue("email"), "Email field should contain correct value");
        assertEquals("Before", usersPage.getFieldValue("firstName"), "First name field should contain correct value");
        assertEquals("Edit", usersPage.getFieldValue("lastName"), "Last name field should contain correct value");

        usersPage.edit(email, "AfterEdit");
        assertTrue(usersPage.isUserPresent("AfterEdit"), "Updated user name should be present in the list");
    }

    @Test
    public void testEmailValidation() {
        usersPage.openCreatePage();
        usersPage.fillForm("invalid-email", "Test", "User");
        usersPage.submit();
        assertEquals("Incorrect email format", usersPage.getErrorMessage(),
            "Error message for invalid email format should be displayed");
    }

    @Test
    public void testUserDeletion() {
        String email = "delete" + RandomStringUtils.secure().nextAlphanumeric(4, 8) + "@test.com";
        usersPage.create(email, "To", "Delete");
        usersPage.deleteUser(email);
        assertFalse(usersPage.isUserPresent(email), "Deleted user should not be present in the user list");
    }

    @Test
    public void testBulkDelete() {
        usersPage.bulkDelete();
        assertEquals(0, usersPage.getUserCount(), "User list should be empty after bulk delete");
    }
}
