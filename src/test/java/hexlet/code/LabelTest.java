package hexlet.code;

import hexlet.code.pages.LabelsPage;
import hexlet.code.utils.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LabelTest extends BaseTest {

    private LabelsPage labelsPage;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        labelsPage = new LabelsPage(driver, wait);
        login();
        labelsPage.open();
    }

    @Test
    void testLabelCreation() {
        String name = "Label" + RandomUtils.randomString();
        labelsPage.openCreatePage();
        assertTrue(labelsPage.isCreateFormDisplayed(), "Label creation form should be visible with required fields");
        labelsPage.fillForm(name);
        labelsPage.submit();
        labelsPage.open();
        assertTrue(labelsPage.isLabelPresent(name), "Created label should be present in the labels list");
    }

    @Test
    void testLabelListDisplay() {
        String name = "List" + RandomUtils.randomString();
        labelsPage.create(name);

        assertTrue(labelsPage.getLabelCount() > 0, "Labels table should contain records");
        assertTrue(labelsPage.isColumnVisible("Name"), "Name column should be visible");
        assertTrue(labelsPage.isLabelPresent(name), "Label should be present in the list");
        assertEquals(name, labelsPage.getCellValue(name, "Name"), "Label name cell should match created label name");
    }

    @Test
    void testLabelEditing() {
        String name = "Before" + RandomUtils.randomString();
        labelsPage.create(name);

        labelsPage.openLabelSettings(name);
        assertEquals(name, labelsPage.getFieldValue("name"), "Name field should contain the original label name");

        String newName = "After" + RandomUtils.randomString();
        labelsPage.edit(name, newName);
        assertTrue(labelsPage.isLabelPresent(newName), "Updated label name should be present in the list");
    }

    @Test
    void testLabelDeletion() {
        String name = "Delete" + RandomUtils.randomString();
        labelsPage.create(name);
        labelsPage.deleteLabel(name);
        assertFalse(labelsPage.isLabelPresent(name), "Deleted label should not be present in the list");
    }
}
