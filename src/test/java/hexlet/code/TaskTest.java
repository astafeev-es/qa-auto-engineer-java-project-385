package hexlet.code;

import hexlet.code.pages.TasksPage;
import hexlet.code.pages.LabelsPage;
import hexlet.code.pages.UsersPage;
import hexlet.code.utils.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest extends BaseTest {

    private TasksPage tasksPage;
    private LabelsPage labelsPage;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        tasksPage = new TasksPage(driver, wait);
        labelsPage = new LabelsPage(driver, wait);
        login();
        tasksPage.open("tasks");
    }

    @Test
    void testTaskListDisplay() {
        String title = "ListTask" + RandomUtils.randomString();
        tasksPage.create(title, "Draft", "jane@gmail.com");

        assertTrue(tasksPage.getTaskCount() > 0, "Task board should contain records");
        assertTrue(tasksPage.isColumnVisible("Draft"), "Draft column should be visible");
        assertTrue(tasksPage.isTaskInColumn(title, "Draft"), "Task should be present on the board");
    }

    @Test
    void testTaskFiltration() {
        String labelName = "FilterLabel" + RandomUtils.randomString();
        labelsPage.openCreatePage().fillForm(labelName);
        labelsPage.submit();

        tasksPage.open("tasks");

        String title = "FilterTask" + RandomUtils.randomString();
        tasksPage.openCreatePage();
        tasksPage.fillForm(title, "To Review", "jane@gmail.com");
        tasksPage.selectFromCombobox("Label", labelName);
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        tasksPage.submit();
        tasksPage.open("tasks");

        String otherTitle = "OtherTask" + RandomUtils.randomString();
        UsersPage usersPage = new UsersPage(driver, wait);
        usersPage.create("john@gmail.com", "John", "Doe");
        tasksPage.create(otherTitle, "Draft", "john@gmail.com");

        assertTrue(tasksPage.getTaskCount() > 0, "Task board should contain records before filtering");

        tasksPage.selectFromCombobox("Status", "To Review");
        tasksPage.waitForTaskToDisappear(otherTitle);
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"),
            "Task should be visible in 'To Review' column after filtration by Status");
        assertFalse(tasksPage.isTaskInColumn(otherTitle, "Draft"),
            "Irrelevant task should not be visible after filtration by Status");

        tasksPage.open("tasks");

        tasksPage.selectFromCombobox("Assignee", "jane@gmail.com");
        tasksPage.waitForTaskToDisappear(otherTitle);
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"),
            "Task should be visible after filtration by Assignee");
        assertFalse(tasksPage.isTaskInColumn(otherTitle, "Draft"),
            "Irrelevant task should not be visible after filtration by Assignee");

        tasksPage.open("tasks");

        tasksPage.selectFromCombobox("Label", labelName); // Could be 'Labels' or 'Label'
        tasksPage.waitForTaskToDisappear(otherTitle);
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"),
            "Task should be visible after filtration by Label");
        assertFalse(tasksPage.isTaskInColumn(otherTitle, "Draft"),
            "Irrelevant task should not be visible after filtration by Label");
    }

    @Test
    void testTaskDeletion() {
        String title = "DeleteTask" + RandomUtils.randomString();
        tasksPage.create(title, "Draft", "jane@gmail.com");

        assertTrue(tasksPage.getTaskCount() > 0, "Task board should contain records before delete");
        tasksPage.deleteTask(title);
        assertFalse(tasksPage.isTaskInColumn(title, "Draft"), "Deleted task should not be present in the column");
        assertFalse(tasksPage.isTaskPresent(title), "Deleted task should not be present anywhere in the list/board");
    }

    @Test
    void testTaskCreation() {
        String title = "Task" + RandomUtils.randomString();
        tasksPage.openCreatePage();
        assertTrue(tasksPage.isCreateFormDisplayed(), "Task creation form should show required fields");
        tasksPage.fillForm(title, "Draft", "jane@gmail.com");
        tasksPage.submit();
        tasksPage.open("tasks");
        assertTrue(tasksPage.isTaskInColumn(title, "Draft"), "Created task should be present in the 'Draft' column");
    }

    @Test
    void testTaskEditing() {
        String title = "EditTask" + RandomUtils.randomString();
        tasksPage.create(title, "Draft", "jane@gmail.com");

        String newTitle = "UpdatedTask" + RandomUtils.randomString();
        tasksPage.edit(title, newTitle);
        assertTrue(tasksPage.isTaskInColumn(newTitle, "Draft"), "Updated task title should be present in the column");
    }

    @Test
    void testTaskMoving() {
        String title = "MoveTask" + RandomUtils.randomString();
        tasksPage.create(title, "Draft", "jane@gmail.com");

        tasksPage.moveTask(title, "Published");
        assertTrue(tasksPage.isTaskInColumn(title, "Published"),
            "Task should be present in the 'Published' column after move");
    }
}
