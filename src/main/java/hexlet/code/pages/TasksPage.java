package hexlet.code.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TasksPage extends BasePage {

    @FindBy(name = "title")
    private WebElement titleInput;

    @FindBy(name = "content")
    private WebElement contentInput;

    public TasksPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public void open() {
        elementHelper.click(tasksMenuItem);
    }

    public void create(String title, String status, String assignee) {
        openCreatePage();
        fillForm(title, status, assignee);
        submit();
        open();
    }

    public void fillForm(String title, String status, String assignee) {
        elementHelper.sendKeys(titleInput, title);

        selectFromCombobox("Status", status);
        selectFromCombobox("Assignee", assignee);
    }

    public TasksPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(titleInput)
            && elementHelper.isVisible(submitButton)
            && elementHelper.isVisibleBy(By.xpath("//*[contains(normalize-space(.), 'Assignee')]"))
            && elementHelper.isVisibleBy(By.xpath("//*[contains(normalize-space(.), 'Status')]"));
    }

    public boolean isTaskInColumn(String title, String columnName) {
        try {
            String xpath = "//div[h6[contains(., '%s')]]//*[@role='button' and contains(., '%s')]"
                .formatted(columnName, title);
            return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTaskPresent(String title) {
        try {
            String xpath = "//*[@data-rfd-draggable-id and contains(., '%s')]".formatted(title);
            return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isColumnVisible(String columnName) {
        String xpath = "//h6[contains(., '" + columnName + "')]";
        return elementHelper.isVisibleBy(By.xpath(xpath));
    }

    public TasksPage edit(String title, String newTitle) {
        openTaskEdit(title);
        elementHelper.sendKeys(titleInput, newTitle);
        elementHelper.click(contentInput);

        submit();
        open();
        return this;
    }

    public TasksPage moveTask(String title, String newStatus) {
        openTaskEdit(title);
        selectFromCombobox("Status", newStatus);
        submit();
        open();
        return this;
    }

    public TasksPage openTaskEdit(String title) {
        open();
        String xpath = "//*[@role='button' and contains(., '%s')]//a[contains(., 'Edit')]".formatted(title);
        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        elementHelper.click(editBtn);
        return this;
    }

    public TasksPage deleteTask(String title) {
        openTaskEdit(title);
        elementHelper.click(deleteButton);
        open();
        String xpath = "//*[@data-rfd-draggable-id and contains(., '%s')]".formatted(title);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
        return this;
    }

    public TasksPage waitForTaskToDisappear(String title) {
        String xpath = "//*[@data-rfd-draggable-id and contains(., '%s')]".formatted(title);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
        return this;
    }

    public int getTaskCount() {
        String xpath = "//*[@data-rfd-draggable-id]";
        return driver.findElements(By.xpath(xpath)).size();
    }
}
