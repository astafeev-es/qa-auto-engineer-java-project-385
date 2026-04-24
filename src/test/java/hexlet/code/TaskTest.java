package hexlet.code;

import hexlet.code.pages.TasksPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest extends BaseTest {

    private TasksPage tasksPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        tasksPage = new TasksPage(driver, wait);
        login();
        tasksPage.openTasksPage();
    }

    @Test
    public void testTaskCreation() {
        String title = "Task" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");
        assertTrue(tasksPage.isTaskInColumn(title, "Draft"), "Created task should be present in the 'Draft' column");
    }

    @Test
    public void testTaskFiltration() {
        String title = "FilterTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "To Review", "jane@gmail.com");

        tasksPage.selectFromCombobox("Status", "To Review");
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"),
            "Task should be visible in 'To Review' column after filtration");
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

    @Test
    public void testTaskDeletion() {
        String title = "DeleteTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        tasksPage.deleteTask(title);
        assertFalse(tasksPage.isTaskInColumn(title, "Draft"), "Deleted task should not be present in the column");
    }
}
