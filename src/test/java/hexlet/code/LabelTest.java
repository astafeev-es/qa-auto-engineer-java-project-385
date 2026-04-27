package hexlet.code;

import hexlet.code.pages.LabelsPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LabelTest extends BaseTest {

    private LabelsPage labelsPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        labelsPage = new LabelsPage(driver, wait);
        login();
        labelsPage.open("labels");
    }

    @Test
    public void testLabelCreation() {
        String name = "Label" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);
        assertTrue(labelsPage.isLabelPresent(name), "Created label should be present in the labels list");
    }

    @Test
    public void testLabelListDisplay() {
        String name = "List" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);

        assertTrue(labelsPage.isLabelPresent(name), "Label should be present in the list");
        assertEquals(name, labelsPage.getCellValue(name, "Name"), "Label name cell should match created label name");
    }

    @Test
    public void testLabelEditing() {
        String name = "Before" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);

        labelsPage.openLabelSettings(name);
        assertEquals(name, labelsPage.getFieldValue("name"), "Name field should contain the original label name");

        String newName = "After" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.edit(name, newName);
        assertTrue(labelsPage.isLabelPresent(newName), "Updated label name should be present in the list");
    }

    @Test
    public void testLabelDeletion() {
        String name = "Delete" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);
        labelsPage.deleteLabel(name);
        assertFalse(labelsPage.isLabelPresent(name), "Deleted label should not be present in the list");
    }
}
