package hexlet.code;

import hexlet.code.pages.login.LoginPage;
import hexlet.code.pages.LabelsPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LabelTest extends BaseTest {

    private LoginPage loginPage;
    private LabelsPage labelsPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver, wait);
        labelsPage = new LabelsPage(driver, wait);
        driver.get(baseUrl);
        loginPage.login(username, password)
            .openLabelsPage();
    }

    @Test
    public void testLabelCreation() {
        String name = "Label" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);
        assertTrue(labelsPage.isLabelPresent(name));
    }

    @Test
    public void testLabelListDisplay() {
        String name = "List" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);

        assertTrue(labelsPage.isLabelPresent(name));
        assertEquals(name, labelsPage.getCellValue(name, "Name"));
    }

    @Test
    public void testLabelEditing() {
        String name = "Before" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);

        labelsPage.openLabelSettings(name);
        assertEquals(name, labelsPage.getFieldValue("name"));

        String newName = "After" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.edit(name, newName);
        assertEquals(newName, labelsPage.getCellValue(newName, "Name"));
    }

    @Test
    public void testLabelDeletion() {
        String name = "Delete" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.create(name);
        labelsPage.deleteLabel(name);
        assertFalse(labelsPage.isLabelPresent(name));
    }
}
