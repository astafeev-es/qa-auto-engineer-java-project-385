package hexlet.code;

import hexlet.code.pages.TaskStatusesPage;
import hexlet.code.utils.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskStatusTest extends BaseTest {

    private TaskStatusesPage statusPage;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        statusPage = new TaskStatusesPage(driver, wait);
        login();
        statusPage.open();
    }

    @Test
    void testStatusCreation() {
        String name = "Name" + RandomUtils.randomString();
        String slug = "slug" + RandomUtils.randomString();
        statusPage.openCreatePage();
        assertTrue(statusPage.isCreateFormDisplayed(),
            "Status creation form should be visible with all required fields");
        statusPage.fillForm(name, slug);
        statusPage.submit();
        statusPage.open();
        assertTrue(statusPage.isStatusPresent(name), "Created status name should be present in the list");
        assertTrue(statusPage.isStatusPresent(slug), "Created status slug should be present in the list");
    }

    @Test
    void testStatusListDisplay() {
        String name = "List" + RandomUtils.randomString();
        String slug = "slug" + RandomUtils.randomString();
        statusPage.create(name, slug);

        assertTrue(statusPage.isColumnVisible("Name"), "Name column should be visible");
        assertTrue(statusPage.isColumnVisible("Slug"), "Slug column should be visible");

        assertEquals(name, statusPage.getCellValue(slug, "Name"), "Status name cell value should match");
        assertEquals(slug, statusPage.getCellValue(slug, "Slug"), "Status slug cell value should match");
    }

    @Test
    void testStatusEditing() {
        String name = "Before" + RandomUtils.randomString();
        String slug = "edit" + RandomUtils.randomString();
        statusPage.create(name, slug);

        statusPage.openStatusSettings(slug);
        assertEquals(name, statusPage.getFieldValue("name"), "Name field should contain original status name");
        assertEquals(slug, statusPage.getFieldValue("slug"), "Slug field should contain original status slug");

        String newName = "After" + RandomUtils.randomString();
        statusPage.edit(slug, newName);
        assertEquals(newName, statusPage.getCellValue(slug, "Name"),
            "Updated status row should contain the saved name");
        assertEquals(slug, statusPage.getCellValue(slug, "Slug"),
            "Updated status row should keep the same slug");
    }

    @Test
    void testStatusDeletion() {
        String name = "Delete" + RandomUtils.randomString();
        String slug = "del" + RandomUtils.randomString();
        statusPage.create(name, slug);
        statusPage.deleteStatus(slug);
        assertFalse(statusPage.isStatusPresent(slug), "Deleted status slug should not be present in the list");
    }

    @Test
    void testBulkDelete() {
        String firstName = "Bulk" + RandomUtils.randomString();
        String firstSlug = "bulk" + RandomUtils.randomString();
        String secondName = "Bulk" + RandomUtils.randomString();
        String secondSlug = "bulk" + RandomUtils.randomString();

        statusPage.create(firstName, firstSlug);
        statusPage.create(secondName, secondSlug);

        assertTrue(statusPage.getStatusCount() > 0, "Status list should contain records before bulk delete");
        statusPage.bulkDelete();
        assertEquals(0, statusPage.getStatusCount(), "Status list should be empty after bulk delete");
        assertFalse(statusPage.isStatusPresent(firstSlug), "First created status should be removed by bulk delete");
        assertFalse(statusPage.isStatusPresent(secondSlug), "Second created status should be removed by bulk delete");
    }
}
