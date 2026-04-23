package hexlet.code;

import hexlet.code.pages.login.LoginPage;
import hexlet.code.pages.TasksPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest extends BaseTest {

    private LoginPage loginPage;
    private TasksPage tasksPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver, wait);
        tasksPage = new TasksPage(driver, wait);
        driver.get(baseUrl);
        loginPage.login(username, password)
            .openTasksPage();
    }

    @Test
    public void testTaskCreation() {
        String title = "Task" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");
        assertTrue(tasksPage.isTaskInColumn(title, "Draft"));
    }

    @Test
    public void testTaskFiltration() {
        String title = "FilterTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "To Review", "jane@gmail.com");

        // Filter by Status
        tasksPage.filterBy("Status", "To Review");
        assertTrue(tasksPage.isTaskInColumn(title, "To Review"));

        // Filter by Status that shouldn't contain the task
        tasksPage.filterBy("Status", "Published");
        assertFalse(tasksPage.isTaskInColumn(title, "To Review"));
    }

    @Test
    public void testTaskEditing() {
        String title = "EditTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        String newTitle = "UpdatedTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.edit(title, newTitle);
        // Так как в приложении баг с редактированием, этот тест может упасть
        assertTrue(tasksPage.isTaskInColumn(newTitle, "Draft"));
    }

    @Test
    public void testTaskMoving() {
        String title = "MoveTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        tasksPage.moveTask(title, "Published");
        assertTrue(tasksPage.isTaskInColumn(title, "Published"));
    }

    @Test
    public void testTaskDeletion() {
        String title = "DeleteTask" + RandomStringUtils.secure().nextAlphanumeric(4, 8);
        tasksPage.create(title, "Draft", "jane@gmail.com");

        tasksPage.deleteTask(title);
        assertFalse(tasksPage.isTaskInColumn(title, "Draft"));
    }
}
