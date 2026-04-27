package hexlet.code;

import hexlet.code.pages.TasksPage;
import hexlet.code.pages.LabelsPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest extends BaseTest {

    private TasksPage tasksPage;
    private LabelsPage labelsPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        tasksPage = new TasksPage(driver, wait);
        labelsPage = new LabelsPage(driver, wait);
        login();
        tasksPage.open("tasks");
    }

    @Test
    public void testTaskListDisplay() {
        String title = "ListTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        assertTrue(tasksPage.getTaskCount() > 0, "Task board should contain records");
        assertTrue(tasksPage.isColumnVisible("Draft"), "Draft column should be visible");
        assertTrue(tasksPage.isTaskInColumn(title, "Draft"), "Task should be present on the board");
    }

    @Test
    public void testTaskFiltration() {
        String labelName = "FilterLabel" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        labelsPage.openCreatePage().fillForm(labelName);
        labelsPage.submit();

        tasksPage.open("tasks");

        String title = "FilterTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.openCreatePage();
        tasksPage.fillForm(title, "To Review", "jane@gmail.com");
        tasksPage.selectFromCombobox("Label", labelName);
        new org.openqa.selenium.interactions.Actions(driver).sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();
        tasksPage.submit();
        tasksPage.open("tasks");

        String otherTitle = "OtherTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        hexlet.code.pages.UsersPage usersPage = new hexlet.code.pages.UsersPage(driver, wait);
        usersPage.create("john@gmail.com", "John", "Doe");
        tasksPage.create(otherTitle, "Draft", "john@gmail.com");

        assertTrue(tasksPage.getTaskCount() > 0, "Task board should contain records before filtering");

        // Filter by Status
        tasksPage.selectFromCombobox("Status", "To Review");
        tasksPage.waitForTaskToDisappear(otherTitle);
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"),
            "Task should be visible in 'To Review' column after filtration by Status");
        assertFalse(tasksPage.isTaskInColumn(otherTitle, "Draft"),
            "Irrelevant task should not be visible after filtration by Status");

        // Reset filter - assume clicking clear or selecting empty works. Or just reload page
        tasksPage.open("tasks");

        // Filter by Assignee
        tasksPage.selectFromCombobox("Assignee", "jane@gmail.com");
        tasksPage.waitForTaskToDisappear(otherTitle);
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"),
            "Task should be visible after filtration by Assignee");
        assertFalse(tasksPage.isTaskInColumn(otherTitle, "Draft"),
            "Irrelevant task should not be visible after filtration by Assignee");

        tasksPage.open("tasks");

        // Filter by Label
        tasksPage.selectFromCombobox("Label", labelName); // Could be 'Labels' or 'Label'
        tasksPage.waitForTaskToDisappear(otherTitle);
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"),
            "Task should be visible after filtration by Label");
        assertFalse(tasksPage.isTaskInColumn(otherTitle, "Draft"),
            "Irrelevant task should not be visible after filtration by Label");
    }

    @Test
    public void testTaskDeletion() {
        String title = "DeleteTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        assertTrue(tasksPage.getTaskCount() > 0, "Task board should contain records before delete");
        tasksPage.deleteTask(title);
        assertFalse(tasksPage.isTaskInColumn(title, "Draft"), "Deleted task should not be present in the column");
        assertFalse(tasksPage.isTaskPresent(title), "Deleted task should not be present anywhere in the list/board");
    }

    @Test
    public void testTaskCreation() {
        String title = "Task" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.openCreatePage();
        assertTrue(tasksPage.isCreateFormDisplayed(), "Task creation form should show required fields");
        tasksPage.fillForm(title, "Draft", "jane@gmail.com");
        tasksPage.submit();
        tasksPage.open("tasks");
        assertTrue(tasksPage.isTaskInColumn(title, "Draft"), "Created task should be present in the 'Draft' column");
    }

    @Test
    public void testTaskEditing() {
        String title = "EditTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        String newTitle = "UpdatedTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.edit(title, newTitle);
        assertTrue(tasksPage.isTaskInColumn(newTitle, "Draft"), "Updated task title should be present in the column");
    }

    @Test
    public void testTaskMoving() {
        String title = "MoveTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        tasksPage.moveTask(title, "Published");
        assertTrue(tasksPage.isTaskInColumn(title, "Published"),
            "Task should be present in the 'Published' column after move");
    }
}
