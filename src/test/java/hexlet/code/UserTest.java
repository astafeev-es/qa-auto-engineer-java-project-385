package hexlet.code;

import hexlet.code.pages.UsersPage;
import hexlet.code.utils.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest extends BaseTest {

    private UsersPage usersPage;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        usersPage = new UsersPage(driver, wait);
        login();
        usersPage.open("users");
    }

    @Test
    void testUserCreation() {
        String email = "create" + RandomUtils.randomString() + "@test.com";
        usersPage.openCreatePage();
        assertTrue(usersPage.isCreateFormDisplayed(), "User creation form should be visible with all required fields");
        usersPage.fillForm(email, "John", "Doe");
        usersPage.submit();
        usersPage.open("users");
        assertTrue(usersPage.isUserPresent(email), "Created user should be present in the user list");
    }

    @Test
    void testUserListDisplay() {
        String email = "list" + RandomUtils.randomString() + "@test.com";
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
    void testUserEditing() {
        String email = "edit" + RandomUtils.randomString() + "@test.com";
        usersPage.create(email, "Before", "Edit");

        usersPage.openUserSettings(email);
        assertEquals(email, usersPage.getFieldValue("email"), "Email field should contain correct value");
        assertEquals("Before", usersPage.getFieldValue("firstName"), "First name field should contain correct value");
        assertEquals("Edit", usersPage.getFieldValue("lastName"), "Last name field should contain correct value");

        usersPage.edit(email, "AfterEdit");
        assertEquals(email, usersPage.getCellValue(email, "Email"),
            "Updated row should still belong to the same user email");
        assertEquals("AfterEdit", usersPage.getCellValue(email, "First name"),
            "First name should be updated in the user list");
        assertEquals("EditUpdated", usersPage.getCellValue(email, "Last name"),
            "Last name should contain the saved updated value");
    }

    @Test
    void testEmailValidation() {
        usersPage.openCreatePage();
        usersPage.fillForm("invalid-email", "Test", "User");
        usersPage.submit();
        assertEquals("Incorrect email format", usersPage.getErrorMessage(),
            "Error message for invalid email format should be displayed");
    }

    @Test
    void testUserDeletion() {
        String email = "delete" + RandomUtils.randomString() + "@test.com";
        usersPage.create(email, "To", "Delete");
        usersPage.deleteUser(email);
        assertFalse(usersPage.isUserPresent(email), "Deleted user should not be present in the user list");
    }

    @Test
    void testBulkDelete() {
        String firstEmail = "bulk" + RandomUtils.randomString() + "@test.com";
        String secondEmail = "bulk" + RandomUtils.randomString() + "@test.com";

        usersPage.create(firstEmail, "Bulk", "One");
        usersPage.create(secondEmail, "Bulk", "Two");

        assertTrue(usersPage.getUserCount() > 0, "User list should contain records before bulk delete");
        usersPage.bulkDelete();
        assertEquals(0, usersPage.getUserCount(), "User list should be empty after bulk delete");
        assertFalse(usersPage.isUserPresent(firstEmail), "First created user should be removed by bulk delete");
        assertFalse(usersPage.isUserPresent(secondEmail), "Second created user should be removed by bulk delete");
    }
}
