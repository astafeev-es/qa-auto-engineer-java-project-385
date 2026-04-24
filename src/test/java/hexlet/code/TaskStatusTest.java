package hexlet.code;

import hexlet.code.pages.TaskStatusesPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskStatusTest extends BaseTest {

    private TaskStatusesPage statusPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        statusPage = new TaskStatusesPage(driver, wait);
        login();
        statusPage.openTaskStatusesPage();
    }

    @Test
    public void testStatusCreation() {
        String name = "Name" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "slug" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);
        assertTrue(statusPage.isStatusPresent(name), "Created status name should be present in the list");
        assertTrue(statusPage.isStatusPresent(slug), "Created status slug should be present in the list");
    }

    @Test
    public void testStatusListDisplay() {
        String name = "List" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "slug" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);

        assertTrue(statusPage.isColumnVisible("Name"), "Name column should be visible");
        assertTrue(statusPage.isColumnVisible("Slug"), "Slug column should be visible");

        assertEquals(name, statusPage.getCellValue(slug, "Name"), "Status name cell value should match");
        assertEquals(slug, statusPage.getCellValue(slug, "Slug"), "Status slug cell value should match");
    }

    @Test
    public void testStatusEditing() {
        String name = "Before" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "edit" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);

        statusPage.openStatusSettings(slug);
        assertEquals(name, statusPage.getFieldValue("name"), "Name field should contain original status name");
        assertEquals(slug, statusPage.getFieldValue("slug"), "Slug field should contain original status slug");

        String newName = "After" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.edit(slug, newName);
        assertTrue(statusPage.isStatusPresent(newName), "Updated status name should be present in the list");
    }

    @Test
    public void testStatusDeletion() {
        String name = "Delete" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        String slug = "del" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        statusPage.create(name, slug);
        statusPage.deleteStatus(slug);
        assertFalse(statusPage.isStatusPresent(slug), "Deleted status slug should not be present in the list");
    }

    @Test
    public void testBulkDelete() {
        statusPage.bulkDelete();
        assertEquals(0, statusPage.getStatusCount(), "Status list should be empty after bulk delete");
    }
}
