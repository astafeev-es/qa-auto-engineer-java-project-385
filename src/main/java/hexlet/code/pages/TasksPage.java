package hexlet.code.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TasksPage extends BasePage {

    @FindBy(name = "title")
    private WebElement titleInput;

    @FindBy(name = "content")
    private WebElement contentInput;

    public TasksPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open Tasks page")
    public void open() {
        elementHelper.click(tasksMenuItem);
    }

    @Step("Create task '{title}' with status '{status}' and assignee '{assignee}'")
    public void create(String title, String status, String assignee) {
        openCreatePage();
        fillForm(title, status, assignee);
        submit();
        open();
    }

    @Step("Fill task form")
    public void fillForm(String title, String status, String assignee) {
        elementHelper.sendKeys(titleInput, title);

        selectFromCombobox("Status", status);
        selectFromCombobox("Assignee", assignee);
    }

    @Step("Open task create page")
    public TasksPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    @Step("Check if create task form is displayed")
    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(titleInput)
            && elementHelper.isVisible(submitButton)
            && elementHelper.isVisibleBy(By.xpath("//*[contains(normalize-space(.), 'Assignee')]"))
            && elementHelper.isVisibleBy(By.xpath("//*[contains(normalize-space(.), 'Status')]"));
    }

    @Step("Check if task '{title}' is in column '{columnName}'")
    public boolean isTaskInColumn(String title, String columnName) {
        String xpath = "//div[h6[contains(., '%s')]]//*[@role='button' and contains(., '%s')]"
            .formatted(columnName, title);
        return elementHelper.isPresent(By.xpath(xpath));
    }

    @Step("Check if task '{title}' is present")
    public boolean isTaskPresent(String title) {
        String xpath = "//*[@data-rfd-draggable-id and contains(., '%s')]".formatted(title);
        return elementHelper.isPresent(By.xpath(xpath));
    }

    @Override
    @Step("Check if column '{columnName}' is visible")
    public boolean isColumnVisible(String columnName) {
        String xpath = "//h6[contains(., '" + columnName + "')]";
        return elementHelper.isVisibleBy(By.xpath(xpath));
    }

    @Step("Edit task '{title}' to new title '{newTitle}'")
    public TasksPage edit(String title, String newTitle) {
        openTaskEdit(title);
        elementHelper.replaceInputValue(titleInput, newTitle);
        elementHelper.click(contentInput);

        submit();
        open();
        return this;
    }

    @Step("Move task '{title}' to status '{newStatus}'")
    public TasksPage moveTask(String title, String newStatus) {
        openTaskEdit(title);
        selectFromCombobox("Status", newStatus);
        submit();
        open();
        return this;
    }

    @Step("Open edit page for task '{title}'")
    public TasksPage openTaskEdit(String title) {
        open();
        String xpath = "//*[@role='button' and contains(., '%s')]//a[contains(., 'Edit')]".formatted(title);
        WebElement editBtn = elementHelper.findElement(By.xpath(xpath));
        elementHelper.click(editBtn);
        return this;
    }

    @Step("Delete task '{title}'")
    public TasksPage deleteTask(String title) {
        openTaskEdit(title);
        elementHelper.click(deleteButton);
        open();
        String xpath = "//*[@data-rfd-draggable-id and contains(., '%s')]".formatted(title);
        elementHelper.waitForInvisibility(By.xpath(xpath));
        return this;
    }

    @Step("Wait for task '{title}' to disappear")
    public TasksPage waitForTaskToDisappear(String title) {
        String xpath = "//*[@data-rfd-draggable-id and contains(., '%s')]".formatted(title);
        elementHelper.waitForInvisibility(By.xpath(xpath));
        return this;
    }

    @Step("Get task count")
    public int getTaskCount() {
        String xpath = "//*[@data-rfd-draggable-id]";
        return elementHelper.findElements(By.xpath(xpath)).size();
    }
}
