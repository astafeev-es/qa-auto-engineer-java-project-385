package hexlet.code;

import hexlet.code.pages.login.LoginPage;
import hexlet.code.pages.TaskStatusesPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskStatusTest extends BaseTest {

    private LoginPage loginPage;
    private TaskStatusesPage statusPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver, wait);
        statusPage = new TaskStatusesPage(driver, wait);
        driver.get(baseUrl);
        loginPage.login(username, password)
            .openTaskStatusesPage();
    }

    @Test
    public void testStatusCreation() {
        String name = "Name" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "slug" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);
        assertTrue(statusPage.isStatusPresent(name));
        assertTrue(statusPage.isStatusPresent(slug));
    }

    @Test
    public void testStatusListDisplay() {
        String name = "List" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "slug" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);

        assertTrue(statusPage.isColumnVisible("Name"));
        assertTrue(statusPage.isColumnVisible("Slug"));

        assertEquals(name, statusPage.getCellValue(slug, "Name"));
        assertEquals(slug, statusPage.getCellValue(slug, "Slug"));
    }

    @Test
    public void testStatusEditing() {
        String name = "Before" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "edit" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);

        statusPage.openStatusSettings(slug);
        assertEquals(name, statusPage.getFieldValue("name"));
        assertEquals(slug, statusPage.getFieldValue("slug"));

        String newName = "After" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.edit(slug, newName);
        assertEquals(newName, statusPage.getCellValue(slug, "Name"));
    }

    @Test
    public void testStatusDeletion() {
        String name = "Delete" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "del" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);
        statusPage.deleteStatus(slug);
        assertFalse(statusPage.isStatusPresent(slug));
    }

    @Test
    public void testBulkDelete() {
        statusPage.bulkDelete();
        assertEquals(0, statusPage.getStatusCount());
    }
}
